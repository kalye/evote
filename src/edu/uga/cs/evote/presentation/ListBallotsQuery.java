package edu.uga.cs.evote.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uga.cs.evote.db.DbUtils;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;

public class ListBallotsQuery extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void doGet( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{

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
		    				"<h2>Ballots</h2>" +
		    				"<br /> <br />";
		    
		    List<Ballot> ballots = logicLayer.findAllBallots();
		    if(ballots.size() == 0){
		    	result += "<p>No Ballots Found</p>";
		    } else{
		    	
		    	result += "<form method=post action='UpdateBallotQuery'>";
		    	for(int i = 0; i < ballots.size(); i++){
		    		
		    		Ballot ballot = ballots.get(i);
		    		
		    		result += "<p style='font-size:20px'><strong>" + ballot.getElectoralDistrict().getName();
		    		
		    		Date closeDate = ballot.getCloseDate();
		    		if(closeDate.before(new Date())){
		    			
		    			result += "</strong> ( Closed )";
		    			
		    			if(!ballot.getIsApproved()){
		    				result += "</p><button type='submit' class='btn btn-default' value='results' name='results" + ballot.getId() + "'>View/Approve Results</button>";
		    			} else{
		    				result += "( Approved )</p><button type='submit' class='btn btn-default' value='view' name='results" + ballot.getId() + "'>View Results</button>";
		    			}
		    		} else{
		    			
		    			Date openDate = ballot.getOpenDate();
		    			
		    			if(openDate.before(new Date()) || openDate.equals(new Date())){
		    				
		    				result += "</strong> ( Started = " + openDate + "; Closes " + closeDate + " )</p>";
			    			result +="<button type='submit' class='btn btn-default' value='view' name='view" + ballot.getId() + "'>View</button>";
		    			} else{
		    			result += "</strong> ( Starts = " + openDate + "; Closes " + closeDate + " )</p>";
		    			result +="<button type='submit' class='btn btn-default' value='update' name='update" + ballot.getId() + "'>Update</button>" +
			    				"<button type='submit' class='btn btn-default' value='delete' name='delete" + ballot.getId() + "'>Delete</button>" +
			    				"<button type='submit' class='btn btn-default' value='view' name='view" + ballot.getId() + "'>View</button>";
		    			}//if
		    		}//if
		    		 
		    		result += "<br /><br /><br />";
		    	}
		    	result += "</form>";
		    }
		    
		    result += "<a type='button' class='btn btn-default' href='createBallot.html' style='font-size: 16px'><strong>Create New Ballot</strong></a>";
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
