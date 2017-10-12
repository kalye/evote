package edu.uga.cs.evote.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uga.cs.evote.db.DbUtils;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;


public class AreYouSureQuery extends HttpServlet {

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
	         
		    String cookieValue = "";
	    	long id = 0;
		    Cookie[] cookies = request.getCookies();
		    
		    ObjectLayer objectLayer = new ObjectLayerImpl();
	        // obtain a reference to Persistence module and connect it to the ObjectModel        
	        PersistenceLayer persistence = new PersistenceLayerImpl( conn, objectLayer ); 
	        // connect the ObjectModel module to the Persistence module
	        objectLayer = new ObjectLayerImpl(persistence);
	        persistence.setObjectLayer(objectLayer);
	        
	        LogicLayer logicLayer = new LogicLayerImpl(conn);
	        	
	        //toClient.println("<p> Cookies length = " + cookies.length + "</p>");

		    if(cookies.length > 0){
		    	
		    	for(Cookie cookie: cookies){
		    		
		    		//toClient.println("<p> Cookie name: " + cookie.getName() + "</p>");
		    		
		    		if(cookie.getName().equals("districtDelete")){
		    			
		    			//toClient.println("<p>cookie found!! Cookie value = " + cookie.getValue() + "</p>");
		    			cookieValue = cookie.getValue();
		    			
		    			List<ElectoralDistrict> electoralDistricts = logicLayer.findAllElectoralDistricts();
		    			
		    			id = Long.parseLong(cookieValue);
		    				
		    			//toClient.println("<p>found district</p>");
			    		String noButton = request.getParameter("no");
			    		if(noButton != null){
			    			cookie.setMaxAge(0);
			    			RequestDispatcher rd = request.getRequestDispatcher("searchDistricts.html");
			    			rd.forward(request, response);
			    			conn.close();
			    			toClient.close();
			    			return;
			    		}
			    		
			    		for(ElectoralDistrict district: electoralDistricts){
		    				
			    			if(district.getId() == id){
		    					
		    					objectLayer.deleteElectoralDistrict(district);
					    		String redirect = new String("successOfficer.html");
					    		response.sendRedirect(redirect);
					    		conn.close();
					    		toClient.close();
					    		return;
		    				}
		    			}
			    		
		    		} else if(cookie.getName().equals("candidateDelete")){ 
		    			
		    			//toClient.println("<p>cookie found!! Cookie value = " + cookie.getValue() + "</p>");
		    			cookieValue = cookie.getValue();
		    			
		    			id = Long.parseLong(cookieValue);
		    			
		    			String noButton = request.getParameter("no");
		    			if(noButton != null){

		    				RequestDispatcher rd = request.getRequestDispatcher("ListBallotsQuery");
		    			    rd.forward(request, response);
		    			    conn.close();
		    			    toClient.close();
		    			    return;
		    			}
		    			
		    			Candidate candidate = objectLayer.createCandidate();
		    			candidate.setId(id);
		    			candidate = objectLayer.findCandidate(candidate).get(0);
		    			
		    			String redirect = "";
		    			if(request.getParameter("page") == null) {
		    				redirect += "UpdateElectionQuery";
		    				redirect += "?update" + candidate.getElection().getId() + "=update";
		    			} else if(request.getParameter("page").equals("list")){
		    				redirect += "ListCandidatesQuery";
		    				redirect += "?search=" + request.getParameter("search");
		    			}
		    			
		    			objectLayer.deleteCandidate(candidate);
		    			//String redirect = new String("successOfficer.html");
		    			response.sendRedirect(redirect);
		    			conn.close();
		    			toClient.close();
		    			return;
		    		} else if(cookie.getName().equals("partyDelete")){

		    			cookieValue = cookie.getValue();
		    			
		    			List<PoliticalParty> parties = logicLayer.findAllPoliticalParties();
		    			id = Long.parseLong(cookieValue);

		    			toClient.println(parties.size());
		    			
		    			String noButton = request.getParameter("no");
		    			if(noButton != null){

		    				RequestDispatcher rd = request.getRequestDispatcher("searchParties.html");
		    			    rd.forward(request, response);
		    			    conn.close();
		    			    toClient.close();
		    			    return;
		    			}
		    				
		    			for(PoliticalParty party: parties){
		    				if(party.getId() == id){
		    					objectLayer.deletePoliticalParty(party);
				    			String redirect = new String("successOfficer.html");
				    			response.sendRedirect(redirect);
				    			conn.close();
				    			toClient.close();
				    			return;
		    				}
		    			}	
		    		} else if(cookie.getName().equals("ballotDelete")){ 
		    			
		    			//toClient.println("<p>cookie found!! Cookie value = " + cookie.getValue() + "</p>");
		    			id = Long.parseLong(cookie.getValue());
		    			Ballot ballot = objectLayer.createBallot();
		    			
		    			List<Ballot> ballots = logicLayer.findAllBallots();
		    			for(Ballot currentBallot: ballots){
		    				
		    				if(currentBallot.getId() == id){
		    					
		    					ballot = currentBallot;
		    				}
		    			}
		    			/*Ballot ballot = objectLayer.createBallot();
		    			ballot.setId(id);
		    			
		    			ballot = objectLayer.findBallot(ballot).get(0);*/
		    					
		    			//toClient.println("<p>found district</p>");
		    			String noButton = request.getParameter("no");
		    			if(noButton != null){
		    				RequestDispatcher rd = request.getRequestDispatcher("ListBallotsQuery");
		    			    rd.forward(request, response);
		    			    conn.close();
		    			    toClient.close();
		    			    return;
		    			}//if
		    			
		    			List<Issue> issues = logicLayer.findIssues(ballot);
		    			List<Election> elections = logicLayer.findElections(ballot);
		    			
		    			for(Issue issue: issues){
		    				objectLayer.deleteIssue(issue);
		    			}
		    			
		    			for(Election election: elections){
		    				
		    				for(Candidate candidate: election.getCandidates()){
		    					
		    					objectLayer.deleteCandidate(candidate);
		    				}
		    				
		    				objectLayer.deleteElection(election);
		    			}
		    			
		    			objectLayer.deleteBallot(ballot);
		    			String redirect = new String("successOfficer.html");
		    			response.sendRedirect(redirect);
		    			conn.close();
		    			toClient.close();
		    			return;
		    		} else if(cookie.getName().equals("issueDelete")){ 
		    			
		    			//toClient.println("<p>cookie found!! Cookie value = " + cookie.getValue() + "</p>");
		    			id = Long.parseLong(cookie.getValue());
		    			Issue issue = objectLayer.createIssue();
		    			
		    			List<Issue> issues = logicLayer.findAllIssues();
		    			for(Issue iss: issues){
		    				if(iss.getId() == id){
		    					issue = iss;
		    				}
		    			}
		    			
		    					
		    			//toClient.println("<p>found district</p>");
		    			String noButton = request.getParameter("no");
		    			if(noButton != null){
		    				RequestDispatcher rd = request.getRequestDispatcher("ListBallotsQuery");
		    			    rd.forward(request, response);
		    			    conn.close();
		    			    toClient.close();
		    			    return;
		    			}//if
		    			
		    			objectLayer.deleteIssue(issue);
		    			String redirect = new String("successOfficer.html");
		    			response.sendRedirect(redirect);
		    			conn.close();
		    			toClient.close();
		    			return;
		    		} else if(cookie.getName().equals("electionDelete")){ 
		    			
		    			//toClient.println("<p>cookie found!! Cookie value = " + cookie.getValue() + "</p>");
		    			id = Long.parseLong(cookie.getValue());
		    			Election election = objectLayer.createElection();
		    			
		    			List<Election> elections = logicLayer.findAllElections();
		    			System.out.println("TEAM4: " + elections.size());
		    			
		    			for(Election e: elections){
		    				if(e.getId() == id){
		    					election = e;
		    				}
		    			}
		    					
		    			//toClient.println("<p>found district</p>");
		    			String noButton = request.getParameter("no");
		    			if(noButton != null){
		    				RequestDispatcher rd = request.getRequestDispatcher("ListBallotsQuery");
		    			    rd.forward(request, response);
		    			    conn.close();
		    			    toClient.close();
		    			    return;
		    			}//if
		    			System.out.println("TEAM4: About to delete...");
		    			objectLayer.deleteElection(election);
		    			String redirect = new String("successOfficer.html");
		    			response.sendRedirect(redirect);
		    			conn.close();
		    			toClient.close();
		    			return;
		    		}
		    	}
		    }
		} catch(Exception e){

			try {
				conn.close();
			} catch (SQLException e1) {

			}
		    toClient.println(e);
		    toClient.close();
		}//try
	}//doPost
	
	public void doGet(HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{

		Connection  conn = null;
        PrintWriter toClient = response.getWriter();
        
        try {
        	Class.forName("com.mysql.jdbc.Driver").newInstance();
		    
			response.setContentType("text/html");
		String result = "<!DOCTYPE html>"
				+ "<html lang='en'>"
				+	"<head>"
				+ "<title>eVote System</title>"
				+ "<meta charset='utf-8'>"
				+ "<meta name='viewport' content='width=device-width, initial-scale=1'>"
				+ "<link rel='stylesheet' href='http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'>"
				+ "<script src='https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js'></script>"
				+ "<script src='http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js'></script>"
				+ "</head>"
				+ "<body>"
	+ "<nav class='navbar navbar-default'> <div class='container-fluid'> <div class='navbar-header'>"
	+ "<a class='navbar-brand' href='ElectionsOfficerHome.html'>eVote - Elections Officer</a></div>"
	+ "<ul class='nav navbar-nav'><li class='dropdown'><a class='dropdown-toggle' data-toggle='dropdown' href=''#''>View/Update <span class='caret'></span></a><ul class='dropdown-menu'>"
	+ "<li><a href='ListDistrictsQuery'>Electoral Districts</a></li><li><a href='ListPartiesQuery'>Political Parties</a></li><li><a href='ListBallotsQuery'>Ballots</a></li></ul></li><li><a href='ViewProfileQuery'>View Profile</a></li><li><a href='successAnon.html'>Log Out</a></ul></div></nav>"
	+ "<div class='container'><h4>Are you sure you want to delete this item? (All information will be lost)</h3><br />";
		
		result += "<form method=post action='AreYouSureQuery'>";
		if(request.getParameter("page") != null) {
			result += "<input type='hidden' value='" + request.getParameter("page") + "' name='page'>";
			result += "<input type='hidden' value='" + request.getParameter("search") + "' name='search'>";
		}
				
		result += "<button type='submit' class='btn btn-default' name='yes' value='yes'>Yes</button> <button class='btn btn-default' name='no' onclick='goBack()'>No</button></form></div><script>function goBack() { window.history.back();}</script></body></html>";
        toClient.println(result);
        } catch(Exception e) {
        	System.out.println(e);
        }
	}
}
