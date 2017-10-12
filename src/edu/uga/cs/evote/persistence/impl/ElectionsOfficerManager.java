package edu.uga.cs.evote.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.object.ObjectLayer;

public class ElectionsOfficerManager {

	private ObjectLayer objectLayer = null;
    private Connection  conn = null;
	
	public ElectionsOfficerManager(Connection conn, ObjectLayer objectLayer) {
		// TODO Auto-generated constructor stub
		this.conn = conn;
		this.objectLayer = objectLayer;
	}

	public void store(ElectionsOfficer electionsOfficer) throws EVException{
		// TODO Auto-generated method stub
		
		String               insertOfficerSql = "insert into userPerson ( kind, userName, password, email, firstName, lastName, address, zipCode, voterID, electoralDistrictid, age ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";              
        String               updateOfficerSql = "update userPerson set kind = ?, userName = ?, password = ?, email = ?, firstName = ?, lastName = ?, address = ?, zipCode = ?, voterID = ?, electoralDistrictid = ?, age = ? where id = ?";              
        PreparedStatement    stmt;
        int                  inscnt;
        long                 electionsOfficerId;
		
        try {
        	//create Elections Officer in database
        	if(!electionsOfficer.isPersistent()) {
        		stmt = (PreparedStatement) conn.prepareStatement(insertOfficerSql);
        	} else {
        		stmt = (PreparedStatement) conn.prepareStatement(updateOfficerSql);
        	}
        	
        	//set kind
        	stmt.setString(1, "electionsOfficer");
        	
        	//edit username in database
        	if ( electionsOfficer.getUserName() != null)
        		stmt.setString(2, electionsOfficer.getUserName());
        	else
        		throw new EVException("ElectionsOfficerManager.save: can't save an Elections Officer: userName undefined");
        	
        	//edit password in database
        	if (electionsOfficer.getPassword() != null)
        		stmt.setString(3, electionsOfficer.getPassword());
        	else
        		throw new EVException("ElectionsOfficerManager.save: can't save an Elections Officer: password undefined");
        	
        	//edit email
        	if(electionsOfficer.getEmailAddress() != null)
        		stmt.setString(4, electionsOfficer.getEmailAddress());
        	else
        		throw new EVException("ElectionsOfficerManager.save: can't save an Elections Officer: email address undefined");
        	
        	//edit firstname
        	if (electionsOfficer.getFirstName() != null)
        		stmt.setString(5, electionsOfficer.getFirstName());
        	else
        		throw new EVException("ElectionsOfficerManager.save: can't save an Elections Officer: firstname undefined");
        	
        	//edit lastname
        	if (electionsOfficer.getLastName() != null)
        		stmt.setString(6, electionsOfficer.getLastName());
        	else
        		throw new EVException("ElectionsOfficerManager.save: can't save an Elections Officer: lastname undefined");
        	
        	//edit address
        	if (electionsOfficer.getAddress() != null)
        		stmt.setString(7, electionsOfficer.getAddress());
        	else
        		throw new EVException("ElectionsOfficerManager.save: can't save an Elections Officer: address undefined");
        	
        	if(electionsOfficer.getZipCode() != null)
        		stmt.setString(8,  electionsOfficer.getZipCode());
        	else
        		throw new EVException("ElectionsOfficerManager.save: can't save an Elections Officer: address undefined");
        	//voterID
        	stmt.setNull(9, java.sql.Types.INTEGER);
        	
        	//electoralDistrictID
        	stmt.setNull(10, java.sql.Types.INTEGER);
        	
        	//age
        	stmt.setInt(11, 0);
        	
        	//id
        	if(electionsOfficer.isPersistent())
        		stmt.setLong(12, electionsOfficer.getId());
        	
        	inscnt = stmt.executeUpdate();
        	
        	if( !electionsOfficer.isPersistent() ) {
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
                            electionsOfficerId = r.getLong( 1 );
                            if( electionsOfficerId > 0 )
                                electionsOfficer.setId( electionsOfficerId ); // set this person's db id (proxy object)
                        }
                    }
                }
            }
            else {
                if( inscnt < 1 )
                    throw new EVException( "ElectionsOfficerManager.save: failed to save an Elections Officer" ); 
            }
        	
        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new EVException("ElectionsOfficerManager.save: failed to save an Elections Officer: " + e);
        }

	} //store
	
	public void delete( ElectionsOfficer electionsOfficer ) 
            throws EVException
    {
        String               deletePersonSql = "delete from userPerson where id = ?";              
        PreparedStatement    stmt = null;
        int                  inscnt;
        
        // form the query based on the given Person object instance
        if( !electionsOfficer.isPersistent() ) // is the Person object persistent?  If not, nothing to actually delete
            throw new EVException( "ElectionsOfficerManager.delete: failed to delete this Election Officer");
        
        try {
            
            //DELETE t1, t2 FROM t1, t2 WHERE t1.id = t2.id;
            //DELETE FROM t1, t2 USING t1, t2 WHERE t1.id = t2.id;
            stmt = (PreparedStatement) conn.prepareStatement( deletePersonSql );
            
            stmt.setLong( 1, electionsOfficer.getId() );
            
            inscnt = stmt.executeUpdate();
            
            if( inscnt == 0 ) {
                throw new EVException( "ElectionsOfficerManager.delete: failed to delete this Election Officer" );
            }
        }
        catch( SQLException e ) {
            throw new EVException( "ElectionsOfficerManager.delete: failed to delete this Election Officer: " + e.getMessage() );
        }
    }
	
	public List<ElectionsOfficer> restore(ElectionsOfficer modelElectionsOfficer) throws EVException {
		
		String selectElectionsOfficerSql = "select id, userName, password, email, firstName, lastName, address, zipCode from userPerson where kind = 'electionsOfficer'";
		Statement stmt = null;
		StringBuffer query = new StringBuffer(100);
		List<ElectionsOfficer> electionsOfficers = new ArrayList<ElectionsOfficer>();
		
		//form query based on ElectionsOfficer Object
		query.append(selectElectionsOfficerSql);
		
		if(modelElectionsOfficer != null) {
			if(modelElectionsOfficer.getId() >= 0){
				query.append(" and id = " + modelElectionsOfficer.getId());
			} else if( modelElectionsOfficer.getUserName() != null ){ // userName is unique, so it is sufficient to get a person
                query.append( " and username = '" + modelElectionsOfficer.getUserName() + "'" );
			} else {
                if( modelElectionsOfficer.getPassword() != null ){
                	query.append( " and password = '" + modelElectionsOfficer.getPassword() + "'" );
                }
                if( modelElectionsOfficer.getEmailAddress() != null ) {
                	query.append( " and email = '" + modelElectionsOfficer.getEmailAddress() + "'" );
                }

                if( modelElectionsOfficer.getFirstName() != null ) {
                	query.append( " and firstName = '" + modelElectionsOfficer.getFirstName() + "'" );
                }

                if( modelElectionsOfficer.getLastName() != null ) {
                	query.append( " and lastName = '" + modelElectionsOfficer.getLastName() + "'" );
                }

                if( modelElectionsOfficer.getAddress() != null ) {
                	query.append( " and address = '" + modelElectionsOfficer.getAddress() + "'" );
                }
                
                if( modelElectionsOfficer.getZipCode() != null ) {
                	query.append( " and address = '" + modelElectionsOfficer.getZipCode() + "'" );
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
                
                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    userName = rs.getString( 2 );
                    password = rs.getString( 3 );
                    email = rs.getString( 4 );
                    firstName = rs.getString( 5 );
                    lastName = rs.getString( 6 );
                    address = rs.getString( 7 );
                    zipCode = rs.getString(8);

                    ElectionsOfficer electionsOfficer = objectLayer.createElectionsOfficer
                    			( firstName, lastName, userName, password, email, address, zipCode );
                    electionsOfficer.setId( id );

                    electionsOfficers.add( electionsOfficer );

                }
                
                return electionsOfficers;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "ElectionsOfficerManager.restore: Could not restore persistent Elections Officer object; Root cause: " + e );
        }
        
        // if we get to this point, it's an error
        throw new EVException( "ElectionsOfficerManager.restore: Could not restore persistent Elections Officer objects" );
    }

}
