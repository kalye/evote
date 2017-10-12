package edu.uga.cs.evote.presentation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.uga.cs.evote.session.SessionManager;


public class LogoutQuery extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void doGet( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{

		try{
			
			HttpSession httpSession = request.getSession();
			String ssid = (String) httpSession.getAttribute("ssid");
			SessionManager.logout(ssid);
			
			for (Cookie c : request.getCookies()) {

				c.setValue("");
				c.setMaxAge(0);
				c.setPath("/");

    		    response.addCookie(c);
    		}
    		
			response.sendRedirect("successAnon.html");
		} catch(Exception e){

		    System.out.println(e.getMessage());
		}//try
		
	}//doPost
}
