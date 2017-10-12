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
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class CreateElectionQuery extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException {
		
		Connection conn = null;
		PrintWriter toClient = response.getWriter();
		
		try{
		    
			Class.forName("com.mysql.jdbc.Driver").newInstance();
	    
			response.setContentType("text/html");
		    
		    String redirect = "successOfficer.html";
		    
			String question = request.getParameter("office");

			boolean isPartisan = false;
			if(request.getParameter("isPartisan") != null) {
				isPartisan = true;
			}
		    
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

	        //find ballot
		    Cookie[] cookies = request.getCookies();
		    Cookie cookie = null;
		    
		    Ballot ballot = null;
		    long id = 0;
	        LogicLayer logicLayer = new LogicLayerImpl(conn);

		    
		    for(int i = 0; i < cookies.length; i++) {
		    	cookie = cookies[i];
		    	if(cookie.getName().equals("ballot")){
		    		String s = cookie.getValue();
		    		id = Long.parseLong(s);
		    		System.out.println("Id: " + id);
		    		List<Ballot> ballots = logicLayer.findAllBallots();
		    		
		    		for(Ballot b: ballots){
		    			if(b.getId() == id){
		    				ballot = b;
		    			}
		    		}
		    		//ballot = ballots.get((int)id - 1);
		    		//ballot.setId(id);
		    		//ballot = objectLayer.findBallot(ballot).get(0);
		    	}
		    }
			
		    Election election = objectLayer.createElection();
	        election.setOffice(question);
	        election.setBallot(ballot);
	        
	        //System.out.println("Current Ballot: " + ballot.getId());
        	List<Election> elections = objectLayer.findElection(election);
        	
        	if(elections.size() != 0){
        		redirect = "objectAlreadyExists.html";
        		response.sendRedirect(redirect);    
			    conn.close();
			    return;
        	}
        	
        	election.setIsPartisan(isPartisan);
        		
        	objectLayer.storeElection(election);
			
			response.sendRedirect(redirect);
			conn.close();
			return;
		} catch(Exception e){
			String redirect = new String("invalidDataOfficer.html");
			toClient.println(e);
			//System.out.println(e);
        	response.sendRedirect(redirect);   
			toClient.close();
		    try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    return;
		}//try
		
	}//doPost

}
