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
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class ChangeVoterPasswordQuery extends HttpServlet {

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

		    String cookieValue = "";
            Cookie[] cookies = request.getCookies();
            Cookie cookie = null;


            //if(cookies.length > 0){                                                                                                                               


            List<Voter> voters = logicLayer.findAllVoters();
            //      String ssid = request.getSession().getId();                                                                                                     
            //Session session = SessionManager.getSessionById(ssid);                                                                                                
            //User user = session.getUser();                                                                                                                        

		    for(int j = 0; j < cookies.length; j++){

				cookie = cookies[j];
	
	            if(cookie.getName().equals("voterUser")){
	                        	
	                 cookieValue = cookie.getValue();
	                 for(int i = 0; i < voters.size(); i++){
	
	                	 Voter voter  = voters.get(i);  
	                	 if(voter.getUserName().equals(cookieValue)){
	                           
	                		 try{
	                			 
	                			ObjectLayer objectLayer = new ObjectLayerImpl();
	 					        PersistenceLayer persistence = new PersistenceLayerImpl( conn, objectLayer ); 
	 					        objectLayer = new ObjectLayerImpl(persistence);
	 					        persistence.setObjectLayer(objectLayer);
	 		    				
	 					        
		                		 String pass = request.getParameter("pass").trim();
		                		 String newPass = request.getParameter("newPass").trim();
		                		 String reEnter = request.getParameter("reEnter").trim();
		                		 
		                		 String originalPass = voter.getPassword();
		                		 
		                		 if(!pass.equals(originalPass) || !newPass.equals(reEnter) || newPass.equals("")){
		                			 
		                			 throw new Exception("invalid data");
		                		 } 
		                		 
		                		 voter.setPassword(newPass);
		                		 objectLayer.storeVoter(voter);
		                		 
		                		 String redirect = "successVoter.html";
		                		 response.sendRedirect(redirect);
		                		 conn.close();
		                		 toClient.close();
		                		 return;
	                		 } catch(Exception e){
	                			 
	                			 String redirect = "invalidDataVoter.html";
	                			 response.sendRedirect(redirect);
	                			 conn.close();
	                			 toClient.close();
	                			 return;
	                		 }
	                	 }
	                 }
	             }                                               
		    }
		} catch(Exception e){
			toClient.println(e.getMessage());
			toClient.close();
			try {
				
				conn.close();
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
			return;
		}
	}//doPost
}
