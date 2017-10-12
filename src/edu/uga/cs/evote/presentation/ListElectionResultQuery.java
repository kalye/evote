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
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;

public class ListElectionResultQuery extends HttpServlet  {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4746837609081425474L;

	public void doGet( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{

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

			LogicLayer logicLayer = new LogicLayerImpl(conn);

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
					"<li><a href='ListBallotsQuery'>Ballots</a></li>" +
					"<li><a href='searchDistricts.html'>Electoral Districts</a></li>" +
					"<li><a href='searchIssues.html'>Issues</a></li>" +
					"<li><a href='searchElections.html'>Elections</a></li>" +
					"<li><a href='searchCandidatess.html'>Candidates</a></li>" +
					"<li><a href='searchParties.html'>Political Parties</a></li>" +
					"</ul></li>" +
					"<li><a href='ViewProfileQuery'>Update Profile</a></li>" +
					"<li><a href='successAnon.html'>Log Out</a>" +
					"</ul></div></nav>";
			result += "<div class='container'>" +
					"<h2>Electoral Districts</h2>" +
					"<br /> <br />";

			List<Election> elections = logicLayer.findAllElections();
			if(elections.size() == 0){
				result += "<p>No Election found</p>";
			} else{
				result +="<table class=\"table table-bordered\"><tr><td>Is partisian</td>"
						+ "<td>total vote count</td>"
						+ "<td> Candidate-vote </td></tr>";
				for(int i = 0; i < elections.size(); i++){
					Election election = elections.get(i);
					result += "<tr><td>" + election.getIsPartisan() + "</td>"
							+ "<td>" + election.getVoteCount() + "</td>"
							+ "<td><table class=\"table\">";
					for(Candidate candidate: election.getCandidates()){
						result += "<tr><td>" + candidate.getName() + "-" + candidate.getVoteCount() + " </td></tr>";
					}

					result +=  "</table></td></tr>";
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
		}//try

	}//doPost

}
