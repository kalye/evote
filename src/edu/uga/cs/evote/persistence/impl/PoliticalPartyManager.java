package edu.uga.cs.evote.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;

public class PoliticalPartyManager {
	private static final String INSERT_POLITICAL_PARTY_RECORD = "insert into politicalParty (name) values (?)";
	private static final String UPDATE_POLITICAL_PARTY_RECORD = "update politicalParty set name = ? where id = ?";
    private static final String DELETE_POLITICAL_PARTY_RECORD = "delete from politicalParty where id = ?";              
	private ObjectLayer objectLayer = null;
	private Connection   conn = null;
	
	public PoliticalPartyManager(Connection conn, ObjectLayer objectLayer) {
		this.conn = conn;
		this.objectLayer = objectLayer;
	}
	
	public void store(PoliticalParty politicalParty) throws EVException {
		PreparedStatement    stmt;
		int                  inscnt;
		long                 voteRecordId;
		try {
			if( !politicalParty.isPersistent() )
				stmt = (PreparedStatement) conn.prepareStatement( INSERT_POLITICAL_PARTY_RECORD );
			else
				stmt = (PreparedStatement) conn.prepareStatement( UPDATE_POLITICAL_PARTY_RECORD );
			

			if( politicalParty.getName() != null ) // name is unique unique and non null
				stmt.setString( 1, politicalParty.getName());
			else 
				throw new EVException( "PoliticalPartyManager.save: can't save a politicalParty: NAME undefined" );

			if( politicalParty.isPersistent() )
				stmt.setLong( 2, politicalParty.getId() );
			
			inscnt = stmt.executeUpdate();

			if( !politicalParty.isPersistent() ) {
				if( inscnt >= 1 ) {
					String sql = "select last_insert_id()";
					if( stmt.execute( sql ) ) { // statement returned a result

						// retrieve the result
						ResultSet r = stmt.getResultSet();

						// we will use only the first row!
						//
						while( r.next() ) {

							// retrieve the last insert auto_increment value
							voteRecordId = r.getLong( 1 );
							if( voteRecordId > 0 )
								politicalParty.setId( voteRecordId ); // set this person's db id (proxy object)
						}
					}
				}
				else
					throw new EVException( "PoliticalPartyManager.save: failed to save a politicalParty" );
			}
			else {
				if( inscnt < 1 )
					throw new EVException( "PoliticalPartyManager.save: failed to save a politicalParty" ); 
			}

		} catch( SQLException e ) {
			e.printStackTrace();
			throw new EVException( "PoliticalPartyManager.save: failed to save a politicalParty: " + e );
		}
	}
	
	public void delete(PoliticalParty politicalParty) 
            throws EVException
    {
        PreparedStatement    stmt = null;
        int                  inscnt;
        
        if( !politicalParty.isPersistent() ) 
            throw new EVException( "PoliticalPartyManager.delete: failed to delete this politicalParty");
        
        try {
            
            stmt = (PreparedStatement) conn.prepareStatement( DELETE_POLITICAL_PARTY_RECORD );
            
            stmt.setLong( 1, politicalParty.getId() );
            
            inscnt = stmt.executeUpdate();
            
            if( inscnt == 0 ) {
                throw new EVException( "PoliticalPartyManager.delete: failed to delete this politicalParty" );
            }
        }
        catch( SQLException e ) {
            throw new EVException( "PoliticalPartyManager.delete: failed to delete this politicalParty : " + e.getMessage() );
        }
    }

public List<PoliticalParty> restore(PoliticalParty pp) throws EVException {
		
		String selectPoliticalPartySql = "select id, name from politicalParty ";
		Statement stmt = null;
		StringBuffer query = new StringBuffer(100);
		StringBuffer condition = new StringBuffer(100);
		List<PoliticalParty> polpars = new ArrayList<PoliticalParty>();
		
		condition.setLength(0);
		
		//form query based on voter Object
		query.append(selectPoliticalPartySql);
		if(pp != null) {
			System.out.println("TEAM4: "+ pp.getName());

			if(pp.getId() > 0){
				if(condition.length() > 0)
            		condition.append(" and");
                condition.append( " id = " + pp.getId());
			//} else if( pp.getId() != 0 ){ // id is unique, so it is sufficient to get a politicalParty
			//	if(condition.length() > 0)
            //		condition.append(" and");
             //   condition.append( "id = '" + pp.getId() + "'" );
			}else {
                if( pp.getName() != null ) {
                	if(condition.length() > 0)
                		condition.append(" and");
                    condition.append( " name = '" + pp.getName() + "'" );
                }
            }
			
			if( condition.length() > 0 ) {
                query.append(  "where " );
                query.append( condition );
            }
		}
		
		try {

			System.out.println("TEAM4: " + query.toString());
            stmt = conn.createStatement();

            // retrieve the persistent Person objects
            //
            if( stmt.execute( query.toString() ) ) { // statement returned a result
                ResultSet rs = stmt.getResultSet();
                long   id;
                String name;
                
                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    name = rs.getString( 2 );
                    

                    PoliticalParty polpar = objectLayer.createPoliticalParty(name);
                    polpar.setId( id );
                    polpar.setName(name);

                    polpars.add(polpar);

                }
                System.out.println("TEAM4: size==" + polpars.size());
                return polpars;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "PoliticalParty.restore: Could not restore persistent Political Party object; Root cause: " + e );
        }
        
        // if we get to this point, it's an error
        throw new EVException( "PoliticalParty.restore: Could not restore persistent Political Party objects" );
    }

public List<Candidate> restoreCandidateIsMemberOfPoliticalParty(PoliticalParty politicalParty) 
        throws EVException{

    String selectPersonSql = "select c.id, c.name, p.id from candidate c, politicalParty p" +
                             " where c.partyid = p.id";
    Statement    stmt = null;
    StringBuffer query = new StringBuffer( 100 );
    StringBuffer condition = new StringBuffer( 100 );
    List<Candidate>   candidates = new ArrayList<Candidate>();

    condition.setLength( 0 );
    
    // form the query based on the given Person object instance
    query.append( selectPersonSql );
    
    if( politicalParty != null ) {
        if( politicalParty.getId() >= 0 ) // id is unique, so it is sufficient to get a person
            query.append( " and p.id = " + politicalParty.getId() );
        else if( politicalParty.getName() != null ) // userName is unique, so it is sufficient to get a person
            query.append( " and p.name = '" + politicalParty.getName() + "'" );
    }
            
    try {

        stmt = conn.createStatement();

        // retrieve the persistent Club objects
        //
        if( stmt.execute( query.toString() ) ) { // statement returned a result
                      
            long   id;
            String name;
           
            ResultSet rs = stmt.getResultSet();
            Candidate candidate = null;
            
            while( rs.next() ) {
                
                id = rs.getLong( 1 );
                name = rs.getString( 2 );
                
                candidate = objectLayer.createCandidate(); // create a proxy club object
                // and now set its retrieved attributes
                candidate.setId( id );
                candidate.setName( name );
                
                candidates.add( candidate );
            }
            
            return candidates;
        }
    }
    catch( Exception e ) {      // just in case...
        throw new EVException( "PoliticalPartyManager.restoreCandidateIsMemberOfPoliticalParty: Could not restore persistent Candidate objects; Root cause: " + e );
    }

    throw new EVException( "PoliticalPartyManager.restoreCandidateIsMemberOfPoliticalParty: Could not restore persistent Candidate objects" );
}
}
