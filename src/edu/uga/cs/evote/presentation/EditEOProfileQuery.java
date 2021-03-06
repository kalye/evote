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
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;


//will have another html file with html forms that have boxes to allow the user to select new information                                  
public class EditEOProfileQuery extends HttpServlet  {

    private static final long serialVersionUID = 1L;

    public void doGet( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{

        Connection  conn = null;

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();

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
		"<html lang=\"en\">" +
                "<head>" +
                "<title>View Profile</title>" +
		"<meta charset=\"utf-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" +
		"<link rel=\"stylesheet\" href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">" +
                "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js\"></script>" +
		"<script src=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>" +
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
		"<li><a href='searchCandidates.html'>Election Candidates</a></li>" +
		"<li><a href='searchDistricts.html'>Electoral Districts</a></li>" +
		"<li><a href='searchParties.html'>Political Parties</a></li>" +
		"<li><a href='searchElections.html'>Elections</a></li>" +
		"<li><a href='ListBallotsQuery'>Ballots</a></li>" +
		"</ul></li>" +
		"<li><a href='ViewProfileQuery'>Update Profile</a></li>" +
		"<li><a href='successAnon.html'>Log Out</a>" +
		"</ul></div></nav>" +
		"<h1>Edit Profile Information</h1>" ;

            String cookieValue = "";
    	    Cookie[] cookies = request.getCookies();
    	    Cookie cookie = null;

            List<ElectionsOfficer> officers = logicLayer.findAllElectionsOfficers();
    	    //	    String ssid = request.getSession().getId();
    	    //Session session = SessionManager.getSessionById(ssid);
    	    //User user = session.getUser();

    	    if(officers.size() == 0){
    		result += "<p>No user found</p>";
    	    }
    	    else{
    		if(cookies.length > 0){
    		    for(int j = 0; j < cookies.length; j++){

    			cookie = cookies[j];

                            if(cookie.getName().equals("electionsOfficerUser")){
                            	
                            	cookieValue = cookie.getValue();
                                for(int i = 0; i < officers.size(); i++){
                                	
                                    ElectionsOfficer officer = officers.get(i);  //if this doesn't work, compare each voter value in the list to the user values                       
                                    if(officer.getUserName().equals(cookieValue)){
                                    	result+=  "<div class='container'>" +
                                    			"<form method=post action='UpdateEOProfileQuery'>" +
                                    			"<div class='form-group'>" +
													"<label for='userName'>Username:</label> <p>" + officer.getUserName() + "</p>" +
												"</div>" +
												"<div class='form-group'>" +
													"<label for='firstName'>First Name:</label> <input type='text'" +
														"class='form-control' name='firstName' placeholder='" + officer.getFirstName() + "'>" +
												"</div>" +
												"<div class='form-group'>" +
													"<label for='lastName'>Last Name:</label> <input type='text'" +
														"class='form-control' name='lastName' placeholder='" + officer.getLastName() + "'>" +
												"</div>" +
												"<div class='form-group'>" +
													"<label for='email'>Email Address:</label> <input type='email'" +
														"class='form-control' name='email' placeholder='" + officer.getEmailAddress() + "'>" +
												"</div>" +
												"<button type='submit' class='btn btn-default' name='save'>Save</button>" +
												"<a type='button' class='btn btn-default' href='ViewProfileQuery'>Cancel</a>" +
											"</form>" +
											"<form method=post action='ChangeEOPasswordQuery'>" +
												"<h4>Change Password:</h4>" +
												"<div class='form-group'>" +
													"<label for='pass'>Current Password:</label> <input type='password'" +
													"class='form-control' name='pass'>" +
												"</div>" +
												"<div class='form-group'>" +
													"<label for='newPass'>New Password:</label> <input type='password'" +
													"class='form-control' name='newPass'>" +
												"</div>" +
												"<div class='form-group'>" +
													"<label for='reEnter'>Re-Enter New Password:</label> <input type='password'" +
													"class='form-control' name='reEnter'>" +
												"</div>" +	
												"<button type='submit' class='btn btn-default'>Save Password Change</button>" +
											"</form>";
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

        }//catch                                                                                                                                                                 
    }//doGet                                                                                                                                                                     
}

	    