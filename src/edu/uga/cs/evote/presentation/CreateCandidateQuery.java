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

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.db.DbUtils;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class CreateCandidateQuery extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{

		Connection conn = null;
		PrintWriter toClient = response.getWriter();

		try{
	    
			Class.forName("com.mysql.jdbc.Driver").newInstance();
	    
			response.setContentType("text/html");

		    
		    String redirect = "successOfficer.html"; 
		    
		    String name = request.getParameterValues("name")[0];
		    String partyName = request.getParameterValues("party")[0];
		    
		    if(partyName.equals("")){
		    	partyName = "No Party";
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
	        
	        // obtain a reference to the ObjectModel module      
	        objectLayer = new ObjectLayerImpl();
	        // obtain a reference to Persistence module and connect it to the ObjectModel        
	        persistence = new PersistenceLayerImpl( conn, objectLayer ); 
	        // connect the ObjectModel module to the Persistence module
	        objectLayer = new ObjectLayerImpl(persistence);
	        persistence.setObjectLayer(objectLayer);
	        LogicLayer logicLayer = new LogicLayerImpl(conn);
	        
	        Cookie[] cookies = request.getCookies();
		    Cookie cookie = null;
		    String cookieValue = "";
		    
		    if(cookies.length > 0){
		    	for(int i = 0; i < cookies.length; i++){
		    		cookie = cookies[i];
		    		if(cookie.getName().equals("election")){
		    			cookieValue = cookie.getValue();
		    		}
		    	}
		    }
		    long id = Long.parseLong(cookieValue);
		    Election election = objectLayer.createElection();
		    
		    for(Election e: logicLayer.findAllElections()){
		    	if(e.getId() == id){
		    		election = e;
		    	}
		    }
		    
	        Candidate modelCandidate = objectLayer.createCandidate();
	        modelCandidate.setName(name);
	        modelCandidate.setElection(election);
	        
	        List<Candidate> candidates = objectLayer.findCandidate(modelCandidate);
	        
	        if(candidates.size() > 0){
	        	redirect = "objectAlreadyExists.html";
        		response.sendRedirect(redirect);    
			    conn.close();
			    return;
	        }
	        
	        PoliticalParty modelPoliticalParty = objectLayer.createPoliticalParty();
	        modelPoliticalParty.setName(partyName);
	        
	        List<PoliticalParty> politicalParties = objectLayer.findPoliticalParty(modelPoliticalParty);
	        
	        boolean isPartisan = election.getIsPartisan();
	        
	        toClient.println(" Party name: " + partyName + ", isPartisan: " + isPartisan);
	        
		    if(politicalParties.size() == 0){
		        throw new EVException("No PoliticalParty exists with that name");
		    } else{
		    	if((partyName.equals("No Party") && isPartisan) || (!partyName.equals("No Party") && !isPartisan)){
	        		throw new Exception("no party for partisan election/parties not allowed for nonpartisan election");
	        	}//if
		    }

	        modelCandidate.setPoliticalParty(politicalParties.get(0));
	        
			objectLayer.storeCandidate(modelCandidate);
			
			redirect = "";
    		String n = request.getParameter("page");
    		System.out.println("TEAM4: page is " + n);
    		//if(n.equals("election") || request.getParameter("page") == null) {
    			redirect += "UpdateElectionQuery";
    			System.out.println("TEAM4: " + modelCandidate.getElection().getId());
	    		redirect += "?update" + modelCandidate.getElection().getId() + "=update";
    		//} else 
			response.sendRedirect(redirect);
			conn.close();
			return;

		} catch(Exception e){
	
			//String redirect = new String("invalidDataOfficer.html");
        	//response.sendRedirect(redirect);   
			toClient.println(e);
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
}//CreateDistrictQuery
