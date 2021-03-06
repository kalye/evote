package edu.uga.cs.evote.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uga.cs.evote.db.DbUtils;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class UpdateIssueQuery extends HttpServlet {

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
	        
	        ObjectLayer objectLayer = new ObjectLayerImpl();
	        PersistenceLayer persistence = new PersistenceLayerImpl( conn, objectLayer ); 
	        objectLayer = new ObjectLayerImpl(persistence);
	        persistence.setObjectLayer(objectLayer);
	        LogicLayer logicLayer = new LogicLayerImpl(conn);
	        

		    response.setContentType("text/html");

		    String result = "<!DOCTYPE html>" +
    				"<html lang='en'>" +
    					"<head>" +
    						"<title>Update Issue</title>" +
    						"<meta charset='utf-8'>" +
    						"<meta name='viewport' content='width=device-width, initial-scale=1'>" +
    						"<link rel='stylesheet'" +
    							" href='http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'>" +
    						"<script" +
    							" src='https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js'></script>" +
    						"<script" +
    							" src='http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js'></script>" +
    					"</head>" +
    					"<body>" +
    						"<nav class='navbar navbar-default'>" +
    							"<div class='container-fluid'>" +
    								"<div class='navbar-header'>" +
    									"<a class='navbar-brand' href='ElectionsOfficerHome.html'>eVote - Elections Officer</a>" +
    								"</div>" +
    								"<ul class='nav navbar-nav'>" +
    									"<li class='dropdown'><a class='dropdown-toggle'" +
    										" data-toggle='dropdown' href='#'>View/Update <span class='caret'></span></a>" +
    										"<ul class='dropdown-menu'>" +
    										"<li><a href='ListBallotsQuery'>Ballots</a></li>" +
											"<li><a href='searchDistricts.html'>Electoral Districts</a></li>" +
											"<li><a href='searchIssues.html'>Issues</a></li>" +
											"<li><a href='searchElections.html'>Elections</a></li>" +
											"<li><a href='searchCandidatess.html'>Candidates</a></li>" +
											"<li><a href='searchParties.html'>Political Parties</a></li>" +
    										"</ul></li>" +
    								"<li><a href='editProfile.html'>Update Profile</a></li>" +
    								"<li><a href='successAnon.html'>Log Out</a>" +
    					"</ul></div></nav>";

		    Cookie[] cookies = request.getCookies();
		    Cookie cookie = null;
		    String cookieValue = "";
		    
		   // if(cookies.length > 0){
		    	//for(int i = 0; i < cookies.length; i++){
		    		//cookie = cookies[i];
		    		//if(cookie.getName().equals("ballot")){
		    			//cookieValue = cookie.getValue();
		    		//}
		    	//}
		   // }
		    
		    //long id = Long.parseLong(cookieValue);
		    //List<Ballot> ballots = logicLayer.findAllBallots();
    		//Ballot ballot = ballots.get((int)id - 1);
		    //Ballot ballot = objectLayer.createBallot();
		    //ballot.setId(id);
		    //ballot = objectLayer.findBallot(ballot).get(0);
    		
	        //System.out.println("Ballot for updating issue: " + ballot.getId());

		    
		    List<Issue> issues = logicLayer.findAllIssues();
		    
		    String update;
		    String delete;
		    
		    for(Issue issue: issues){
		    	
		    	update = request.getParameter("update" + issue.getId());
		    	delete = request.getParameter("delete" + issue.getId());
		    	
		    	if(update != null){

		    		if(issue.getBallot().getOpenDate().before(new Date()) || 
		    				issue.getBallot().getOpenDate().equals(new Date())){
		    			
		    			String redirect = new String("noEdit.html");
					    response.sendRedirect(redirect);
					    conn.close();
					    toClient.close();
					    return;
		    		}
		    		
		    		for (Cookie c : request.getCookies()) {
		    		    
						if(!(c.getName().equals("voterUser") || c.getName().equals("electionOfficerUser"))){
							c.setValue("");
							c.setMaxAge(0);
							c.setPath("/");
						}//if
		    		    response.addCookie(c);
		    		}
				   
				    cookie = new Cookie("issue", "" + issue.getId());
				    cookie.setMaxAge(60*60);
				    response.addCookie(cookie);
				    
				    result += "<div class='container'>" +
				    				"<h3>Update Issue</h3>" +
				    				"<form method=post action='EditIssueQuery'>" +
				    					"<div class='form-group'>" +
				    						"<label for='name'>Name:</label> <input type='name'" +
				    						"class='form-control' id='name' name='name' placeholder='" + issue.getQuestion() + "'>" +
				    					"</div>" +
				    					"<p>District: " + issue.getBallot().getElectoralDistrict().getName() + " ( " + 
											issue.getBallot().getElectoralDistrict().getZipCode() + " )"+ "</p>" +
				    					"<button type='submit' class='btn btn-default' name='save'>Save Changes</button>" +
				    					"<a type='button' class='btn btn-default' href='ListBallotsQuery'>Cancel</a>" +
				    					"<button type='delete' class='btn btn-default' name='delete'>Delete Issue</button>" +
				    				"</form>" +
				    			"</div>" +
				    		"</body>" +
				    	"</html>";
				   toClient.println(result);
				   conn.close();
				   toClient.close();			
		    	} else if(delete != null){

		    		if(issue.getBallot().getOpenDate().before(new Date()) || 
		    				issue.getBallot().getOpenDate().equals(new Date())){
		    			
		    			String redirect = new String("noEdit.html");
					    response.sendRedirect(redirect);
					    conn.close();
					    toClient.close();
					    return;
		    		}

		    		for (Cookie c : request.getCookies()) {
		    		    
						if(!(c.getName().equals("voterUser") || c.getName().equals("electionOfficerUser"))){
							c.setValue("");
							c.setMaxAge(0);
							c.setPath("/");
						}//if
		    		    response.addCookie(c);
		    		}
		    		
		    		Cookie sendCookie = new Cookie("issueDelete", "" + issue.getId());
		    		sendCookie.setMaxAge(60*60);
				    response.addCookie(sendCookie);
				    String redirect = new String("AreYouSure.html");
				    response.sendRedirect(redirect);
				    conn.close();
				    toClient.close();
				    return;
		    	}
		    }			    
		} catch(Exception e){

			try{
				conn.close();
			} catch(Exception f){
				
			}
		    toClient.println("<p>" + e + "</p>");
		    toClient.close();
		}//try
		
	}//doPost
}
