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
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class UpdateEOProfileQuery extends HttpServlet {

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

		    List<ElectionsOfficer> officers = logicLayer.findAllElectionsOfficers();
		    
		    String save = request.getParameter("save");
	    	String cookieValue = "";
	    	
		    Cookie[] cookies = request.getCookies();
		    
		    for(Cookie cookie: cookies){

		    	if(cookie.getName().equals("electionsOfficerUser")){
		    		cookieValue = cookie.getValue();

		    	}
		    }

		    for(ElectionsOfficer officer: officers){
		    	
		    	if(cookieValue.equals(officer.getUserName())){

		    		String originalFirst = officer.getFirstName();
		    		String originalLast = officer.getLastName();
		    		String originalEmail = officer.getEmailAddress();
		    		String originalUserName = officer.getUserName();
		    		
		    		//toClient.println("<p>District name: " + originalName + ", zip: " + originalZip + "</p>");
		    		
		    		if(save != null){
			    		
		    			String first = request.getParameter("firstName");
		    			String last = request.getParameter("lastName");
		    			String email = request.getParameter("email");

		    			if(first.equals("")){
		    				first = originalFirst;
		    			}
		    			if(last.equals("")){
		    				last = originalLast;
		    			}
		    			if(email.equals("")){
		    				email = originalEmail;
		    			}
				        
				        try{
		    				
				        	ObjectLayer objectLayer = new ObjectLayerImpl();
					        PersistenceLayer persistence = new PersistenceLayerImpl( conn, objectLayer ); 
					        objectLayer = new ObjectLayerImpl(persistence);
					        persistence.setObjectLayer(objectLayer);
		    				
					        Voter modelVoter = objectLayer.createVoter();
					        modelVoter.setEmailAddress(email);
					        
					        ElectionsOfficer modelOfficer = objectLayer.createElectionsOfficer();
					        modelOfficer.setEmailAddress(email);
					        
					        
					        List<Voter> voters = objectLayer.findVoter(modelVoter);
					        officers = objectLayer.findElectionsOfficer(modelOfficer);
					        
					        if(!email.equals(originalEmail)){
					        	if(voters.size() > 0 || officers.size() > 0){
					        		
					        		String redirect = new String("duplicateOfficer.html");
				    				response.sendRedirect(redirect);
				    				conn.close();
				    				return;
					        	}
					        }
					        
					        officer.setFirstName(first);
					        officer.setLastName(last);
					        officer.setEmailAddress(email);
					        
					        objectLayer.storeElectionsOfficer(officer);
		    				String redirect = new String("successOfficer.html");
		    				response.sendRedirect(redirect);
		    				conn.close();
		    				return;
		    				
		    			} catch(Exception e){
		    				
		    				//toClient.println("<p>" + e + "</p>");
		    				String redirect = new String("invalidDataOfficer.html");
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

		    e.printStackTrace(toClient);
		    toClient.close();
		    try {
				conn.close();
			} catch (SQLException e1) {
			}
		}//try
		
	}//doPost
}
