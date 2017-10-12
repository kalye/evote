package edu.uga.cs.evote.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uga.cs.evote.db.DbUtils;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;

public class UpdateBallotQuery extends HttpServlet {

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

		    String result = "<!DOCTYPE html>" +
    				"<html lang='en'>" +
    					"<head>" +
    						"<title>Update Ballot</title>" +
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

		    List<Ballot> ballots = logicLayer.findAllBallots();
		    
		    String update;
		    String delete;
		    String view;
		    String results;
		    
		    
		    for(Ballot ballot: ballots){
		    	
		    	update = request.getParameter("update" + ballot.getId());
		    	delete = request.getParameter("delete" + ballot.getId());
		    	view = request.getParameter("view" + ballot.getId());
		    	results = request.getParameter("results" + ballot.getId());
		    	
		    	if(update != null){
		    		
		    		for (Cookie c : request.getCookies()) {
		    		    
						if(!(c.getName().equals("voterUser") || c.getName().equals("electionOfficerUser"))){
							c.setValue("");
							c.setMaxAge(0);
							c.setPath("/");
						}//if
		    		    response.addCookie(c);
		    		}
		    		
		    		Cookie cookie = new Cookie("ballot", ballot.getId() + "");
				    cookie.setMaxAge(60*60);
				    response.addCookie(cookie);
				    
				    
				    result += "<div class='container'>" +
				    				"<h3>Update Ballot</h3>" +
				    				"<form method=post action='EditBallotQuery'>" +
				    					"<div class='form-group'>" +
				    						"<label for='district'>Electoral District:</label> <input type='text'" +
				    						"class='form-control' id='district' name='district' placeholder='" + ballot.getElectoralDistrict().getName() + "'>" +
				    					"</div>" +
				    					"<div class='form-group'>" +
				    						"<label for='open'>Open Date:</label> <input type='date' name='openDate' value='" + ballot.getOpenDate() + "'>" +
				    					"</div>" + 
				    					"<div class='form-group'>" +
			    							"<label for='close'>Close Date:</label> <input type='date' name='closeDate' value='" + ballot.getCloseDate() + "'>" +
										"</div>" + 
				    					"<button type='submit' class='btn btn-default' name='save'>Save Changes</button>" +
				    					"<a type='button' class='btn btn-default' href='ListBallotsQuery'>Cancel</a>" +
				    					"<button type='delete' class='btn btn-default' name='delete'>Delete Ballot</button>" +
				    				"</form><br/>" +
				    				"<h3>Update Issues</h3>";
				    				
				    				boolean issueFound = false;
				    				
				    				List<Issue> issues = logicLayer.findAllIssues();
				    	
				    				result += "<form method=post action='UpdateIssueQuery'>";
				    				for(Issue issue: issues){
				    						
				    					if(issue.getBallot().getId() == ballot.getId()){
				    		
					    					result += 	"<p style='font-size:20px'>" + issue.getQuestion() + "</p>" +
					    									"<button type='submit' class='btn btn-default' value='update' name='update" + issue.getId() + "'>Update</button>" +
					    									"<button type='submit' class='btn btn-default' value='delete' name='delete" + issue.getId() + "'>Delete</button>" +
					    									"<br /><br /><br />";
					    					issueFound = true;
				    					}
				    				}//for
				    				result += "</form>";
				    				
				    				if(!issueFound){
				    					result += "<p>No Issues</p>";
				    				}
				    				
				    				result += "<a type='button' class='btn btn-default' href='createIssue.html' style='font-size: 16px'><strong>Add Issue</strong></a>";
				
				    				result += "<h3>Update Elections</h3>";
				    				//System.out.println(ballot.getId());
				    				
				    				List<Election> elections = logicLayer.findAllElections();
				    				
				    				boolean electionFound = false;
				    				
				    					result += "<form method=post action='UpdateElectionQuery'>";
				    					for(Election election: elections){
				    		
				    						if(election.getBallot().getId() == ballot.getId()){
					    						result += 	"<p style='font-size:20px'>" + election.getOffice() + " ( ";
					    						
					    						List<Candidate> candidates = election.getCandidates();
					    						for(Candidate candidate: candidates){
					    							result += candidate.getName() + ", ";
					    						}
					    						
					    						
					    						
					    						result += 	")</p>" +
					    									"<button type='submit' class='btn btn-default' value='update' name='update" + election.getId() + "'>Update</button>" +
					    									"<button type='submit' class='btn btn-default' value='delete' name='delete" + election.getId() + "'>Delete</button>" +
					    									"<br /><br /><br />";
					    						
					    						electionFound = true;
				    						}
				    					}//for
				    					result += "</form>";

				    					
				    					if(!electionFound){
				    						result += "<p>No Elections</p>";
				    					}
				    				
				    				result += "<a type='button' class='btn btn-default' href='createElection.html' style='font-size: 16px'><strong>Add Election</strong></a>" +
				    						"</div>" +
				    					"</body>" +
				    				"</html>";
				   
					toClient.println(result);
					conn.close();
					toClient.close();		
		    	} else if(delete != null){
				    
		    		for (Cookie c : request.getCookies()) {
		    		    
						if(!(c.getName().equals("voterUser") || c.getName().equals("electionOfficerUser"))){
							c.setValue("");
							c.setMaxAge(0);
							c.setPath("/");
						}//if
		    		    response.addCookie(c);
		    		}
		    		
		    		Cookie sendCookie = new Cookie("ballotDelete", "" + ballot.getId());
		    		sendCookie.setMaxAge(60*60);
				    response.addCookie(sendCookie);
				    String redirect = new String("AreYouSure.html");
				    response.sendRedirect(redirect);
				    conn.close();
				    toClient.close();
				    return;
		    	} else if(view != null){
		    		
		    		 result += "<div class='container'>" +
				    				"<h3>View Ballot</h3>" +
				    					"<p>Electoral District: " + ballot.getElectoralDistrict().getName() + "</p>" +
				    					"<p>Open Date: " + ballot.getOpenDate() + "</p>" +
			    						"<p>Close Date: " + ballot.getCloseDate() + "</p>" +
				    					"<br/>" +
				    				"<h3>Issues</h3>";
				    				List<Issue> issues = logicLayer.findIssues(ballot);
				    				if(issues.size() == 0){
				    					result += "<p>No Issues</p>";
				    				} else{
				    	
				    					for(int j = 0; j < issues.size(); j++){
				    						
				    						Issue issue = issues.get(j);
				    		
				    						result += 	"<p>" + issue.getQuestion() + "</p>";
				    					}//for
				    				}//if
				    				
				    				result += "<h3>Elections</h3>";
				    				
				    				List<Election> elections = logicLayer.findElections(ballot);
				    				if(elections.size() == 0){
				    					result += "<p>No Elections</p>";
				    				} else{
				    	
				    					for(int j = 0; j < elections.size(); j++){
				    						Election election = elections.get(j);
				    		
				    						result += 	"<p>" + election.getOffice() + " ( ";
				    						
				    						List<Candidate> candidates = election.getCandidates();
				    						for(int k = 0; k < candidates.size(); k++){
				    							result += candidates.get(k).getName() + ", ";
				    						}
				    						
				    						result += 	")</p>";
				    					}//for
				    				}//if 
				    				
				    				result += "</div>" +
				    					"</body>" +
				    				"</html>";
				   
					toClient.println(result);
					conn.close();
					toClient.close();		
		    	} else if(results != null){
		    		
		    		result += "<div class='container'>" +
		    				"<h3>Ballot Results</h3>" +
		    					"<p>Electoral District: " + ballot.getElectoralDistrict().getName() + "</p>" +
		    					"<p>Open Date: " + ballot.getOpenDate() + "</p>" +
	    						"<p>Close Date: " + ballot.getCloseDate() + "</p>" +
		    					"<br/>" +
		    				"<h3>Issues</h3>";
		    				List<Issue> issues = logicLayer.findIssues(ballot);
		    				if(issues.size() == 0){
		    					result += "<p>No Issues</p>";
		    				} else{
		    	
		    					for(int j = 0; j < issues.size(); j++){
		    						Issue issue = issues.get(j);
		    						int yesCount = issue.getYesCount();
		    						int noCount = issue.getNoCount();
		    						String resultString;
		    						
		    						if(yesCount > noCount){
		    							
		    							resultString = 	"Issue passes with ";
		    						} else{
		    							
		    							resultString = "Issue fails with ";
		    						}
		    						
		    						resultString += yesCount + " votes in favor and " +
													noCount + " votes against.";
		    						result += 	"<p style='font-size:16px'>" + issue.getQuestion() + "</p>" +
		    									"<p>Results: " + resultString + "</p></br>";
		    					}//for
		    				}//if
		    				
		    				result += "<h3>Elections</h3>";
		    				
		    				List<Election> elections = logicLayer.findElections(ballot);
		    				if(elections.size() == 0){
		    					result += "<p>No Elections</p>";
		    				} else{
		    	
		    					for(int j = 0; j < elections.size(); j++){
		    						Election election = elections.get(j);
		    		
		    						result += 	"<p style='font-size:16px'>" + election.getOffice() + " ( ";
		    						
		    						List<Candidate> candidates = election.getCandidates();
		    						for(int k = 0; k < candidates.size(); k++){
		    							result += candidates.get(k).getName() + ", ";
		    						}
		    						
		    						result += 	")</p>";
		    						
		    						String resultString = "";
		    						int max = 0;
		    						List<Candidate> winners = new ArrayList<Candidate>();
		    						
		    						for(Candidate candidate: candidates){
		    							
		    							if(candidate.getVoteCount() > max){
		    								max = candidate.getVoteCount();
		    								winners = new ArrayList<Candidate>();
		    								winners.add(candidate);
		    							} else if(candidate.getVoteCount() == max){
		    								winners.add(candidate);
		    							}
		    							resultString += "<p>" + candidate.getName() + ": " + candidate.getVoteCount() + "</p>";
		    						}

		    						if(winners.size() == 1){
		    							resultString += "<p>" + winners.get(0).getName() + " has won with " + max + " vote(s).</p>";
		    						} else{
		    							
		    							resultString += "<p>";
		    							for(Candidate winner: winners){
			    							resultString += winner.getName() + ", ";
			    						}
		    							resultString += "have tied with " + max + " votes each.</p>";
		    						}
		    						
		    						result += "<p>Results: </p>" + resultString + "<br/>";
		    					}//for
		    				}//if 
		    				
		    				if(!ballot.getIsApproved()){
		    					result += "<form method=post action='ApproveResultsQuery'><button type='submit' class='btn btn-default'>Approve These Results</button></form>";
		    				}
		    					
		    				result += "</div>" +
		    					"</body>" +
		    				"</html>";
		    				
		    				for (Cookie c : request.getCookies()) {
				    		    
								if(!(c.getName().equals("voterUser") || c.getName().equals("electionOfficerUser"))){
									c.setValue("");
									c.setMaxAge(0);
									c.setPath("/");
								}//if
				    		    response.addCookie(c);
				    		}
				    		
				    		Cookie cookie = new Cookie("ballot", ballot.getId() + "");
						    cookie.setMaxAge(60*60);
						    response.addCookie(cookie);
						    
		    				toClient.println(result);
		    				toClient.close();
		    				conn.close();
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
