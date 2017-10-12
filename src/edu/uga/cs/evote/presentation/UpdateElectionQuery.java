package edu.uga.cs.evote.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uga.cs.evote.db.DbUtils;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class UpdateElectionQuery extends HttpServlet{
	
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

		    String result = "<!DOCTYPE html>" +
    				"<html lang='en'>" +
    					"<head>" +
    						"<title>Update Election</title>" +
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

		    Cookie cookie = null;
		    
		    List<Election> elections = logicLayer.findAllElections();
		    
		    String update;
		    String delete;
		    
		    for(Election election: elections){
		    	
		    	update = request.getParameter("update" + election.getId());
		    	delete = request.getParameter("delete" + election.getId());
		    	
		    	if(update != null){

		    		if(election.getBallot().getOpenDate().before(new Date()) || election.getBallot().getOpenDate().equals(new Date())){
		    			
		    			String redirect = new String("noEdit.html");
					    response.sendRedirect(redirect);
					    conn.close();
					    toClient.close();
					    return;
		    		}
		    		
		    		for (Cookie c : request.getCookies()) {
		    		    
				    	if(!(c.getName().equals("voterUser") || c.getName().equals("electionsOfficerUser"))){
			    			c.setValue("");
			    		    c.setMaxAge(0);
			    		    c.setPath("/");
			
			    		    response.addCookie(c);
				    	}//if
		    		}
				    
				    cookie = new Cookie("election", "" + election.getId());
				    cookie.setMaxAge(60*60);
				    response.addCookie(cookie);
				    
				    result += "<div class='container'>" +
				    				"<h3>Update Election</h3>" +
				    				"<form method=post action='EditElectionQuery'>" +
				    				"<p>District: " + election.getBallot().getElectoralDistrict().getName() + " ( " + 
										election.getBallot().getElectoralDistrict().getZipCode() + " )"+ "</p>" +
				    				"<div class='form-group'>" +
				    						"<label for='name'>Name:</label> <input type='name'" +
				    						"class='form-control' id='name' name='name' placeholder='" + election.getOffice() + "'>" +
				    					"</div>" +
				    					"<div class='form-group'>" +
				    						"<label for='isPartisan'>isPartisan:</label> <input type='checkbox' name='isPartisan'";
				    
				    if(election.getIsPartisan()){ result += " checked>"; }
				    else result += ">";
				    result += 			"</div>" +
				    					"<button type='submit' class='btn btn-default' name='save'>Save Changes</button>" +
				    					"<a type='button' class='btn btn-default' href='ListBallotsQuery'>Cancel</a>" +
				    					"<button type='delete' class='btn btn-default' name='delete'>Delete Election</button>" +
				    					"</form>";
				    
				    result += "<h3>Candidates</h3>";
					List<Candidate> candidates = election.getCandidates();
					
					result += "<form method=post action='UpdateCandidateQuery'>";
					for(int j = 0; j < candidates.size(); j++){
						
						Candidate candidate = candidates.get(j);
			
						result += 	"<p style='font-size:20px'>" + candidate.getName() + " ( " + candidate.getPoliticalParty().getName() + " )</p>" +
									"<button type='submit' class='btn btn-default' value='update' name='update" + candidate.getId() + "'>Update</button>" +
									"<button type='submit' class='btn btn-default' value='delete' name='delete" + candidate.getId() + "'>Delete</button>" +
									"<br /><br /><br />";
						}//for
					
					result += "</form>";
					result += "<a type='button' class='btn btn-default' href='createCandidate.html' style='font-size: 16px'><strong>Add Candidate</strong></a>" +
							"</div>" +
						"</body>" +
					"</html>";
				   toClient.println(result);
				   conn.close();
				   toClient.close();			
		    	} else if(delete != null){

		    		if(election.getCandidates().size() > 0 || election.getBallot().getOpenDate().before(new Date()) || election.getBallot().getOpenDate().equals(new Date())){
		    			
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
		    		
		    		Cookie sendCookie = new Cookie("electionDelete", "" + election.getId());
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
		    e.printStackTrace(toClient);
		    toClient.close();
		}//try
		
	}//doPost
	
public void doGet( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{
		
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

		    String result = "<!DOCTYPE html>" +
    				"<html lang='en'>" +
    					"<head>" +
    						"<title>Update Election</title>" +
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
    											"<li><a href='ListDistrictsQuery'>Electoral Districts</a></li>" +
    											"<li><a href='ListPartiesQuery'>Political Parties</a></li>" +
    											"<li><a href='ListBallotsQuery'>Ballots</a></li>" +
    										"</ul></li>" +
    								"<li><a href='editProfile.html'>Update Profile</a></li>" +
    								"<li><a href='successAnon.html'>Log Out</a>" +
    					"</ul></div></nav>";

		    Cookie cookie = null;
		    
		    List<Election> elections = logicLayer.findAllElections();
		    
		    String update;
		    String delete;
		    
		    for(Election election: elections){
		    	System.out.println("TEAM4: Looking at election " + election.getId());
		    	
		    	update = request.getParameter("update" + election.getId());
		    	delete = request.getParameter("delete" + election.getId());
		    	System.out.println("TEAM4: update is " + update);
		    	if(update != null){

		    		if(election.getBallot().getOpenDate().before(new Date()) || election.getBallot().getOpenDate().equals(new Date())){
		    			
		    			String redirect = new String("noEdit.html");
					    response.sendRedirect(redirect);
					    conn.close();
					    toClient.close();
					    return;
		    		}
		    		
		    		for (Cookie c : request.getCookies()) {
		    		    
				    	if(!(c.getName().equals("voterUser") || c.getName().equals("electionsOfficerUser"))){
			    			c.setValue("");
			    		    c.setMaxAge(0);
			    		    c.setPath("/");
			
			    		    response.addCookie(c);
				    	}//if
		    		}
				    
				    cookie = new Cookie("election", "" + election.getId());
				    cookie.setMaxAge(60*60);
				    response.addCookie(cookie);
				    System.out.println("TEAM4: Got the cookie");
				    result += "<div class='container'>" +
				    				"<h3>Update Election</h3>" +
				    				"<form method=post action='EditElectionQuery'>" +
				    				"<p>District: " + election.getBallot().getElectoralDistrict().getName() + " ( " + 
										election.getBallot().getElectoralDistrict().getZipCode() + " )"+ "</p>" +
				    				"<div class='form-group'>" +
				    						"<label for='name'>Name:</label> <input type='name'" +
				    						"class='form-control' id='name' name='name' placeholder='" + election.getOffice() + "'>" +
				    					"</div>" +
				    					"<div class='form-group'>" +
				    						"<label for='isPartisan'>isPartisan:</label> <input type='checkbox' name='isPartisan'";
				    
				    if(election.getIsPartisan()){ result += " checked>"; }
				    else result += ">";
				    result += 			"</div>" +
				    					"<button type='submit' class='btn btn-default' name='save'>Save Changes</button>" +
				    					"<a type='button' class='btn btn-default' href='ListBallotsQuery'>Cancel</a>" +
				    					"<button type='delete' class='btn btn-default' name='delete'>Delete Election</button>" +
				    					"</form>";
				    
				    result += "<h3>Candidates</h3>";
					List<Candidate> candidates = election.getCandidates();
					
					result += "<form method=post action='UpdateCandidateQuery'><input type='hidden' name='page' value='election'>";
					//result += "";

					for(int j = 0; j < candidates.size(); j++){
						
						Candidate candidate = candidates.get(j);
			
						result += 	"<p style='font-size:20px'>" + candidate.getName() + " ( " + candidate.getPoliticalParty().getName() + " )</p>" +
									"<button type='submit' class='btn btn-default' value='update' name='update" + candidate.getId() + "'>Update</button>" +
									"<button type='submit' class='btn btn-default' value='delete' name='delete" + candidate.getId() + "'>Delete</button>" +
									"<br /><br /><br />";
						}//for
					
					result += "</form>";
					result += "<a type='button' class='btn btn-default' href='createCandidate.html' style='font-size: 16px'><strong>Add Candidate</strong></a>" +
							"</div>" +
						"</body>" +
					"</html>";
				   toClient.println(result);
				   conn.close();
				   toClient.close();			
		    	} else if(delete != null){

		    		if(election.getCandidates().size() > 0 || election.getBallot().getOpenDate().before(new Date()) || election.getBallot().getOpenDate().equals(new Date())){
		    			
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
		    		
		    		Cookie sendCookie = new Cookie("electionDelete", "" + election.getId());
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

			System.out.println(e);
			System.out.println("TEAM4: Update");
			
			try{
				conn.close();
			} catch(Exception f){
				
			}
		    e.printStackTrace(toClient);
		    toClient.close();
		}//try
		
	}//doPost
}
