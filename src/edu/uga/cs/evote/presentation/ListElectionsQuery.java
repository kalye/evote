package edu.uga.cs.evote.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uga.cs.evote.db.DbUtils;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;

public class ListElectionsQuery extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void doPost( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{

		Connection  conn = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		    
			response.setContentType("text/html");

	        
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
	        
		    PrintWriter toClient = response.getWriter();

		    response.setContentType("text/html");

		    String result = "<!DOCTYPE html>" +
		    				"<html lang='en'>" +
		    					"<head>" +
		    						"<title>Edit Profile</title>" +
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
		    								"<li><a href='ViewProfileQuery'>Update Profile</a></li>" +
		    								"<li><a href='successAnon.html'>Log Out</a>" +
		    					"</ul></div></nav>";
		    result += "<div class='container'>" +
		    				"<h2>Elections</h2>" +
		    				"<br /> <br />";
		    
		    result += "<div class='container'><form method=get action='ListElectionsQuery'><div class='form-group'>" 
		    		+ "<label for='name'>Name:</label> <input type='text' class='form-control' id='search' name='search' placeholder='Search All Elections'></div>"
		    + "<button type='submit' class='btn btn-default'><strong>Go</strong></button></form></div>";
		    
		    List<Election> elections = logicLayer.findAllElections();
		    boolean foundMatch = false;
		    
		    String search = request.getParameter("search");
		    if(search.equals("Search All Elections")){
		    	search = "";
		    }
		    
		    result += "<form method=post action='UpdateElectionQuery'>";
		    
		    for(Election election: elections){
		    	
		    	if(election.getOffice().toLowerCase().trim().contains(search.toLowerCase().trim())){
			         
			    	foundMatch = true;
			    	
			    	result += 	"<p style='font-size:20px'>" + election.getOffice() + " ( ";
					
					List<Candidate> candidates = election.getCandidates();
					for(Candidate candidate: candidates){
						result += candidate.getName() + ", ";
					}
					
					
					
					result += 	")</p>" +
								"<p>District: " + election.getBallot().getElectoralDistrict().getName() + " ( " + 
								election.getBallot().getElectoralDistrict().getZipCode() + " )"+ "</p>" +
								"<button type='submit' class='btn btn-default' value='update' name='update" + election.getId() + "'>Update</button>" +
								"<button type='submit' class='btn btn-default' value='delete' name='delete" + election.getId() + "'>Delete</button>" +
								"<br /><br /><br />";
			    }
		    }//for
		    
		    if(!foundMatch){
		    	
		    	result += "<p>No Elections Found</p>";
		    }
		    
		    result += "</form>";
		    result += "</div></body></html>";
			toClient.println(result);
			conn.close();
			toClient.close();			
				
					    
		} catch(Exception e){

			try{
				conn.close();
			} catch(Exception f){
				
			}
		    System.out.println(e.getMessage());
		}//try
		
	}//doPost
}
