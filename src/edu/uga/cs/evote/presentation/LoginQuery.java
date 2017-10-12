package edu.uga.cs.evote.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.db.DbUtils;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.User;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;

public class LoginQuery extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{

		try{
	    			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
	    
			response.setContentType("text/html");

			String username = request.getParameterValues("username")[0];
		    String password = request.getParameterValues("password")[0];
		    
		    Connection  conn = null;
	        
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
	           
	        HttpSession httpSession = request.getSession();
	        LogicLayer logicLayer = new LogicLayerImpl(conn);
	        Session session = SessionManager.createSession();
	        String redirect = "";
	        String ssid;
	        try{
	        	
	        	ssid = logicLayer.login( session, username, password );
	        } catch(EVException e){
	        	redirect = new String("wrongLogin.html");
	        	response.sendRedirect(redirect);  
    		    conn.close();
    		    return;
	        }
	        
	        httpSession.setAttribute("ssid", ssid);
	        
	        User user = session.getUser();
	        String userName = user.getUserName();
		if(user instanceof ElectionsOfficer){
		    Cookie cookie = new Cookie("electionsOfficerUser", userName);
		    cookie.setMaxAge(60*60);
		    response.addCookie(cookie);
	            redirect = new String("ElectionsOfficerHome.html");
	            response.sendRedirect(redirect);    
    		    conn.close();
    		    return;
	        } else{ //(user instanceof Voter){
		    Cookie cookie = new Cookie("voterUser", userName);
		    cookie.setMaxAge(60*60);
		    response.addCookie(cookie);
		    redirect = new String("VoterHome.html");
		    response.sendRedirect(redirect);    
    		    conn.close();
    		    return;
	        }
	        	
		} catch(Exception e){
	
		    System.out.println(e.getMessage());
		}//try
    }//doPost
}//StockQuery
