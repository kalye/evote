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

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.db.DbUtils;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class EditDistrictQuery extends HttpServlet {

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

		    List<ElectoralDistrict> electoralDistricts = logicLayer.findAllElectoralDistricts();
		    
		    String save = request.getParameter("save");
	    	String delete = request.getParameter("delete");
	    	String cookieValue = "";
	    	
		    Cookie[] cookies = request.getCookies();
		    Cookie cookie = null;
		    
		    if(cookies.length > 0){
		    	for(int i = 0; i < cookies.length; i++){
		    		cookie = cookies[i];
		    		if(cookie.getName().equals("district")){
		    			cookieValue = cookie.getValue();
		    			
		    			//toClient.println("<p>Cookie found: " + cookieValue + "</p>");
		    			cookie.setMaxAge(0);
		    		}
		    	}
		    }

		    for(ElectoralDistrict district: electoralDistricts){
		    	
		    	if(cookieValue.equals("" + district.getId())){
		    		
		    		//toClient.println("<p>District found.</p>");

		    		String originalName = district.getName();
		    		String originalZip = district.getZipCode();
		    		
		    		//toClient.println("<p>District name: " + originalName + ", zip: " + originalZip + "</p>");
		    		
		    		if(save != null){
			    		
		    			String name = request.getParameterValues("name")[0];
		    			String zip = request.getParameterValues("zip")[0];

		    			if(name.equals("")){
				        	name = originalName;
				        } 
				        if(zip.equals("")){
				        	zip = originalZip;
				        }
				        
				        //toClient.println("<p>Name: " + name + ", zip: " + zip + "</p>");
				        
				        try{
		    				
		    				int z = Integer.parseInt(zip);
		    				if(z < 0){
		    					throw new EVException("Zip code can't be negative");
		    				}
		    				
		    				ObjectLayer objectLayer = new ObjectLayerImpl();
					        PersistenceLayer persistence = new PersistenceLayerImpl( conn, objectLayer ); 
					        objectLayer = new ObjectLayerImpl(persistence);
					        persistence.setObjectLayer(objectLayer);
					        
					        if(zip.length() != 5 || name == ""){
		    					throw new EVException("Invalid values entered");
		    				}
					        
					        ElectoralDistrict modelElectoralDistrict = objectLayer.createElectoralDistrict();
					        modelElectoralDistrict.setZipCode(zip);
					        
					        ElectoralDistrict modelElectoralDistrict2 = objectLayer.createElectoralDistrict();
					        modelElectoralDistrict2.setName(name);
					        
					        List<ElectoralDistrict> electoralDistricts3 = objectLayer.findElectoralDistrict(modelElectoralDistrict);
					        List<ElectoralDistrict> electoralDistricts2 = objectLayer.findElectoralDistrict(modelElectoralDistrict2);
					        
					        
					        if(name.equals(originalName)){
					        	if(zip.equals(originalZip)){
					        		
					        	} else if(electoralDistricts3.size() > 0){
					        		throw new EVException("Can't set two districts to the same zip");
					        	}
					        } else if(zip.equals(originalZip)){
					        	if(electoralDistricts2.size() > 0){
					        		throw new EVException("Can't give two districts the same name");
					        	}
					        } else{
					        	if(electoralDistricts3.size() > 0 || electoralDistricts2.size() > 0){
					        	
					        			throw new EVException("Can't give two districts the same name and/or zip");
					        	}
					        }//if
					        
					        district.setName(name);
					        district.setZipCode(zip);
		    				objectLayer.storeElectoralDistrict(district);
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
			    	} else if(delete != null){
			    		
			    		for (Cookie c : request.getCookies()) {
			    		    
							if(!(c.getName().equals("voterUser") || c.getName().equals("electionOfficerUser"))){
								c.setValue("");
								c.setMaxAge(0);
								c.setPath("/");
							}//if
			    		    response.addCookie(c);
			    		}
			    		
			    		Cookie sendCookie = new Cookie("districtDelete", "" + district.getId());
			    		sendCookie.setMaxAge(60*60);
					    response.addCookie(sendCookie);
					    String redirect = new String("AreYouSure.html");
					    response.sendRedirect(redirect);
					    toClient.close();
					    conn.close();
					    return;
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
