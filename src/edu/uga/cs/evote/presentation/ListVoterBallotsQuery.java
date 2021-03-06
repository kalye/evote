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
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class ListVoterBallotsQuery extends HttpServlet  {


    /**                                                                                                                                                             
     *                                                                                                                                                              
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
	    ObjectLayer objectLayer = new ObjectLayerImpl();
        PersistenceLayer persistence = new PersistenceLayerImpl( conn, objectLayer ); 
        objectLayer = new ObjectLayerImpl(persistence);
        persistence.setObjectLayer(objectLayer);
        
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
                                        "<h2>Ballots</h2>" +
     "<br /> <br />";

 Cookie[] cookies = request.getCookies();
 Cookie cookie = null;
 String cookieValue = "";
 
 if(cookies.length > 0){
 	for(int i = 0; i < cookies.length; i++){
 		cookie = cookies[i];
 		if(cookie.getName().equals("voterUser")){
 			cookieValue = cookie.getValue();
 		}
 	}
 }
 
 Voter voter = objectLayer.createVoter();
 voter.setUserName(cookieValue);
 voter = objectLayer.findVoter(voter).get(0);
 
 //List<Election> elections = logicLayer.findAllElections();
 //List<Ballot> ballots = voter.getElectoralDistrict().getBallots();
 List<Ballot> ballots = logicLayer.findAllBallots();
		 
 Date currentDate = new Date();
 boolean ballotsFound = false;
 
     for(int i = 0; i < ballots.size(); i++){
		 Ballot ballot = ballots.get(i);
		 
		 if(currentDate.before(ballot.getCloseDate()) && (ballot.getElectoralDistrict().getName().equals(voter.getElectoralDistrict().getName()))){
			 
			 ballotsFound = true;
			 result += "<h2> Ballot  " + i + "</h2>" +
					 "<h4> District:  " + ballot.getElectoralDistrict().getName() + "</h4>"
					 + "<h4> Zip Code:  " + ballot.getElectoralDistrict().getZipCode() + "</h4>"
					 + "<p> Open Date:  " + ballot.getOpenDate() + "</p>" 
					 + "<p> Close Date:  " + ballot.getCloseDate() + "</p>" +
					 "<form method=post action='ListVoterBallotItemsQuery'>";
		 
		
		     result += "<button type='submit' class='btn btn-default' style='font-size: 16px' name='view" + ballot.getId() + "'><strong>VIEW BALLOT ITEMS</strong></button>";
		     
		     boolean alreadyVoted = false;
		     List<VoteRecord> records = logicLayer.findAllVoteRecords();
		     for(VoteRecord record: records){
		    	 
		    	 if(record.getVoter().getId() == voter.getId() && record.getBallot().getId() == ballot.getId()){
		    		 alreadyVoted = true;
		    	 }
		     }
		     //VoteRecord voteRecord = objectLayer.createVoteRecord();
		     //voteRecord.setBallot(ballot);
		    // voteRecord.setVoter(voter);
		    // List<VoteRecord> records = objectLayer.findVoteRecord(voteRecord);
		     
		     if(currentDate.after(ballot.getOpenDate()) && currentDate.before(ballot.getCloseDate()) && !alreadyVoted){
			     result += "<button type='submit' class='btn btn-default' style='font-size: 16px' name='vote" + ballot.getId() + "'><strong>VOTE</strong></button>" +
			     "</form>";
		     }
		 }//if
     }
     
     if(!ballotsFound){
    	 
    	 result += "<p>No Ballots Available</p>";
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
	    
	    
	}//try                                                                                                                                                  
	
    }//doPost                                                                                                                     
    
}
