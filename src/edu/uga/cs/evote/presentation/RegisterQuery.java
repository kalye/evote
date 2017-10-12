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
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class RegisterQuery extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{
		PrintWriter toClient = response.getWriter();
		Connection  conn = null;

		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
	    
			response.setContentType("text/html");
		    
		    
	        ObjectLayer objectLayer = null;
	        PersistenceLayer persistence = null;
	
	        // get a database connection
	        try {
	            conn = DbUtils.connect();
	        } 
	        catch (Exception seq) {
			toClient.println("<P>Not valid");
	            System.err.println( "Unable to obtain a database connection" );
	        }
	        
	        if( conn == null ) {
			toClient.println("<P>Invalid too");
	            System.out.println( "failed to connect to the database" );
	            return;
	        }
	        
	        // obtain a reference to the ObjectModel module      
	        objectLayer = new ObjectLayerImpl();
	        // obtain a reference to Persistence module and connect it to the ObjectModel        
	        persistence = new PersistenceLayerImpl( conn, objectLayer ); 
	        // connect the ObjectModel module to the Persistence module
	        objectLayer = new ObjectLayerImpl(persistence);
	        persistence.setObjectLayer(objectLayer);
		 
	        String firstName = request.getParameterValues("fname")[0];
		    String lastName = request.getParameterValues("lname")[0];
		    String email = request.getParameterValues("email")[0];
		    String address = request.getParameterValues("addr")[0];
		    String zipCode = request.getParameterValues("zip")[0];
		    String userName = request.getParameterValues("uname")[0];
		    String password = request.getParameterValues("pwd")[0];
		    String reEnter = request.getParameterValues("repwd")[0];
		    String age = request.getParameterValues("age")[0];
		    
	        String redirect = "";
	        
	        int ageNum = 0;
	        ElectoralDistrict electoralDistrict = null;
	        String voterId = "";
	        
	        if(	firstName.equals("") || lastName.equals("") || email.equals("") || address.equals("") ||
	        	zipCode.equals("") || userName.equals("") || password.equals("") || reEnter.equals("")){
	        	
	        	redirect = "emptyFieldsAnon.html";
	        	response.sendRedirect(redirect);    
    		    conn.close();
    		    return;
	        } else if(!password.equals(reEnter)){
	        		
	        	redirect = "invalidDataAnon.html";
	        	response.sendRedirect(redirect);   
	        	conn.close();
    		    
    		   // return;
	        } else{
	        	try{
	        		if(zipCode.length() != 5){
					toClient.println("<P>Zip Code not right length");
	        			throw new Exception();
	        		}
	        		
	        		Integer.parseInt(zipCode);
	        		
	        		ElectoralDistrict modelElectoralDistrict = objectLayer.createElectoralDistrict();
	        		modelElectoralDistrict.setZipCode(zipCode);
	        		
	        		List<ElectoralDistrict> electoralDistricts = objectLayer.findElectoralDistrict(modelElectoralDistrict);
	        		if(electoralDistricts.size() == 0){
					toClient.println("<P>Electoral District Size = 0");
	        			throw new Exception();
	        		}
	        		
	        		electoralDistrict = electoralDistricts.get(0);
	        	} catch(Exception e){
	        		
	        		redirect = "invalidDataAnon.html";
	        		response.sendRedirect(redirect);    
	    		    conn.close();
	    		    return;
	        	}
	        	
	        	try{
	        		ageNum = Integer.parseInt(age);
	        	} catch(Exception e){
	        		
	        		redirect = "invalidDataAnon.html";
	        		response.sendRedirect(redirect);    
	    		    conn.close();
	    		    return;
	        	}
	        	
	        	Voter modelVoter = objectLayer.createVoter();
	        	modelVoter.setUserName(userName);
	        	
	        	Voter modelVoter2 = objectLayer.createVoter();
	        	modelVoter2.setEmailAddress(email);
	        	
	        	ElectionsOfficer modelElectionsOfficer = objectLayer.createElectionsOfficer();
	        	modelElectionsOfficer.setUserName(userName);
	        	
	        	ElectionsOfficer modelElectionsOfficer2 = objectLayer.createElectionsOfficer();
	        	modelElectionsOfficer2.setEmailAddress(email);
	        	
	        	List<Voter> voters = objectLayer.findVoter(modelVoter);
	        	List<Voter> voters2 = objectLayer.findVoter(modelVoter2);
	        	List<ElectionsOfficer> electionsOfficers = objectLayer.findElectionsOfficer(modelElectionsOfficer);
	        	List<ElectionsOfficer> electionsOfficers2 = objectLayer.findElectionsOfficer(modelElectionsOfficer2);
	        	
	        	if(voters.size() > 0 || electionsOfficers.size() > 0 || voters2.size() > 0 || electionsOfficers2.size() > 0){
	        		redirect = "userAlreadyExists.html";
	        		response.sendRedirect(redirect);    
	    		    conn.close();
	    		    return;
	        	} else{
	        		
	        		LogicLayer logicLayer = new LogicLayerImpl(conn);
	        		voters = logicLayer.findAllVoters();
	        		int idNum = voters.size() + 10;
	        		
	        		voterId = "" + firstName.charAt(0) + lastName.charAt(0) + idNum;
	        		
	        		logicLayer.createVoter(	firstName, lastName, userName, password, email, address, zipCode, voterId, ageNum, electoralDistrict);
	        		
	        		redirect = "successAnon.html";
	        		response.sendRedirect(redirect);    
	    		    conn.close();
	    		    return;
	        	}
	        }
			
		} catch(Exception e){
			try{
				conn.close();
			} catch(Exception f){
				
			}
			toClient.println("<h1>Error: </h1><P>" + e);	
		    System.out.println(e.getMessage());
		}//try
    }//doPost
}
