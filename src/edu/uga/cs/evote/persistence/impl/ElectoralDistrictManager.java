package edu.uga.cs.evote.persistence.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;

public class ElectoralDistrictManager {

	private static final String INSERT_ELECTORALDISTRICT_RECORD = "insert into electoralDistrict (name, zipCode) values (?,?)";
	private static final String UPDATE_ELECTORALDISTRICT_RECORD = "update electoralDistrict set name = ?, zipCode = ? where id = ?";
	private static final String DELETE_ELECTORALDISTRICT_RECORD = "delete from electoralDistrict where id = ?";              

	private ObjectLayer objectLayer = null;
	private Connection   conn = null;
	public ElectoralDistrictManager(Connection conn, ObjectLayer objectLayer) {
		this.conn = conn;
		this.objectLayer = objectLayer;
	}
	public void store(ElectoralDistrict electoralDistrict) throws EVException {
		PreparedStatement    stmt = null;
		int                  inscnt;
		long                 voteRecordId;
		try {
			if( !electoralDistrict.isPersistent() )
				stmt = (PreparedStatement) conn.prepareStatement( INSERT_ELECTORALDISTRICT_RECORD );
			else
				stmt = (PreparedStatement) conn.prepareStatement( UPDATE_ELECTORALDISTRICT_RECORD );
			if( electoralDistrict.getName() != null ) // name is unique unique and non null
				stmt.setString( 1, electoralDistrict.getName());
			else 
				throw new EVException( "ElectoralDistrictManager.save: can't save a electoralDistrict: NAME undefined" );
			
			stmt.setString(2, electoralDistrict.getZipCode());
			
			if( electoralDistrict.isPersistent() )
				stmt.setLong( 3, electoralDistrict.getId() );

			inscnt = stmt.executeUpdate();

			if( !electoralDistrict.isPersistent() ) {
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
								electoralDistrict.setId( voteRecordId ); // set this person's db id (proxy object)
						}
					}
				}
				else
					throw new EVException( "ElectoralDistrictManager.save: failed to save a electoralDistrict 1");
			}
			else {
				if( inscnt < 1 )
					throw new EVException( "ElectoralDistrictManager.save: failed to save a electoralDistrict 2" ); 
			}

		} catch( SQLException e ) {
			e.printStackTrace();
			throw new EVException( "ElectoralDistrictManager.save: failed to save a electoralDistrict: " + e );
		}
	}

	public void delete(ElectoralDistrict electoralDistrict) 
			throws EVException
	{
		PreparedStatement    stmt = null;
		int                  inscnt;

		if( !electoralDistrict.isPersistent() ) 
			throw new EVException( "ElectoralDistrictManager.delete: failed to delete this electoralDistrict");

		try {

			stmt = (PreparedStatement) conn.prepareStatement( DELETE_ELECTORALDISTRICT_RECORD );

			stmt.setLong( 1, electoralDistrict.getId() );

			inscnt = stmt.executeUpdate();

			if( inscnt == 0 ) {
				throw new EVException( "ElectoralDistrictManager.delete: failed to delete this electoralDistrict" );
			}
		}
		catch( SQLException e ) {
			throw new EVException( "ElectoralDistrictManager.delete: failed to delete this electoralDistrict : " + e.getMessage() );
		}
	}

	public List<ElectoralDistrict> restore(ElectoralDistrict modelElectoralDistrict) throws EVException {

		String selectElectoralDistrictSql = "select id, name, zipCode from electoralDistrict ";
		Statement stmt = null;
		StringBuffer query = new StringBuffer(100);
		StringBuffer condition = new StringBuffer(100);
		List<ElectoralDistrict> electoralDistricts = new ArrayList<ElectoralDistrict>();

		condition.setLength(0);

		//form query based on ElectoralDistrict Object
		query.append(selectElectoralDistrictSql);

		if(modelElectoralDistrict != null) {
			if(modelElectoralDistrict.getId() >= 0){
				if( condition.length() > 0 ) {
					condition.append("and");
				}
				condition.append(" id = " + modelElectoralDistrict.getId());
			} else if( modelElectoralDistrict.getName() != null ){ // name is unique, so it is sufficient to get a electoralDistrict
				if( condition.length() > 0 ) {
					condition.append("and");
				}
				condition.append( " name = '" + modelElectoralDistrict.getName() + "'" );	
			} else if(modelElectoralDistrict.getZipCode() != null){
				if( condition.length() > 0 ) {
					condition.append("and");
				}
				condition.append(" zipCode = '" + modelElectoralDistrict.getZipCode() + "'");
			}

			if( condition.length() > 0 ) {
				query.append(  "where" );
				query.append( condition );
			}

		}

		try {

			stmt = conn.createStatement();

			// retrieve the persistent electoralDistrict objects
			//
			if( stmt.execute( query.toString() ) ) { // statement returned a result
				ResultSet rs = stmt.getResultSet();
				long   id;
				String name;
				String zipCode;

				while( rs.next() ) {

					id = rs.getLong( 1 );
					name = rs.getString( 2 );
					zipCode = rs.getString( 3 );

					ElectoralDistrict electoralDistrict = objectLayer.createElectoralDistrict( name, zipCode );
					electoralDistrict.setId( id );

					electoralDistricts.add( electoralDistrict );

				}

				return electoralDistricts;
			}
		}
		catch( Exception e ) {      // just in case...
			throw new EVException( "ElectoralDistrictManager.restore: Could not restore persistent Elections Officer object; Root cause: " + e );
		}

		// if we get to this point, it's an error
		throw new EVException( "ElectoralDistrictManager.restore: Could not restore persistent Elections Officer objects" );
	}//restore

	public List<Voter> restoreVoterBelongsToElectoralDistrict( ElectoralDistrict electoralDistrict ) 
			throws EVException{

		String selectPersonSql = "select v.id, v.userName, v.password, v.email, v.firstName, v.lastName, v.address, v.zipCode, v.voterid, v.age, e.id " +
				"from userPerson v, electoralDistrict e where v.electoralDistrictid = e.id";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		List<Voter>  voters = new ArrayList<Voter>();

		// form the query based on the given Person object instance
		query.append( selectPersonSql );

		if( electoralDistrict != null ) {
			if( electoralDistrict.getId() >= 0 ) // id is unique, so it is sufficient to get a person
				query.append( " and e.id = " + electoralDistrict.getId() );
			else if( electoralDistrict.getName() != null ) // name is unique, so it is sufficient to get a person
				query.append( " and e.name = '" + electoralDistrict.getName() + "'" );
		}

		try {

			stmt = conn.createStatement();

			// retrieve the persistent Club objects
			//
			if( stmt.execute( query.toString() ) ) { // statement returned a result

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

				Voter   voter = null;

				ResultSet rs = stmt.getResultSet();

				while( rs.next() ) {

					id = rs.getLong( 1 );
					userName = rs.getString( 2 );
					password = rs.getString( 3 );
					email = rs.getString( 4 );
					firstName = rs.getString( 5 );
					lastName = rs.getString( 6 );
					address = rs.getString( 7 );
					zipCode = rs.getString( 8 );
					voterid = rs.getString( 9 );
					age = rs.getInt( 10 );

					voter = objectLayer.createVoter(); 
					voter.setId( id );
					voter.setUserName( userName );
					voter.setPassword( password );
					voter.setEmailAddress(email);
					voter.setFirstName(firstName);
					voter.setLastName(lastName);
					voter.setAddress(address);
					voter.setZipCode(zipCode);
					voter.setVoterId(voterid);
					voter.setAge(age);

					voters.add( voter );
				}

				return voters;
			}
		}
		catch( Exception e ) {      // just in case...
			throw new EVException( "ElectoralDistrictManager.restoreVoterBelongsToElectoralDistrict: Could not restore persistent Voter objects; Root cause: " + e );
		}

		throw new EVException( "ElectoralDistrictManager.restoreVoterBelongsToElectoralDistrict: Could not restore persistent Voter objects" );
	}

	public List<Ballot> restoreElectoralDistrictHasBallotBallot(ElectoralDistrict electoralDistrict) 
			throws EVException{

		String selectPersonSql = "select b.id, b.openDate, b.closeDate, e.id " +
				"from ballot b, electoralDistrict e where b.electoralDistrictid = e.id";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		List<Ballot>   ballots = new ArrayList<Ballot>();

		condition.setLength( 0 );

		// form the query based on the given Person object instance
		query.append( selectPersonSql );

		if( electoralDistrict != null ) {
			if( electoralDistrict.getId() >= 0 ) // id is unique, so it is sufficient to get a person
				query.append( " and e.id = " + electoralDistrict.getId() );
			else if( electoralDistrict.getName() != null ) // userName is unique, so it is sufficient to get a person
				query.append( " and e.name = '" + electoralDistrict.getName() + "'" );
		}

		try {

			stmt = conn.createStatement();

			// retrieve the persistent Club objects
			//
			if( stmt.execute( query.toString() ) ) { // statement returned a result

				long   id;
				Date openDate;
				Date closeDate;

				ResultSet rs = stmt.getResultSet();
				Ballot ballot;

				while( rs.next() ) {

					id = rs.getLong( 1 );
					openDate = rs.getDate( 2 );
					closeDate = rs.getDate( 3 );

					ballot = objectLayer.createBallot(); // create a proxy club object
					// and now set its retrieved attributes
					ballot.setId( id );
					ballot.setOpenDate( openDate );
					ballot.setCloseDate( closeDate );

					ballots.add( ballot );
				}

				return ballots;
			}
		}
		catch( Exception e ) {      // just in case...
			throw new EVException( "ElectoralDistrictManager.restoreElectoralDistrictHasBallotBallot: Could not restore persistent Ballot objects; Root cause: " + e );
		}

		throw new EVException( "ElectoralDistrictManager.restoreElectoralDistrictHasBallotBallot: Could not restore persistent Ballot objects" );
	}
}
