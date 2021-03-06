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
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;

public class ListPartiesQuery extends HttpServlet {

	/**
	 * 
	 */
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
	    											"<li><a href='searchCandidates.html'>Candidates</a></li>" +
	    											"<li><a href='searchParties.html'>Political Parties</a></li>" +
		    										"</ul></li>" +
		    								"<li><a href='ViewProfileQuery'>Update Profile</a></li>" +
		    								"<li><a href='successAnon.html'>Log Out</a>" +
		    					"</ul></div></nav>";
		    result += "<div class='container'>" +
		    				"<h2>Political Parties</h2>" +
		    				"<br /> <br />";
		    
		    result += "<div class='container'><form method=get action='ListPartiesQuery'><div class='form-group'>" 
		    		+ "<label for='name'>Name:</label> <input type='text' class='form-control' id='search' name='search' placeholder='Search All Parties'></div>"
		    + "<button type='submit' class='btn btn-default'><strong>Go</strong></button></form></div>";
		    
		    List<PoliticalParty> politicalParties = logicLayer.findAllPoliticalParties();
		    boolean foundMatch = false;
		    
		    String search = request.getParameter("search");
		    if(search.equals("Search All Parties")){
		    	search = "";
		    }
		    
		    result += "<form method=post action='UpdatePartyQuery'>";
		    
		    for(PoliticalParty party: politicalParties){
		    
		    	if(party.getName().toLowerCase().trim().contains(search.toLowerCase().trim())){
			         
			    	foundMatch = true;
			    	
			    	if(!party.getName().equals("No Party")){
			    		result += "<p style='font-size:20px'><strong>" + party.getName() + "</strong></p>" +
			    				"<button type='submit' class='btn btn-default' value='update' name='update" + party.getId() + "'>Update</button>" +
			    				"<button type='submit' class='btn btn-default' value='delete' name='delete" + party.getId() + "'>Delete</button>" +
			    				"<br /><br /><br />";
		    		}//if
			    }//if
		    }//for
		    
		    if(!foundMatch){
		    	
		    	result += "<p>No Political Parties Found</p>";
		    }
		    
		    result += "</form>";
		    
		    result += "<a type='button' class='btn btn-default' href='createParty.html' style='font-size: 16px'><strong>Create New Political Party</strong></a>";
			result += "</div></body></html>";
			toClient.println(result);
			conn.close();
			toClient.close();			
		    
		    
		    
		    
		    
		    
		    
		    /*if(politicalParties.size() == 0){
		    	result += "<p>No Political Parties Found</p>";
		    } else{
		    	
		    	result += "<form method=post action='UpdatePartyQuery'>";
		    	for(int i = 0; i < politicalParties.size(); i++){

		    		PoliticalParty politicalParty = politicalParties.get(i);
		    		if(!politicalParty.getName().equals("No Party")){
			    		result += "<p style='font-size:20px'><strong>" + politicalParty.getName() + "</strong></p>" +
			    				"<button type='submit' class='btn btn-default' value='update' name='update" + i + "'>Update</button>" +
			    				"<button type='submit' class='btn btn-default' value='delete' name='delete" + i + "'>Delete</button>" +
			    				"<br /><br /><br />";
		    		}
		    	}
		    	result += "</form>";
		    }
		    
		    result += "<a type='button' class='btn btn-default' href='createParty.html' style='font-size: 16px'><strong>Create New Political Party</strong></a>";
			result += "</div></body></html>";
			toClient.println(result);
			conn.close();
			toClient.close();	*/		
				
					    
		} catch(Exception e){

			try{
				conn.close();
			} catch(Exception f){
				
			}
		    System.out.println(e);
		    e.printStackTrace();
		}//try
		
	}//doPost
}
