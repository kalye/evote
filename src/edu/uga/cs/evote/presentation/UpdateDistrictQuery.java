package edu.uga.cs.evote.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uga.cs.evote.db.DbUtils;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;

public class UpdateDistrictQuery extends HttpServlet {

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

		    List<ElectoralDistrict> electoralDistricts = logicLayer.findAllElectoralDistricts();
		    
		    String update;
		    String delete;
		    
		    for(ElectoralDistrict district: electoralDistricts){
		    	
		    	update = request.getParameter("update" + district.getId());
		    	delete = request.getParameter("delete" + district.getId());
		    	
		    	if(update != null){

		    		for (Cookie c : request.getCookies()) {
		    		    
						if(!(c.getName().equals("voterUser") || c.getName().equals("electionOfficerUser"))){
							c.setValue("");
							c.setMaxAge(0);
							c.setPath("/");
						}//if
		    		    response.addCookie(c);
		    		}

		    		Cookie cookie = new Cookie("district", "" + district.getId());
				    cookie.setMaxAge(60*60);
				    response.addCookie(cookie);
				    
				    List<Voter> voters = logicLayer.findVoters(district);
				    List<Ballot> ballots = district.getBallots();
				    
				    if(voters.size() > 0 || ballots.size() > 0){
				    	String redirect = new String("noEdit.html");
					    response.sendRedirect(redirect);
					    conn.close();
					    toClient.close();
					    return;
				    }
				    
				    result += "<div class='container'>" +
				    				"<h3>Update Electoral District</h3>" +
				    				"<form method=post action='EditDistrictQuery'>" +
				    					"<div class='form-group'>" +
				    						"<label for='name'>Name:</label> <input type='name'" +
				    						"class='form-control' id='name' name='name' placeholder='" + district.getName() + "'>" +
				    					"</div>" +
				    					"<div class='form-group'>" +
				    						"<label for='zip'>Zip Code:</label> <input type='zip'" +
				    						"class='form-control' id='zip' name='zip' placeholder='" + district.getZipCode() + "'>" +
				    					"</div>" + 
				    					"<button type='submit' class='btn btn-default' name='save'>Save Changes</button>" +
				    					"<a type='button' class='btn btn-default' href='searchDistricts.html'>Cancel</a>" +
				    					"<button type='delete' class='btn btn-default' name='delete'>Delete Electoral District</button>" +
				    				"</form>" +
				    			"</div>" +
				    		"</body>" +
				    	"</html>";
				   toClient.println(result);
				   conn.close();
				   toClient.close();
		    	} else if(delete != null){
		    		
		    		List<Voter> voters = logicLayer.findVoters(district);
		    		List<Ballot> ballots = district.getBallots();
		    		
				    if(voters.size() > 0 || ballots.size() > 0){
				    	
				    	String redirect = new String("noEdit.html");
					    response.sendRedirect(redirect);
					    conn.close();
					    toClient.close();
					    return;
				    }
				    
				    for (Cookie c : request.getCookies()) {
		    		    
						if(!(c.getName().equals("voterUser") || c.getName().equals("electionOfficerUser"))){
							c.setValue("");
							c.setMaxAge(0);
							c.setPath("/");
						}//if
		    		    response.addCookie(c);
		    		}
				    
		    		Cookie sendCookie = new Cookie("districtDelete", "" + district.getId());
		    		sendCookie.setMaxAge(60*60);
				    response.addCookie(sendCookie);
				    String redirect = new String("AreYouSure.html");
				    response.sendRedirect(redirect);
				    conn.close();
				    toClient.close();
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
