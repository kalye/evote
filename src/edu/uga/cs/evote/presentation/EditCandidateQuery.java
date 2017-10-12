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
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class EditCandidateQuery extends HttpServlet {

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
	        
		    response.setContentType("text/html");

		    List<Candidate> candidates = logicLayer.findAllCandidates();
		    
		    String save = request.getParameter("save");
	    	String delete = request.getParameter("delete");
	    	String cookieValue = "";
	    	

	    	Candidate candidate = null;
	    	
		    Cookie[] cookies = request.getCookies();
		    Cookie cookie = null;
		    
		    if(cookies.length > 0){
		    	for(int i = 0; i < cookies.length; i++){
		    		cookie = cookies[i];
		    		if(cookie.getName().equals("candidate")){
		    			cookieValue = cookie.getValue();

		    		}
		    	}
		    }

		    long id = Long.parseLong(cookieValue);
		    candidate = objectLayer.createCandidate();
		    candidate.setId(id);
		    candidate = objectLayer.findCandidate(candidate).get(0);

		    String originalName = candidate.getName();
		    		
		    if(save != null){
			    		
		    	String name = request.getParameterValues("name")[0];
		    	String partyName = request.getParameterValues("party")[0];

		    	if(name.equals("")){
				     name = originalName;
				} 
				if(partyName.equals("")){
				     partyName = "No Party";
				}
				       
				try{
					Candidate modelCandidate = objectLayer.createCandidate();
					modelCandidate.setName(name);
					modelCandidate.setElection(candidate.getElection());
					        
					candidates = objectLayer.findCandidate(modelCandidate);
					        
					if(!name.equals(originalName)){
						if(candidates.size() > 0){
					        String redirect = new String("objectAlreadyExists.html");
			    	        response.sendRedirect(redirect);   
			    			toClient.close();
			        		conn.close();
			        		return;
					    }
					} 
					        
					PoliticalParty modelPoliticalParty = objectLayer.createPoliticalParty();
					modelPoliticalParty.setName(partyName);
					        
					List<PoliticalParty> politicalParties = objectLayer.findPoliticalParty(modelPoliticalParty);

					boolean isPartisan = candidate.getElection().getIsPartisan();
					if(politicalParties.size() == 0){
						        throw new EVException("No PoliticalParty/Election exists with that name");
					} else{
						if((partyName.equals("No Party") && isPartisan) || (!partyName.equals("No Party") && !isPartisan)){
			        		throw new Exception("no party for partisan election/parties not allowed for nonpartisan election");
			        	}//if
					}
					        
					candidate.setName(name);
					candidate.setPoliticalParty(politicalParties.get(0));
					
					objectLayer.storeCandidate(candidate);
		    		String redirect = "";
		    		String n = request.getParameter("page");
		    		System.out.println("TEAM4: page is " + n);
		    		//if(n.equals("election") || request.getParameter("page") == null) {
		    			redirect += "UpdateElectionQuery";
		    			System.out.println("TEAM4: " + candidate.getElection().getId());
			    		redirect += "?update" + candidate.getElection().getId() + "=update";
		    		//} else 
			    	if (n.equals("list")) {
		    			redirect = "ListCandidatesQuery";
		    			System.out.println("TEAM4: Go back to search");
		    			redirect += "?search=" + request.getParameter("search");
		    		}
		    		
		    		response.sendRedirect(redirect);
		    		conn.close();
		    		return;
		    				
		    	} catch(Exception e){
		    		System.out.println(e);		
		    		//toClient.println("<p>" + e + "</p>");
		    		String redirect = new String("invalidDataOfficer.html");
		    	    response.sendRedirect(redirect);   
		    		toClient.close();
		        	conn.close();
		        	return;
		    	}
			} else if(delete != null){
			    
				for (Cookie c : request.getCookies()) {
	    		    
					if(!(c.getName().equals("voterUser") || c.getName().equals("electionOfficerUser"))){
						c.setValue("");
						c.setMaxAge(0);
						c.setPath("/");
					}//if
	    		    response.addCookie(c);
	    		}
				
			    Cookie sendCookie = new Cookie("candidateDelete", "" + id);
			    sendCookie.setMaxAge(60*60);
				response.addCookie(sendCookie);
				String redirect = new String("AreYouSureQuery");
				if(request.getParameter("page") != null) {
			    	redirect += "?page=" + request.getParameter("page");
			    	redirect += "&search=" + request.getParameter("search");
			    }
				response.sendRedirect(redirect);
				toClient.close();
				conn.close();
				return;
			} 

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
		
	}//doPost
}
