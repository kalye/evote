package edu.uga.cs.evote.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uga.cs.evote.db.DbUtils;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class UpdateVoterProfileQuery extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void doPost( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{

		Connection  conn = null;
		PrintWriter toClient = response.getWriter();
		
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		    
			response.setContentType("text/html");

		    //String realPath = getServletContext().getRealPath("/register.html");
		    
		    
	        
	        // get a database connection
	        try {
	            conn = DbUtils.connect();
	        } 
	        catch (Exception seq) {
	            System.err.println( "Unable to obtain a database connection" );
	        }
	        
	        if( conn == null ) {
	            System.out.println( "failed to connect to the database" );
	            return;
	        }
	        
	        LogicLayer logicLayer = new LogicLayerImpl(conn);	        
		    
		    response.setContentType("text/html");

		    List<Voter> voters = logicLayer.findAllVoters();
		    
		    String save = request.getParameter("save");
	    	String cookieValue = "";
	    	
		    Cookie[] cookies = request.getCookies();

		    for(Cookie cookie: cookies){

		    	if(cookie.getName().equals("voterUser")){
		    			
		    		cookieValue = cookie.getValue();
		    	}
		    }

		    for(Voter voter: voters){
		    	
		    	if(cookieValue.equals(voter.getUserName())){

		    		String originalFirst = voter.getFirstName();
		    		String originalLast = voter.getLastName();
		    		String originalZip = voter.getZipCode();
		    		String originalEmail = voter.getEmailAddress();
		    		String originalAddress = voter.getAddress();
		    		int originalAge = voter.getAge();
		    		
		    		//toClient.println("<p>District name: " + originalName + ", zip: " + originalZip + "</p>");
		    		
		    		if(save != null){
			    		
		    			String first = request.getParameter("firstName");
		    			String last = request.getParameter("lastName");
		    			String email = request.getParameter("email");
		    			String address = request.getParameter("address");
		    			String zip = request.getParameter("zip");
		    			String age = request.getParameter("age");

		    			if(first.equals("")){
		    				first = originalFirst;
		    			}
		    			if(last.equals("")){
		    				last = originalLast;
		    			}
		    			if(email.equals("")){
		    				email = originalEmail;
		    			}
		    			if(address.equals("")){
		    				address = originalAddress;
		    			}
		    			if(age.equals("")){
		    				age = Integer.toString(originalAge);
		    			}
				        if(zip.equals("")){
				        	zip = originalZip;
				        }
				        
				        try{
		    				
				        	ObjectLayer objectLayer = new ObjectLayerImpl();
					        PersistenceLayer persistence = new PersistenceLayerImpl( conn, objectLayer ); 
					        objectLayer = new ObjectLayerImpl(persistence);
					        persistence.setObjectLayer(objectLayer);
		    				
		    				ElectoralDistrict district = objectLayer.createElectoralDistrict();
		    				district.setZipCode(zip);
		    				List<ElectoralDistrict> districts = objectLayer.findElectoralDistrict(district);
		    				if(districts.size() == 0){
		    					throw new Exception("No district exists for that zip code");
		    				}
					        
		    				district = districts.get(0);
		    				
					        Voter modelVoter = objectLayer.createVoter();
					        modelVoter.setEmailAddress(email);
					        
					        ElectionsOfficer modelOfficer = objectLayer.createElectionsOfficer();
					        modelOfficer.setEmailAddress(email);
					        
					        voters = objectLayer.findVoter(modelVoter);
					        List<ElectionsOfficer> officers = objectLayer.findElectionsOfficer(modelOfficer);
					        
					        if(!email.equals(originalEmail)){
					        	if(voters.size() > 0 || officers.size() > 0){
					        		
					        		String redirect = new String("duplicateVoter.html");
				    				response.sendRedirect(redirect);
				    				conn.close();
				    				return;
					        	}
					        }
					        
					        voter.setFirstName(first);
					        voter.setLastName(last);
					        voter.setZipCode(zip);
					        voter.setElectoralDistrict(district);
					        voter.setAge(Integer.parseInt(age));
					        voter.setEmailAddress(email);
					        voter.setAddress(address);
					        
					        objectLayer.storeVoter(voter);
		    				String redirect = new String("successVoter.html");
		    				response.sendRedirect(redirect);
		    				conn.close();
		    				return;
		    				
		    			} catch(Exception e){
		    				
		    				//toClient.println("<p>" + e + "</p>");
		    				String redirect = new String("invalidDataVoter.html");
		    	        	response.sendRedirect(redirect);   
		    				toClient.close();
		        		    conn.close();
		        		    return;
		    			}
			    	} 
		    	}
		    }

		   conn.close();
		   toClient.close();			
				
					    
		} catch(Exception e){

		    toClient.println("<p>" + e + "</p>");
		    toClient.close();
		    try {
				conn.close();
			} catch (SQLException e1) {
			}
		}//try
		
	}//doPost
}
