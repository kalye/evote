package edu.uga.cs.evote.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
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

public class CreateIssueQuery extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException {
		
		Connection conn = null;
		PrintWriter toClient = response.getWriter();
		
		String question = request.getParameter("issue");
		question = question.trim();
		
		try{
		    
			Class.forName("com.mysql.jdbc.Driver").newInstance();
	    
			response.setContentType("text/html");
		    
		    String redirect = "successOfficer.html";
		    
	        ObjectLayer objectLayer = null;
	        PersistenceLayer persistence = null;
	
	        // get a database connection
	        try {
	            conn = DbUtils.connect();
	        } 
	        catch (Exception seq) {
	            System.err.println( "DeleteTest: Unable to obtain a database connection" );
	        }
	        
	        if( conn == null ) {
	            System.out.println( "DeleteTest: failed to connect to the database" );
	            return;
	        }
	        
			if(question.equals("")) {
				redirect = "emptyFieldsOfficer.html";
        		response.sendRedirect(redirect);
			    conn.close();
			    return;
			}

	        // obtain a reference to the ObjectModel module      
	        objectLayer = new ObjectLayerImpl();
	        // obtain a reference to Persistence module and connect it to the ObjectModel        
	        persistence = new PersistenceLayerImpl( conn, objectLayer ); 
	        // connect the ObjectModel module to the Persistence module
	        objectLayer = new ObjectLayerImpl(persistence);
	        persistence.setObjectLayer(objectLayer);

	        Issue issue = objectLayer.createIssue();

	        //find ballot
		    Cookie[] cookies = request.getCookies();
		    Cookie cookie = null;
	        LogicLayer logicLayer = new LogicLayerImpl(conn);

		    Ballot ballot = objectLayer.createBallot();
		    
		    long id = 0;
		    
		    for(int i = 0; i < cookies.length; i++) {
		    	cookie = cookies[i];
		    	if(cookie.getName().equals("ballot")){
		    		String s = cookie.getValue();
		    		id = Long.parseLong(s);
		    	}
		    }
		    List<Ballot> ballots = logicLayer.findAllBallots();
		    
		    for(Ballot b: ballots){
		    	if(b.getId() == id){
		    		ballot = b;
		    	}
		    }
		    //System.out.println("Here's your ballotId: " + id );
		    //ballot = ballots.get((int)id - 1);
		    //ballot.setId(id);
    		//ballot = objectLayer.findBallot(ballot).get(0);
    		
    		issue.setBallot(ballot);
    		issue.setQuestion(question);
    		
		    List<Issue> issues = objectLayer.findIssue(issue);
        	
        	if(issues.size() != 0){
        		
        		redirect = "objectAlreadyExists.html";
        		response.sendRedirect(redirect);    
			    conn.close();
			    return;
        	}
		    
		    objectLayer.storeIssue(issue);
			
			response.sendRedirect(redirect);
			conn.close();
			return;
		} catch(Exception e){
			String redirect = new String("invalidDataOfficer.html");
        	response.sendRedirect(redirect);   
			toClient.close();
		    try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		    return;
		}//try
		
	}//doPost

}
