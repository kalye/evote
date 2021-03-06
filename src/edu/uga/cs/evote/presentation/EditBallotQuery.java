package edu.uga.cs.evote.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uga.cs.evote.db.DbUtils;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.logic.impl.LogicLayerImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class EditBallotQuery extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void doPost( HttpServletRequest  request, HttpServletResponse response ) throws ServletException, IOException{

		Connection  conn = null;
		PrintWriter toClient = response.getWriter();
		
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		    
			response.setContentType("text/html");

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
	        
	        ObjectLayer objectLayer = new ObjectLayerImpl();
	        PersistenceLayer persistence = new PersistenceLayerImpl( conn, objectLayer ); 
	        objectLayer = new ObjectLayerImpl(persistence);
	        persistence.setObjectLayer(objectLayer);
	        LogicLayer logicLayer = new LogicLayerImpl(conn);
	        
		    response.setContentType("text/html");
		    
		    String save = request.getParameter("save");
	    	String delete = request.getParameter("delete");
	    	

	    	Ballot ballot = objectLayer.createBallot();
	    	
		    Cookie[] cookies = request.getCookies();
		    Cookie cookie = null;
		    
		    if(cookies.length > 0){
		    	for(int i = 0; i < cookies.length; i++){
		    		cookie = cookies[i];
		    		if(cookie.getName().equals("ballot")){
		    			long id = Long.parseLong(cookie.getValue());
		    			List<Ballot> ballots = logicLayer.findAllBallots();
		    			for(Ballot b: ballots){
		    				if(b.getId() == id){
		    					ballot = b;
		    				}
		    			}
		    		}
		    	}
		    }

		    //long id = Long.parseLong(cookieValue);
		    //ballot = objectLayer.createBallot();
		    //ballot.setId(id);
		    //ballot = objectLayer.findBallot(ballot).get(0);
		    
		    String originalDistrictName = ballot.getElectoralDistrict().getName();
		    		
		    if(save != null){
			    		
		    	String districtName = request.getParameterValues("district")[0];

		    	if(districtName.equals("")){
		    		districtName = originalDistrictName;
				} 
		    			
				try{

					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			    	Date openDate = format.parse(request.getParameterValues("openDate")[0]);
			    	Date closeDate = format.parse(request.getParameterValues("closeDate")[0]);
			    			
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
					modelBallot.setOpenDate(openDate);
					modelBallot.setCloseDate(closeDate);
					modelBallot.setElectoralDistrict(districts.get(0));
					        
					List<Ballot> ballots2 = objectLayer.findBallot(modelBallot);
					        
					if(!districtName.equals(originalDistrictName)){
					     if(ballots2.size() > 0){
					        String redirect = "objectAlreadyExists.html";
					        response.sendRedirect(redirect);    
							conn.close();
							return;
					     }
					} 
					        
					ballot.setElectoralDistrict(districts.get(0));
					ballot.setOpenDate(openDate);
					ballot.setCloseDate(closeDate);
					        
		    		objectLayer.storeBallot(ballot);
		    		
		    		String redirect = new String("successOfficer.html");
		    		response.sendRedirect(redirect);
		    		conn.close();
		    		return;
		    				
		    	} catch(Exception e){
		    				
		    		toClient.println("<p>" + e + "</p>");
		    		//String redirect = new String("invalidDataOfficer.html");
		    	    //response.sendRedirect(redirect);   
		    		toClient.close();
		        	conn.close();
		        	return;
		    	}
			} else if(delete != null){
			    
				for (Cookie c : request.getCookies()) {
	    		    
					if(!(c.getName().equals("voterUser") || c.getName().equals("electionOfficerUser"))){
						c.setValue("");
						c.setMaxAge(0);
						c.setPath("/");
					}//if
	    		    response.addCookie(c);
	    		}
				
			    cookie = new Cookie("ballotDelete", "" + ballot.getId());
			    cookie.setMaxAge(60*60);
				response.addCookie(cookie);
				String redirect = new String("AreYouSure.html");
				response.sendRedirect(redirect);
				toClient.close();
				conn.close();
				return;
			} 		
			    
		} catch(Exception e){

		    toClient.println("<p>" + e + "</p>");
		    toClient.close();
		    try {
				conn.close();
			} catch (SQLException e1) {
			}
		}//try
		
	}//doPost
}
