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
import edu.uga.cs.evote.object.ObjectLayer;

public class ElectionManager {

	private ObjectLayer objectLayer = null;
    private Connection  conn = null;
	
	public ElectionManager(Connection conn, ObjectLayer objectLayer) {
		// TODO Auto-generated constructor stub
		this.conn = conn;
		this.objectLayer = objectLayer;
	}

	public void store(Election election) throws EVException{
		// ADD POSITION!
		
		String               insertElectionSql = "insert into ballotItem ( kind, voteCount, ballotid, office, isPartisan ) values ( ?, ?, ?, ?, ? )";              
        String               updateElectionSql = "update ballotItem set kind = ?, voteCount = ?, ballotid = ?, office = ?, isPartisan = ? where id = ?";              
        PreparedStatement    stmt;
        int                  inscnt;
        long                 electionId;
		
        try {
        	if(!election.isPersistent()) {
        		stmt = (PreparedStatement) conn.prepareStatement(insertElectionSql);
        	} else {
        		stmt = (PreparedStatement) conn.prepareStatement(updateElectionSql);
        	}
        	
        	//set kind
        	stmt.setString(1, "election");
        	
        	//edit voteCount in database
        	if ( election.getVoteCount() >= 0)
        		stmt.setInt(2, election.getVoteCount());
        	else
        		throw new EVException("ElectionManager.save: can't save an Elections Officer: voteCount undefined");
        	
        	//edit ballotid in database
        	if (election.getBallot() != null)
        		stmt.setLong(3, election.getBallot().getId());
        	else
        		throw new EVException("ElectionManager.save: can't save an Elections Officer: ballotid undefined");
        	
        	//edit office
        	if(election.getOffice() != null)
        		stmt.setString(4, election.getOffice());
        	else
        		throw new EVException("ElectionManager.save: can't save an Elections Officer: office  undefined");
        	
        	//edit isPartisan
        		stmt.setBoolean(5, election.getIsPartisan());

        	//stmt.setInt(6, election.getPosition());

        	//id
        	if(election.isPersistent())
        		stmt.setLong(6, election.getId());
        	
        	inscnt = stmt.executeUpdate();
        	
        	if( !election.isPersistent() ) {
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
                            electionId = r.getLong( 1 );
                            if( electionId > 0 )
                                election.setId( electionId ); // set this person's db id (proxy object)
                        }
                    }
                }
            }
            else {
                if( inscnt < 1 )
                    throw new EVException( "ElectionManager.save: failed to save an Elections Officer" ); 
            }
        	
        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new EVException("ElectionManager.save: failed to save an Elections Officer: " + e);
        }

	} //store
	
	public void delete( Election election ) 
            throws EVException
    {
        String               deleteBallotItemSql = "delete from ballotItem where id = ?";              
        PreparedStatement    stmt = null;
        int                  inscnt;
        
        // form the query based on the given Person object instance
        if( !election.isPersistent() ) // is the Person object persistent?  If not, nothing to actually delete
            throw new EVException( "ElectionManager.delete: failed to delete this Election Officer");
        
        try {
            
            //DELETE t1, t2 FROM t1, t2 WHERE t1.id = t2.id;
            //DELETE FROM t1, t2 USING t1, t2 WHERE t1.id = t2.id;
            stmt = (PreparedStatement) conn.prepareStatement( deleteBallotItemSql );
            
            stmt.setLong( 1, election.getId() );
            
            inscnt = stmt.executeUpdate();
            
            if( inscnt == 0 ) {
                throw new EVException( "ElectionManager.delete: failed to delete this Election Officer" );
            }
        }
        catch( SQLException e ) {
            throw new EVException( "ElectionManager.delete: failed to delete this Election Officer: " + e.getMessage() );
        }
    }
	
	public List<Election> restore(Election modelElection) throws EVException {

		// add position!!
		String selectElectionSql = "select id, voteCount, office, isPartisan from ballotItem where kind = 'election'";

		Statement stmt = null;
		StringBuffer query = new StringBuffer(200);
		StringBuffer condition = new StringBuffer(200);
		List<Election> elections = new ArrayList<Election>();
		
		condition.setLength(0);
		
		//form query based on Election Object
		query.append(selectElectionSql);
		
		if(modelElection != null) {
			if(modelElection.getId() >= 0){
				condition.append(" and");
				condition.append(" id = '" + modelElection.getId() + "'");
           
			} else {
                if( modelElection.getVoteCount() != 0 ) {
                	condition.append(" and");
                    condition.append( " voteCount = " + modelElection.getVoteCount());
                }
                if( modelElection.getOffice() != null ) {
                    condition.append( " and" );
                    condition.append( " office = '" + modelElection.getOffice() + "'" );
                }

                if( modelElection.getIsPartisan() != false ) {
                    condition.append( " and" );
                    condition.append( " isPartisan = " + modelElection.getIsPartisan());
                }

               /* if(modelElection.getPosition() != 0){
                	condition.append(" and");
                	condition.append(" position = " + modelElection.getPosition());
                }*/

                if( modelElection.getBallot() != null ) {
                    condition.append( " and" );
                    condition.append( " ballotid = " + modelElection.getBallot().getId() );
                }
                
            }
			if( condition.length() > 0 ) {
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
                int voteCount;
                String office;
                boolean isPartisan;

                //int position;
                
                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    voteCount = rs.getInt( 2 );
                    office = rs.getString( 3 );
                    isPartisan = rs.getBoolean( 4 );

                    //position = rs.getInt( 5 );

                    Election election = objectLayer.createElection(office, isPartisan, null );
                    election.setId( id );
                    election.setVoteCount(voteCount);
                    //election.setPosition(position);
                    elections.add( election );

                }
                
                return elections;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "ElectionManager.restore: Could not restore persistent Elections Officer object; Root cause: " + e );
        }
        
        // if we get to this point, it's an error
        throw new EVException( "ElectionManager.restore: Could not restore persistent Elections Officer objects" );
    }

	public List<Candidate> restoreCandidateIsCandidateInElection(Election election) 
	        throws EVException{
		
		//add position!!!
	    String selectPersonSql = "select c.id, c.name, c.voteCount from candidate c, ballotItem e where c.electionid = e.id";
	    Statement    stmt = null;
	    StringBuffer query = new StringBuffer( 100 );
	    List<Candidate>   candidates = new ArrayList<Candidate>();
	    
	    // form the query based on the given Person object instance
	    query.append( selectPersonSql );
	    
	    if( election != null ) {
	        if( election.getId() >= 0 ) // id is unique, so it is sufficient to get a person
	            query.append( " and e.id = " + election.getId() );
	    }
	            
	    try {

	        stmt = conn.createStatement();

	        // retrieve the persistent Club objects
	        //
	        if( stmt.execute( query.toString() ) ) { // statement returned a result
	                      
	            long   id;
	            String name;
	            int voteCount;
	           // int position;
	            
	            ResultSet rs = stmt.getResultSet();
	            Candidate candidate = null;
	            
	            while( rs.next() ) {
	                
	                id = rs.getLong( 1 );
	                name = rs.getString( 2 );
	                voteCount = rs.getInt( 3 );
	                //position = rs.getInt( 4 );
	                
	                candidate = objectLayer.createCandidate(); // create a proxy club object
	                // and now set its retrieved attributes
	                candidate.setId( id );
	                candidate.setName( name );
	                candidate.setVoteCount(voteCount);
	                //candidate.setPosition( position );

	                candidates.add( candidate );
	            }
	            
	            return candidates;
	        }
	    }
	    catch( Exception e ) {      // just in case...
	        throw new EVException( "ElectionManager.restoreCandidateIsCandidateInElection: Could not restore persistent Candidate objects; Root cause: " + e );
	    }

	    throw new EVException( "ElectionManager.restoreCandidateIsCandidateInElection: Could not restore persistent Candidate objects" );
	}
}


