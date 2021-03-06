package edu.uga.cs.evote.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uga.cs.evote.db.DbUtils;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.impl.ElectionImpl;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.impl.ElectionManager;

public class DeleteIssue extends HttpServlet {



	/**
	 * 
	 */
	private static final long serialVersionUID = 5999726587839061176L;

	/**
	 * 
	 */

	public void doPost( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{

		Connection  conn = null;

		try{
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



			PrintWriter toClient = response.getWriter();

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
					"<li><a href='UpdateCandidates.html'>Election Candidates</a></li>" +
					"<li><a href='ListDistrictsQuery'>Electoral Districts</a></li>" +
					"<li><a href='ListPartiesQuery'>Political Parties</a></li>" +
					"<li><a href='ListBallotsQuery'>Ballots</a></li>" +
					"</ul></li>" +
					"<li><a href='ViewProfileQuery'>Update Profile</a></li>" +
					"<li><a href='successAnon.html'>Log Out</a>" +
					"</ul></div></nav>";
			result += "<div class='container'>" +
					"<h2>Electoral Districts</h2>" +
					"<br /> <br />";
			ElectionManager electionManager = new ElectionManager(conn, new ObjectLayerImpl());
			String issueId = request.getParameterValues("issueId")[0];
			ElectionImpl election = new ElectionImpl();
			election.setId(Long.parseLong(issueId));
			List<Election> ellections = electionManager.restore(election);
			if(ellections.size() == 0){
				result += "<p>No Election Found to be deleted</p>";
			} else{
				try{
					electionManager.delete(election);
					result += "<p>Election with id " + election.getId() + " successfully deleted.</p>";
				} catch (Exception e) {
					result += "<p>Error while deleting issue with id: " + election.getId() + ". Error-" + e.getMessage()+ "</p>";
				}		    	
				toClient.println(result);
				conn.close();
				toClient.close();			

			}
		} catch(Exception e){

			try{
				conn.close();
			} catch(Exception f){

			}
			System.out.println(e.getMessage());
		}//try

	}//doPost

}
