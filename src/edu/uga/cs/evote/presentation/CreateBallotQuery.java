package edu.uga.cs.evote.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uga.cs.evote.db.DbUtils;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class CreateBallotQuery extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{

		Connection conn = null;
		PrintWriter toClient = response.getWriter();
		try{
	    
			Class.forName("com.mysql.jdbc.Driver").newInstance();
	    
			response.setContentType("text/html");

		    
		    String redirect = "successOfficer.html"; 
		    
		    String districtName = request.getParameterValues("district")[0];
		    
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date openDate = format.parse(request.getParameterValues("openDate")[0]);
			Date closeDate = format.parse(request.getParameterValues("closeDate")[0]);
		    
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

	        Ballot ballot = objectLayer.createBallot();
	        
	        if(closeDate.before(openDate)){
				throw new Exception("Close date is before open date");
			} else if(openDate.before(new Date())){
				throw new Exception("Open date is past");
			}//if
			
			ElectoralDistrict modelElectoralDistrict = objectLayer.createElectoralDistrict();
			modelElectoralDistrict.setName(districtName);
			List<ElectoralDistrict> districts = objectLayer.findElectoralDistrict(modelElectoralDistrict);
			if(districts.size() == 0){
				throw new Exception("District does not exist");
			}
			
	        Ballot modelBallot = objectLayer.createBallot();
	        modelBallot.setElectoralDistrict(districts.get(0));
	        modelBallot.setOpenDate(openDate);
	        modelBallot.setCloseDate(closeDate);
	        List<Ballot> ballots2 = objectLayer.findBallot(modelBallot);
	        
	        if(ballots2.size() > 0){
	        	redirect = "objectAlreadyExists.html";
        		response.sendRedirect(redirect);    
			    conn.close();
			    return;
	        }
	        
	        ballot.setElectoralDistrict(districts.get(0));
	        ballot.setOpenDate(openDate);
	        ballot.setCloseDate(closeDate);
	        
			objectLayer.storeBallot(ballot);
			
			response.sendRedirect(redirect);
			conn.close();
			return;
		} catch(Exception e){
	
			String redirect = new String("invalidDataOfficer.html");
        	response.sendRedirect(redirect);   
			toClient.close();
		    try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    return;
		}//try
    }//doPost
}//CreateDistrictQuery
