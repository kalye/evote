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

public class ViewProfileQuery extends HttpServlet {

    /**                                                                                                                                                            \
                                                                                                                                                                    
     *                                                                                                                                                             \
                                                                                                                                                                    
     */
    private static final long serialVersionUID = 1L;

    public void doGet( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{

        Connection  conn = null;

        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            response.setContentType("text/html");
            // get a database connection                                                                                                                           \
                                                                                                                                                                    
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
		"<li><a href='searchDistricts.html'>Electoral Districts</a></li>" +
		"<li><a href='searchParties.html'>Political Parties</a></li>" +
		"<li><a href='searchIssues.html'>Issues</a></li>" +
		"<li><a href='searchElections.html'>Elections</a></li>" +
		"<li><a href='ListBallotsQuery'>Ballots</a></li>" +
		"<li><a href='searchCandidates.html'>Election Candidates</a></li>" +
		"</ul></li>" +
		"<li><a href='ViewProfileQuery'>Update Profile</a></li>" +
		"<li><a href='successAnon.html'>Log Out</a>" +
		"</ul></div></nav>" +
                "<h3>View Profile</h3>" ;

	    String cookieValue = "";
            Cookie[] cookies = request.getCookies();


            //if(cookies.length > 0){                                                                                                                               


            List<ElectionsOfficer> eos = logicLayer.findAllElectionsOfficers();
            //      String ssid = request.getSession().getId();                                                                                                     
            //Session session = SessionManager.getSessionById(ssid);                                                                                                
            //User user = session.getUser();                                                                                                                        

            if(eos.size() == 0){
                result += "<p>No user found</p>";
            }
            else{
                if(cookies.length > 0){
		    for(Cookie cookie: cookies){

                        if(cookie.getName().equals("electionsOfficerUser")){
                        	
                            cookieValue = cookie.getValue();
                            for(ElectionsOfficer eo: eos){

                                if(eo.getUserName().equals(cookieValue)){
                                	result+=  "<div class='container'>" +
                                		"<p>Username: " + eo.getUserName() + "(<a href=\"EditEOProfileQuery\">edit</a>) </p>" +	
				    					"<p> First Name:  " + eo.getFirstName() +  "(<a href=\"EditEOProfileQuery\">edit</a>) </p>" +
                                        "<p> Last Name:  " + eo.getLastName() +"(<a href=\"EditEOProfileQuery\">edit</a>) </p>" +
                                        "<p> Email Address:  " + eo.getEmailAddress() +"(<a href=\"EditEOProfileQuery\">edit</a>)</p>" +
                                        "<p><a href=\"EditEOProfileQuery\">Change Password</a></p>";
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
            System.out.println(e);
        }//try                                                                                                                                                      

    }//doPost                                                                                                                                                       
}

