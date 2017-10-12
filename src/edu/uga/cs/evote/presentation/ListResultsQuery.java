package edu.uga.cs.evote.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
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
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class ListResultsQuery extends HttpServlet  {

	private static final long serialVersionUID = 4746837609081425474L;

    public void doGet( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{

        Connection  conn = null;

        try{
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
                   "<nav class=\"navbar navbar-default\">" +
                "<div class=\"container-fluid\">" +
                        "<div class=\"navbar-header\">" +
                                "<a class=\"navbar-brand\" href=\"index.html\">eVote - Voter</a>" +
                   "</div>" +

  "<ul class=\"nav navbar-nav\">" +
                "<li><a href=\"ListVoterBallotsQuery\">View/Vote On Ballots</a></li>" +
                   "<li><a href=\"ListResultsQuery\">View Closed Ballot Results</a></li>" +
                                "<li><a href=\"ViewVoterProfileQuery\">Update Profile</a></li>" +
                                "<li><a href=\"successAnon.html\">Log Out</a>" +
                          "</ul>" +
                "</div>" +
                   "</nav>" ;

 result += "<div class='container'>" +
                                        "<h2>Closed Ballot Results</h2>" +
     "<br /> <br />";

 
 ObjectLayer objectLayer = new ObjectLayerImpl();
 PersistenceLayer persistence = new PersistenceLayerImpl( conn, objectLayer ); 
 objectLayer = new ObjectLayerImpl(persistence);
 persistence.setObjectLayer(objectLayer);
 
 List<Ballot> ballots = logicLayer.findAllBallots();

 Date currentDate = new Date();
 boolean foundBallots = false;
 
     for(Ballot ballot: ballots){
		 
		 if(ballot.getCloseDate().before(currentDate) && ballot.getIsApproved()){
			 
			 foundBallots = true;
			 result += "<div class='container' style='border-style: groove'>" +
			     "<h4> District:  " + ballot.getElectoralDistrict().getName() + "</h4>"
			     + "<h4> Zip Code:  " + ballot.getElectoralDistrict().getZipCode() + "</h4>"
			     + "<p> Open Date:  " + ballot.getOpenDate() + "</p>" 
			     + "<p> Close Date:  " + ballot.getCloseDate() + "</p>" +
			     "<br/>" +
				"<h4>Issues</h4>";
				
			 	List<Issue> issues = logicLayer.findIssues(ballot);
				if(issues.size() == 0){
					result += "<p>No Issues</p>";
				} else{
	
					for(Issue issue: issues){

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
									"<p>Results: " + resultString + "</p><br/>";
					}//for
				}//if
				
				result += "<h4>Elections</h4>";
				
				List<Election> elections = logicLayer.findElections(ballot);
				if(elections.size() == 0){
					result += "<p>No Elections</p>";
				} else{
	
					for(int j = 0; j < elections.size(); j++){
						Election election = elections.get(j);
		
						result += 	"<p style='font-size:16px'>" + election.getOffice() + " ( ";
						
						List<Candidate> candidates = election.getCandidates();
						for(Candidate candidate: candidates){
							result += candidate.getName() + ", ";
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
							resultString += "<p>" + winners.get(0).getName() + " has won with " + max + " votes.</p>";
						} else{
							
							resultString += "<p>";
							for(int l = 0; l < winners.size(); l++){
    							resultString += winners.get(l).getName() + " ";
    						}
							resultString += "have tied with " + max + " vote(s) each.</p>";
						}
						
						result += "<p>Results: </p>" + resultString + "<br/>";
					}//for
				}//if 
			 }//if
		 
		 	result += "</div>";
		 }//for
 
     if(!foundBallots){
    	  result += "<p>No Results Available</p>";
     }
 
     result += "</div></body></html>";
     toClient.println(result);
     conn.close();
     toClient.close();
 
 
 
	} catch(Exception e){
	    
	    try{
		conn.close();
	    } catch(Exception f){
		
	    }
	    System.out.println(e);
	    
	    
	}//try                                                                                                        \                                                                                                              
	
    }//doPost     
}
