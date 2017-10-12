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
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;

public class CandidateManager {

	private ObjectLayer objectLayer = null;
    private Connection  conn = null;
	
	public CandidateManager(Connection conn, ObjectLayer objectLayer) {
		// TODO Auto-generated constructor stub
		this.conn = conn;
		this.objectLayer = objectLayer;
	}

	public void store(Candidate candidate) throws EVException{
		// TODO Auto-generated method stub
		
		String               insertCandidateSql = "insert into candidate( name, voteCount, electionid, partyid ) values ( ?, ?, ?, ? )";              
        String               updateCandidateSql = "update candidate set name = ?, voteCount = ?, electionid = ?, partyid = ? where id = ?";              
        PreparedStatement    stmt;
        int                  inscnt;
        long                 candidateId;
		
        try {
        	//create Candidate in database
        	if(!candidate.isPersistent()) {
        		stmt = (PreparedStatement) conn.prepareStatement(insertCandidateSql);
        	} else {
        		stmt = (PreparedStatement) conn.prepareStatement(updateCandidateSql);
        	}
        	
        	//set name
        	if( candidate.getName() != null){
        		stmt.setString(1, candidate.getName());
        	} else{
        		throw new EVException("CandidateManager.save: can't save an Elections Candidate: userName undefined");
        	}
        	
        	//edit voteCount in database
        	stmt.setInt(2, candidate.getVoteCount());
        	
        	//edit password in database
        	if (candidate.getElection() != null) {
        		stmt.setLong(3, candidate.getElection().getId());
        		System.out.println("Election for Candidate: " + candidate.getElection().getId());
        	} else
        		throw new EVException("CandidateManager.save: can't save an Elections Candidate: password undefined");
        	
        	//edit email
        	if(candidate.getPoliticalParty() != null)
        		stmt.setLong(4, candidate.getPoliticalParty().getId());
        	else
        		stmt.setLong(4, 0);
        	
        	//id
        	if(candidate.isPersistent())
        		stmt.setLong(5, candidate.getId());
        	
        	inscnt = stmt.executeUpdate();
        	
        	if( !candidate.isPersistent() ) {
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
                            candidateId = r.getLong( 1 );
                            if( candidateId > 0 )
                                candidate.setId( candidateId ); // set this person's db id (proxy object)
                        }
                    }
                }
            }
            else {
                if( inscnt < 1 )
                    throw new EVException( "CandidateManager.save: failed to save an Elections Candidate" ); 
            }
        	
        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new EVException("CandidateManager.save: failed to save an Elections Candidate: " + e);
        }

	} //store
	
	public void delete( Candidate candidate ) 
            throws EVException
    {
        String               deletePersonSql = "delete from candidate where id = ?";              
        PreparedStatement    stmt = null;
        int                  inscnt;
        
        // form the query based on the given Person object instance
        if( !candidate.isPersistent() ) // is the Person object persistent?  If not, nothing to actually delete
            throw new EVException( "CandidateManager.delete: failed to delete this Election Candidate");
        
        try {
            
            stmt = (PreparedStatement) conn.prepareStatement( deletePersonSql );
            
            stmt.setLong( 1, candidate.getId() );
            
            inscnt = stmt.executeUpdate();
            
            if( inscnt == 0 ) {
                throw new EVException( "CandidateManager.delete: failed to delete this Election Candidate" );
            }
        }
        catch( SQLException e ) {
            throw new EVException( "CandidateManager.delete: failed to delete this Election Candidate: " + e.getMessage() );
        }
    }
	
	public List<Candidate> restore(Candidate modelCandidate) throws EVException {
		
		String selectCandidateSql = "select id, name, voteCount from candidate ";
		Statement stmt = null;
		StringBuffer query = new StringBuffer(100);
		StringBuffer condition = new StringBuffer(100);
		List<Candidate> candidates = new ArrayList<Candidate>();
		
		condition.setLength(0);
		
		//form query based on Candidate Object
		query.append(selectCandidateSql);
		
		if(modelCandidate != null) {
			if(modelCandidate.getId() >= 0){
				if( condition.length() > 0 )
                    condition.append( " and" );
				condition.append(" id = " + modelCandidate.getId());
			} else if( modelCandidate.getName() != null ){ // userName is unique, so it is sufficient to get a person
				if( condition.length() > 0 )
                    condition.append( " and" );
				condition.append( " name = '" + modelCandidate.getName() + "'" );
			} else {
                if( modelCandidate.getVoteCount() != 0 )
                	if(condition.length() > 0)
                		condition.append(" and");
                    condition.append( " voteCount = " + modelCandidate.getVoteCount());

                if( modelCandidate.getElection() != null ) {
                    if( condition.length() > 0 )
                        condition.append( " and" );
                    condition.append( " electionid = " + modelCandidate.getElection().getId() );
                }

                if( modelCandidate.getPoliticalParty() != null ) {
                    if( condition.length() > 0 )
                        condition.append( " and" );
                    condition.append( " partyid = " + modelCandidate.getPoliticalParty().getId());
                }

            }
			if( condition.length() > 0 ) {
            	query.append("where ");
                query.append( condition );
            }
		}
		
		try {

            stmt = conn.createStatement();

            // retrieve the persistent Person objects
            //
            if( stmt.execute( query.toString() ) ) { // statement returned a result
                ResultSet rs = stmt.getResultSet();
                long   id;
                String name;
                int voteCount;
                
                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    name = rs.getString( 2 );
                    voteCount = rs.getInt( 3 );

                    Candidate candidate = objectLayer.createCandidate( name, null, null );
                    candidate.setId( id );
                    candidate.setVoteCount(voteCount);
                    
                    candidates.add( candidate );

                }
                
                return candidates;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "CandidateManager.restore: Could not restore persistent Candidate object; Root cause: " + e );
        }
        
        // if we get to this point, it's an error
        throw new EVException( "CandidateManager.restore: Could not restore persistent Candidate objects" );
    }

	public PoliticalParty restoreCandidateIsMemberOfPoliticalParty(Candidate candidate) 
            throws EVException
    {
        String       selectPersonSql = "select p.id, p.name, c.id from politicalParty p, candidate c where p.id = c.partyid";              
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );

        condition.setLength( 0 );
        
        // form the query based on the given Person object instance
        query.append( selectPersonSql );
        
        if( candidate != null ) {
            if( candidate.getId() >= 0 ) // id is unique, so it is sufficient to get a person
                query.append( " and c.id = " + candidate.getId() );
            else if( candidate.getName() != null ) // userName is unique, so it is sufficient to get a person
                query.append( " and c.name = '" + candidate.getName() + "'" );
        }
                
        try {

            stmt = conn.createStatement();

            // retrieve the persistent Person object
            //
            if( stmt.execute( query.toString() ) ) { // statement returned a result
                ResultSet rs = stmt.getResultSet();
                
                long   id;
                String name;
                PoliticalParty party = null;
                
                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    name = rs.getString( 2 );

                    party = objectLayer.createPoliticalParty( name );
                    party.setId( id );
                }
                
                return party;
            }
            else
                return null;
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "CandidateManager.restoreCandidateIsMemberOfPoliticalParty: Could not restore persistent PoliticalParty object; Root cause: " + e );
        }
    }
	
	public Election restoreCandidateIsCandidateInElection(Candidate candidate) 
            throws EVException
    {
        String       selectPersonSql = "select e.id, e.voteCount, e.office, e.isPartisan, c.id from ballotItem e, candidate c where e.id = c.electionid";              
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );

        condition.setLength( 0 );
        
        // form the query based on the given Person object instance
        query.append( selectPersonSql );
        if( candidate != null ) {
            if( candidate.getId() >= 0 ) // id is unique, so it is sufficient to get a candidate
                query.append( " and c.id = " + candidate.getId() );
            else if( candidate.getName() != null ) // Name is unique, so it is sufficient to get a candidate
                query.append( " and c.name = '" + candidate.getName() + "'" );
        }
                
        try {

            stmt = conn.createStatement();

            // retrieve the persistent Election object
            //
            if( stmt.execute( query.toString() ) ) { // statement returned a result
                ResultSet rs = stmt.getResultSet();
                
                long   id;
                int voteCount;
                String office;
                boolean isPartisan;
                Election election = null;
                
                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    voteCount = rs.getInt( 2 );
                    office = rs.getString( 3 );
                    isPartisan = rs.getBoolean( 4 );

                    election = objectLayer.createElection( office, isPartisan, null );
                    election.setId( id );
                    election.setVoteCount(voteCount);
                }
                
                return election;
            }
            else
                return null;
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "CandidateManager.restoreCandidateIsCandidateInElection: Could not restore persistent Election object; Root cause: " + e );
        }
    }
}

