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
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class EditIssueQuery extends HttpServlet {


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
	        ObjectLayer objectLayer = new ObjectLayerImpl();
	        PersistenceLayer persistence = new PersistenceLayerImpl( conn, objectLayer ); 
	        objectLayer = new ObjectLayerImpl(persistence);
	        persistence.setObjectLayer(objectLayer);
	        
		    response.setContentType("text/html");

		    String save = request.getParameter("save");
	    	String delete = request.getParameter("delete");
	    	String cookieValue = "";
	    	
	    	Issue issue = null;
	    	
		    Cookie[] cookies = request.getCookies();
		    Cookie cookie = null;
		    
		    if(cookies.length > 0){
		    	for(int i = 0; i < cookies.length; i++){
		    		cookie = cookies[i];
		    		if(cookie.getName().equals("issue")){
		    			cookieValue = cookie.getValue();

		    		}
		    	}
		    }

		    long id = Long.parseLong(cookieValue);
		    issue = objectLayer.createIssue();
		    issue.setId(id);
		    issue = objectLayer.findIssue(issue).get(0);

		    String originalName = issue.getQuestion();
		    		
		    if(save != null){
			    		
		    	String name = request.getParameterValues("name")[0];

		    	if(name.equals("")){
				     name = originalName;
				} 
				       
				try{
					Issue modelIssue = objectLayer.createIssue();
					modelIssue.setQuestion(name);
					modelIssue.setBallot(issue.getBallot());
					
					List<Issue> issues = objectLayer.findIssue(modelIssue);
					        
					if(!name.equals(originalName)){
						if(issues.size() > 0){
					        String redirect = new String("objectAlreadyExists.html");
			    	        response.sendRedirect(redirect);   
			    			toClient.close();
			        		conn.close();
			        		return;
					    }
					} 
					        
					issue.setQuestion(name);
					
					objectLayer.storeIssue(issue);
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
			    Cookie sendCookie = new Cookie("issueDelete", "" + id);
			    sendCookie.setMaxAge(60*60);
				response.addCookie(sendCookie);
				String redirect = new String("AreYouSure.html");
				response.sendRedirect(redirect);
				toClient.close();
				conn.close();
				return;
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
