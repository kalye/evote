package edu.uga.cs.evote.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
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
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class UpdateVoterRecordQuery extends HttpServlet {

    /**                                                                                                                                                                                    
     *                                                                                                                                                                                     
     */
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
	    
	    List<Candidate> candidates = logicLayer.findAllCandidates();
	    List<Issue> issues = logicLayer.findAllIssues();
	    List<Election> elections = logicLayer.findAllElections();
	    
	    Ballot ballot = objectLayer.createBallot();
	    Voter voter = objectLayer.createVoter();
	    
	    Cookie[] cookies = request.getCookies();
	    Cookie cookie = null;
	    
	    if(cookies.length > 0){
	    	for(int i = 0; i < cookies.length; i++){
	    		cookie = cookies[i];
	    		if(cookie.getName().equals("ballot")){
	    			long id = Long.parseLong(cookie.getValue());
	    			List<Ballot> ballots = logicLayer.findAllBallots();
	    			for(Ballot b: ballots){
	    				if(b.getId() == id){
	    					ballot = b;
	    				}
	    			}
	    		} else if(cookie.getName().equals("voterUser")){
	    			String user = cookie.getValue();
	    			List<Voter> voters = logicLayer.findAllVoters();
	    			for(Voter v: voters){
	    				if(v.getUserName().equals(user)){
	    					voter = v;
	    				}
	    			}
	    			//voter.setUserName(user);
	    			//voter = objectLayer.findVoter(voter).get(0);
	    		}
	    	}
	    }

	   // long id = Long.parseLong(cookieValue);
	   // ballot.setId(id);
	   // ballot = objectLayer.findBallot(ballot).get(0);
	    
	    String vote;
	    
	    for(Election election: elections){
	    	
	    	vote = request.getParameter("" + election.getId());
	    	
	    	if(vote != null){
	    		
	    		for(Candidate candidate: candidates){
		    		
	    			if(candidate.getId() == Long.parseLong(vote)){
			    		
	    				candidate.addVote();
			    		election.addVote();
			    		objectLayer.storeCandidate(candidate);
			    		objectLayer.storeElection(election);
	    			}
	    		}
	    	}
	    }
	    
	    for(Issue issue: issues){
	    	
	    	vote = request.getParameter("" + issue.getId());
	    	
	    	if(vote != null){
		    	if(vote.equals("yes")){
		    		issue.addYesVote();
		    		objectLayer.storeIssue(issue);
		    	} else{
		    		issue.addNoVote();
		    		objectLayer.storeIssue(issue);
		    	}
	    	}//if
	    }
	    
	    Date date = new Date();
	    
	    VoteRecord voteRecord = objectLayer.createVoteRecord(ballot, voter, date);
	    objectLayer.storeVoteRecord(voteRecord);
	    
	    response.setContentType("text/html");

	    String redirect = new String("successVoter.html");
	    response.sendRedirect(redirect);
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

    }

}