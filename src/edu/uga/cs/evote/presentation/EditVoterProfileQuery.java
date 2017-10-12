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
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;


//will have another html file with html forms that have boxes to allow the user to select new information
public class EditVoterProfileQuery extends HttpServlet  {

    private static final long serialVersionUID = 1L;

    public void doGet( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{

        Connection  conn = null;

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            response.setContentType("text/html");
            // get a database connection                                                                                                  \
                                                                                                                                           
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
        "</nav>" +
                "<h1>Edit Profile Information</h1>" ;

            String cookieValue = "";
    	    Cookie[] cookies = request.getCookies();
    	    Cookie cookie = null;


    	    //if(cookies.length > 0){
    	    

    	    List<Voter> voters = logicLayer.findAllVoters();
    	    //	    String ssid = request.getSession().getId();
    	    //Session session = SessionManager.getSessionById(ssid);
    	    //User user = session.getUser();

    	    if(voters.size() == 0){
    		result += "<p>No user found</p>";
    	    }
    	    else{
    		if(cookies.length > 0){
    		    for(int j = 0; j < cookies.length; j++){

    			cookie = cookies[j];

                            if(cookie.getName().equals("voterUser")){
                            	
                            	cookieValue = cookie.getValue();
                                for(int i = 0; i < voters.size(); i++){
                                	
                                    Voter voter = voters.get(i);  //if this doesn't work, compare each voter value in the list to the user values                       
                                    if(voter.getUserName().equals(cookieValue)){
                                    	result+=  "<div class='container'>" +
                                    			"<form method=post action='UpdateVoterProfileQuery'>" +
                                    			"<div class='form-group'>" +
													"<label for='userName'>Username:</label> <p>" + voter.getUserName() + "</p>" +
												"</div>" +
												"<div class='form-group'>" +
													"<label for='voterId'>Voter ID:</label> <p>" + voter.getVoterId() + "</p>" +
												"</div>" +
												"<div class='form-group'>" +
													"<label for='firstName'>First Name:</label> <input type='text'" +
														"class='form-control' name='firstName' placeholder='" + voter.getFirstName() + "'>" +
												"</div>" +
												"<div class='form-group'>" +
													"<label for='lastName'>Last Name:</label> <input type='text'" +
														"class='form-control' name='lastName' placeholder='" + voter.getLastName() + "'>" +
												"</div>" +
												"<div class='form-group'>" +
													"<label for='age'>Age:</label> <input type='input type='number' name='age' min='1' max='150' " +
													"class='form-control' placeholder='" + voter.getAge() + "'>" +
												"</div>" +		
												"<div class='form-group'>" +
													"<label for='email'>Email Address:</label> <input type='email'" +
														"class='form-control' name='email' placeholder='" + voter.getEmailAddress() + "'>" +
												"</div>" +
												"<div class='form-group'>" +
													"<label for='address'>Address:</label> <input type='text'" +
													"class='form-control' name='address' placeholder='" + voter.getAddress() + "'>" +
												"</div>" +
												"<div class='form-group'>" +
													"<label for='zip'>Zip Code:</label> <input type='text'" +
													"class='form-control' name='zip' placeholder='" + voter.getZipCode() + "'>" +
												"</div>" +
												"<button type='submit' class='btn btn-default' name='save'>Save</button>" +
												"<a type='button' class='btn btn-default' href='ViewVoterProfileQuery'>Cancel</a>" +
											"</form>" +
											"<form method=post action='ChangeVoterPasswordQuery'>" +
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