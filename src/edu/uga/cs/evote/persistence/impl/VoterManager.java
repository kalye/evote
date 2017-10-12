package edu.uga.cs.evote.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;

public class VoterManager {
	private ObjectLayer objectLayer = null;
    private Connection  conn = null;
	
	public VoterManager(Connection conn, ObjectLayer objectLayer) {
		// TODO Auto-generated constructor stub
		this.conn = conn;
		this.objectLayer = objectLayer;
	}

	public void store(Voter voter) throws EVException{
		// TODO Auto-generated method stub
		
		String               insertOfficerSql = "insert into userPerson ( kind, userName, password, email, firstName, lastName, address, zipCode, voterID, electoralDistrictid, age ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";              
        String               updateOfficerSql = "update userPerson set kind = ?, userName = ?, password = ?, email = ?, firstName = ?, lastName = ?, address = ?, zipCode = ?, voterID = ?, electoralDistrictid = ?, age = ? where id = ?";              
        PreparedStatement    stmt;
        int                  inscnt;
        long                 voterId;
		
        try {
        	//create Voter in database
        	if(!voter.isPersistent()) {
        		stmt = (PreparedStatement) conn.prepareStatement(insertOfficerSql);
        	} else {
        		stmt = (PreparedStatement) conn.prepareStatement(updateOfficerSql);
        	}
        	
        	//set kind
        	stmt.setString(1, "voter");
        	
        	//edit username in database
        	if ( voter.getUserName() != null)
        		stmt.setString(2, voter.getUserName());
        	else
        		throw new EVException("voterManager.save: can't save a Voter: userName undefined");
        	
        	//edit password in database
        	if (voter.getPassword() != null)
        		stmt.setString(3, voter.getPassword());
        	else
        		throw new EVException("voterManager.save: can't save a Voter: password undefined");
        	
        	//edit email
        	if(voter.getEmailAddress() != null)
        		stmt.setString(4, voter.getEmailAddress());
        	else
        		throw new EVException("voterManager.save: can't save a Voter: email address undefined");
        	
        	//edit firstname
        	if (voter.getFirstName() != null)
        		stmt.setString(5, voter.getFirstName());
        	else
        		throw new EVException("voterManager.save: can't save a Voter: firstname undefined");
        	
        	//edit lastname
        	if (voter.getLastName() != null)
        		stmt.setString(6, voter.getLastName());
        	else
        		throw new EVException("voterManager.save: can't save a Voter: lastname undefined");
        	
        	//edit address
        	if (voter.getAddress() != null)
        		stmt.setString(7, voter.getAddress());
        	else
        		throw new EVException("voterManager.save: can't save a Voter: address undefined");
        	
        	if(voter.getZipCode() != null){
        		stmt.setString(8,  voter.getZipCode());
        	}
        	
        	stmt.setString(9,  voter.getVoterId());

        	//electoralDistrictID
        	if(voter.getElectoralDistrict() != null)
        	stmt.setLong(10, voter.getElectoralDistrict().getId());
        	
        	//age
        	if(voter.getAge() != 0){
        		stmt.setInt(11, voter.getAge());
        	}
        	
        	//id
        	if(voter.isPersistent())
        		stmt.setLong(12, voter.getId());
        	
        	inscnt = stmt.executeUpdate();
        	
        	if( !voter.isPersistent() ) {
                // in case this this object is stored for the first time,
                // we need to establish its persistent identifier (primary key)
                if( inscnt == 1 ) {
                    String sql = "select last_insert_id()";
                    if( stmt.execute( sql ) ) { // statement returned a result
                        // retrieve the result
                        ResultSet r = stmt.getResultSet();
                        // we will use only the first row!
                        while( r.next() ) {
                            // retrieve the last insert auto_increment value
                            voterId = r.getLong( 1 );
                            if( voterId > 0 )
                                voter.setId( voterId ); // set this person's db id (proxy object)
                        }
                    }
                }
            }
            else {
                if( inscnt < 1 )
                    throw new EVException( "voterManager.save: failed to save a Voter" ); 
            }
        	
        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new EVException("voterManager.save: failed to save a Voter: " + e);
        }

	} //store
	
	public void delete( Voter voter ) 
            throws EVException
    {
        String               deletePersonSql = "delete from userPerson where id = ?";              
        PreparedStatement    stmt = null;
        int                  inscnt;
        
        // form the query based on the given Person object instance
        if( !voter.isPersistent() ) // is the Person object persistent?  If not, nothing to actually delete
            throw new EVException( "voterManager.delete: failed to delete this Election Officer");
        
        try {
            
            //DELETE t1, t2 FROM t1, t2 WHERE t1.id = t2.id;
            //DELETE FROM t1, t2 USING t1, t2 WHERE t1.id = t2.id;
            stmt = (PreparedStatement) conn.prepareStatement( deletePersonSql );
            
            stmt.setLong( 1, voter.getId() );
            
            inscnt = stmt.executeUpdate();
            
            if( inscnt == 0 ) {
                throw new EVException( "voterManager.delete: failed to delete this Election Officer" );
            }
        }
        catch( SQLException e ) {
            throw new EVException( "voterManager.delete: failed to delete this Election Officer: " + e.getMessage() );
        }
    }
	
	public List<Voter> restore(Voter modelVoter) throws EVException {
		
		String selectvoterSql = "select id, userName, password, email, firstName, lastName, address, zipCode, voterID, age from userPerson where kind = 'voter'";
		Statement stmt = null;
		StringBuffer query = new StringBuffer(100);
		List<Voter> voters = new ArrayList<Voter>();
		
		
		//form query based on voter Object
		query.append(selectvoterSql);
		
		if(modelVoter != null) {
			if(modelVoter.getId() >= 0){
				query.append(" and id = " + modelVoter.getId());
			} else if( modelVoter.getUserName() != null ){ // userName is unique, so it is sufficient to get a person
                query.append( " and username = '" + modelVoter.getUserName() + "'" );
			} else {
                if( modelVoter.getPassword() != null ){
                	query.append( " and password = '" + modelVoter.getPassword() + "'" );
                }
                if( modelVoter.getEmailAddress() != null ) {
                	query.append( " and email = '" + modelVoter.getEmailAddress() + "'" );
                }

                if( modelVoter.getFirstName() != null ) {
                	query.append( " and firstName = '" + modelVoter.getFirstName() + "'" );
                }

                if( modelVoter.getLastName() != null ) {
                	query.append( " and lastName = '" + modelVoter.getLastName() + "'" );
                }

                if( modelVoter.getAddress() != null ) {
                	query.append( " and address = '" + modelVoter.getAddress() + "'" );
                }
                
                if(modelVoter.getZipCode() != null){
                	query.append(" and zipCode = '" + modelVoter.getZipCode() + "'");
                }
                if(modelVoter.getVoterId() != null){
                	query.append(" and voterID = '" + modelVoter.getVoterId() + "'");	
                }
                
                if( modelVoter.getAge() != 0){
                	query.append(" and age = " + modelVoter.getAge());
                }
                
                if(modelVoter.getElectoralDistrict() != null){
                	query.append(" and electoralDistrictid = " + modelVoter.getElectoralDistrict().getId());
                }
            }
		}
		
		try {

            stmt = conn.createStatement();

            // retrieve the persistent Person objects
            //
            if( stmt.execute( query.toString() ) ) { // statement returned a result
                ResultSet rs = stmt.getResultSet();
                long   id;
                String userName;
                String password;
                String email;
                String firstName;
                String lastName;
                String address; 
                String zipCode;
                String voterid;
                int age;
                
                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    userName = rs.getString( 2 );
                    password = rs.getString( 3 );
                    email = rs.getString( 4 );
                    firstName = rs.getString( 5 );
                    lastName = rs.getString( 6 );
                    address = rs.getString( 7 );
                    zipCode = rs.getString(8);
                    voterid = rs.getString(9);
                    age = rs.getInt( 10 );
                    
                    if(objectLayer == null){
                    	throw new EVException("objectLayer is null");
                    }
                    Voter voter = objectLayer.createVoter( firstName, lastName, userName, password, email, address, zipCode, age );
                    
                    voter.setId( id );
                    voter.setVoterId(voterid);
                    
                    voters.add( voter );

                }
                
                return voters;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "VoterManager.restore: Could not restore persistent... Voter object; Root cause: " + e );
        }
        
        // if we get to this point, it's an error
        throw new EVException( "VoterManager.restore: Could not restore persistent Voter objects" );
    }
	
	 public ElectoralDistrict restoreVoterBelongsToElectoralDistrict( Voter voter ) 
	            throws EVException
	    {
	        String       selectElectoralDistrictSql = "select e.id, e.name, e.zipCode, v.id from electoralDistrict e, userPerson v where e.id = v.electoralDistrictid";              
	        Statement    stmt = null;
	        StringBuffer query = new StringBuffer( 100 );
	        StringBuffer condition = new StringBuffer( 100 );

	        condition.setLength( 0 );
	        
	        // form the query based on the given Person object instance
	        query.append( selectElectoralDistrictSql );
	        
	        if( voter != null ) {
	            if( voter.getId() >= 0 ) // id is unique, so it is sufficient to get a person
	                query.append( " and v.id = " + voter.getId() );
	            else if( voter.getUserName() != null ) // userName is unique, so it is sufficient to get a person
	                query.append( " and v.userName = '" + voter.getUserName() + "'" );
	        }
	                
	        try {

	            stmt = conn.createStatement();

	            // retrieve the persistent Person object
	            //
	            if( stmt.execute( query.toString() ) ) { // statement returned a result
	                ResultSet rs = stmt.getResultSet();
	                
	                long   id;
	                String name;
	                String zipCode;
	               
	                ElectoralDistrict electoralDistrict = null;
	                
	                while( rs.next() ) {

	                    id = rs.getLong( 1 );
	                    name = rs.getString( 2 );
	                    zipCode = rs.getString( 3 );

	                    electoralDistrict = objectLayer.createElectoralDistrict( name, zipCode );
	                    electoralDistrict.setId( id );
	                }
	                
	                return electoralDistrict;
	            }
	            else
	                return null;
	        }
	        catch( Exception e ) {      // just in case...
	            throw new EVException( "VoterManager.restoreVoterBelongsToElectoralDistricty: Could not restore persistent ElectoralDistrict object; Root cause: " + e );
	        }
	    }
}
