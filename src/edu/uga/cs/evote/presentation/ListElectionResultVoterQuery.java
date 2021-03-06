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
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;

public class ListElectionResultVoterQuery extends HttpServlet  {


    /**                                                                                                                   


    */

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
                                        "<h2>Closed Election Results</h2>" +
     "<br /> <br />";

 List<Ballot> ballots = logicLayer.findAllBallots();

 Date currentDate = new Date();

 if(ballots.size() == 0){
     result += "<p>No Closed Ballots found</p>";
 } else{
     for(int i = 0; i < ballots.size(); i++){
	 Ballot ballot = ballots.get(i);
	 if(currentDate.after(ballot.getCloseDate())){
	     result += "<h2> Ballot  " + i + "</h2>" +
		 "<h4> District:  " + ballot.getElectoralDistrict().getName() + "</h4>"
		 + "<h4> Zip Code:  " + ballot.getElectoralDistrict().getZipCode() + "</h4>";
	     List<BallotItem> ballotItems = ballot.getBallotItems();
	     for(int k = 0; k < ballotItems.size(); k++){
		 BallotItem ballotItem = ballotItems.get(k);
		 if(ballotItem.getKind().equalsIgnoreCase("election")){
		     result += "<p> Office:  " + ballotItem.getOffice() + "</p>";
		     List<Candidate> candidates = ((Election)ballotItem).getCandidates();
		     for(int j = 0; j < candidates.size(); j++){
			 Candidate candidate = candidates.get(j);
			 result += "<p> Candidate:  " + candidate.getName() + "</p>" +
			     "<p> Vote Count:  " + candidate.getVoteCount() + "</p>";
		     }
		 }
		 else{ //if the ballotItem is an issue
		     result += "<p> Question:  " + ballotItem.getQuestion() + "</p>" +
			 "<p> Yes Count:  " + ballotItem.getYesCount() + "</p>" +
			 "<p> Vote Count:  " + ballotItem.getVoteCount() + "</p>";
		     if(ballotItem.getYesCount() > (ballotItem.getVoteCount() / 2)){
			 result +=  "<p> ISSUE PASSED </p>";
		     }
		     else{
			 result +=  "<p> ISSUE DID NOT PASS </p>";
		     }
		     
		 }
	     }
	 }
     }
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
	    System.out.println(e.getMessage());
	    
	    
	}//try                                                                                                        \                                                                                                              
	
    }//doPost     
}
