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
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class CreateDistrictQuery extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{

		Connection conn = null;
		PrintWriter toClient = response.getWriter();
		try{
	    
			Class.forName("com.mysql.jdbc.Driver").newInstance();
	    
			response.setContentType("text/html");

		    
		    String redirect = "successOfficer.html"; 
		    
		    String name = request.getParameterValues("name")[0];
		    String zipCode = request.getParameterValues("zipCode")[0];
		    
	        ObjectLayer objectLayer = null;
	        PersistenceLayer persistence = null;
	
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
	        
	        // obtain a reference to the ObjectModel module      
	        objectLayer = new ObjectLayerImpl();
	        // obtain a reference to Persistence module and connect it to the ObjectModel        
	        persistence = new PersistenceLayerImpl( conn, objectLayer ); 
	        // connect the ObjectModel module to the Persistence module
	        objectLayer = new ObjectLayerImpl(persistence);
	        persistence.setObjectLayer(objectLayer);
		    
	        try{
	        	if(zipCode.length() != 5 || name == ""){
	        		throw new Exception();
	        	}
	        	
	        	Integer.parseInt(zipCode);
	        	
	        } catch(Exception e){
	        	redirect = "invalidDataOfficer.html";
	        	response.sendRedirect(redirect);    
			    conn.close();
			    return;
	        }
	        
	        ElectoralDistrict modelElectoralDistrict = objectLayer.createElectoralDistrict();
        	modelElectoralDistrict.setZipCode(zipCode);
        	
        	ElectoralDistrict modelElectoralDistrict2 = objectLayer.createElectoralDistrict();
        	modelElectoralDistrict2.setName(name);
        	
        	List<ElectoralDistrict> electoralDistricts = objectLayer.findElectoralDistrict(modelElectoralDistrict);
        	List<ElectoralDistrict> electoralDistricts2 = objectLayer.findElectoralDistrict(modelElectoralDistrict2);
        	
        	if(electoralDistricts.size() != 0 || electoralDistricts2.size() != 0){
        		redirect = "objectAlreadyExists.html";
        		response.sendRedirect(redirect);    
			    conn.close();
			    return;
        	}
        		
	        LogicLayer logicLayer = new LogicLayerImpl(conn);
	        logicLayer.createElectoralDistrict(name, zipCode);
	        
	        response.sendRedirect(redirect);    
		    conn.close();
			
		} catch(Exception e){
	
			try{
				conn.close();
			} catch(Exception f){
				
			}
		    toClient.println("<p>" + e + "</p>");
		}//try
    }//doPost
}//CreateDistrictQuery
