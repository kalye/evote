package edu.uga.cs.evote.logic.impl;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;

/**
 * @author Krys J. Kochut
 *
 */
public class LogicLayerImpl 
    implements LogicLayer
{
    private ObjectLayer objectLayer = null;

    public LogicLayerImpl( Connection conn ) throws EVException
    {
        PersistenceLayer persistenceLayer = new PersistenceLayerImpl( conn, objectLayer );
        objectLayer = new ObjectLayerImpl(persistenceLayer);
        persistenceLayer.setObjectLayer(objectLayer);
        System.out.println( "LogicLayerImpl.LogicLayerImpl(conn): initialized" );
    }
    
    public LogicLayerImpl( ObjectLayer objectLayer )
    {
        this.objectLayer = objectLayer;
        System.out.println( "LogicLayerImpl.LogicLayerImpl(objectLayer): initialized" );
    }

	@Override
	public List<Ballot> findAllBallots() throws EVException {
		BallotCtrl ballotCtrl = new BallotCtrl( objectLayer );
        return ballotCtrl.findAllBallots();
	}

	@Override
	public List<BallotItem> findAllBallotItems() throws EVException {
		BallotItemCtrl ballotItemCtrl = new BallotItemCtrl( objectLayer );
        return ballotItemCtrl.findAllBallotItems();
	}

	@Override
	public List<Candidate> findAllCandidates() throws EVException {
		CandidateCtrl candidateCtrl = new CandidateCtrl( objectLayer );
        return candidateCtrl.findAllCandidates();
	}

	@Override
	public List<Election> findAllElections() throws EVException {
		ElectionCtrl ctrlFindAllElections = new ElectionCtrl( objectLayer );
        return ctrlFindAllElections.findAllElections();
	}

	@Override
	public List<ElectionsOfficer> findAllElectionsOfficers() throws EVException {
		ElectionsOfficerCtrl ctrlFindAllElectionsOfficers = new ElectionsOfficerCtrl( objectLayer );
        return ctrlFindAllElectionsOfficers.findAllElectionsOfficers();
	}

	@Override
	public List<ElectoralDistrict> findAllElectoralDistricts() throws EVException {
		ElectoralDistrictCtrl ctrlFindAllElectoralDistricts = new ElectoralDistrictCtrl( objectLayer );
        return ctrlFindAllElectoralDistricts.findAllElectoralDistricts();
	}

	@Override
	public List<Issue> findAllIssues() throws EVException {
		BallotItemCtrl ctrlFindAllIssues = new BallotItemCtrl( objectLayer );
        return ctrlFindAllIssues.findAllIssues();
	}

	@Override
	public List<PoliticalParty> findAllPoliticalParties() throws EVException {
		PoliticalPartyCtrl ctrlFindAllPoliticalParties = new PoliticalPartyCtrl( objectLayer );
        return ctrlFindAllPoliticalParties.findAllPoliticalParties();
	}

	@Override
	public List<Voter> findAllVoters() throws EVException {
		VoterCtrl ctrlFindAllVoters = new VoterCtrl( objectLayer );
        return ctrlFindAllVoters.findAllVoters();
	}

	@Override
	public List<VoteRecord> findAllVoteRecords() throws EVException {
		VoteRecordCtrl ctrlFindAllVoteRecords = new VoteRecordCtrl( objectLayer );
        return ctrlFindAllVoteRecords.findAllVoteRecords();
	}

	@Override
	public long addToBallot(BallotItem ballotItem, Ballot ballot) throws EVException {
		BallotCtrl ctrlAddToBallot = new BallotCtrl( objectLayer );
        return ctrlAddToBallot.addToBallot( ballotItem, ballot );
	}

	@Override
	public long addToElection(Candidate candidate, Election election) throws EVException {
		ElectionCtrl ctrlAddToElection = new ElectionCtrl( objectLayer );
        return ctrlAddToElection.addToElection( candidate, election );
	}

	@Override
	public long addToElectoralDistrict(Voter voter, ElectoralDistrict electoralDistrict) throws EVException {
		ElectoralDistrictCtrl ctrlAddToElectoralDistrict = new ElectoralDistrictCtrl( objectLayer );
        return ctrlAddToElectoralDistrict.addToElectoralDistrict( voter, electoralDistrict );
	}

	@Override
	public long addToPoliticalParty(Candidate candidate, PoliticalParty politicalParty) throws EVException {
		PoliticalPartyCtrl ctrlAddToPoliticalParty = new PoliticalPartyCtrl( objectLayer );
        return ctrlAddToPoliticalParty.addToPoliticalParty( candidate, politicalParty );
	}

	@Override
	public long createBallot(Date openDate, Date closeDate, ElectoralDistrict electoralDistrict) throws EVException {
		BallotCtrl ctrlCreateBallot = new BallotCtrl( objectLayer );
        return ctrlCreateBallot.createBallot( openDate, closeDate, electoralDistrict );
	}

	@Override
	public long createCandidate(String name, Election election, PoliticalParty politicalParty) throws EVException {
		CandidateCtrl ctrlCreateCandidate = new CandidateCtrl( objectLayer );
        return ctrlCreateCandidate.createCandidate( name, election, politicalParty );
	}

	@Override
	public long createElection(String office, boolean isPartisan, Ballot ballot) throws EVException {
		ElectionCtrl ctrlCreateElection = new ElectionCtrl( objectLayer );
        return ctrlCreateElection.createElection( office, isPartisan, ballot );
	}

	@Override
	public long createElectionsOfficer(String firstName, String lastName, String userName, String password,
			String emailAddress, String address, String zipCode) throws EVException {
		ElectionsOfficerCtrl ctrlCreateElectionsOfficer = new ElectionsOfficerCtrl( objectLayer );
        return ctrlCreateElectionsOfficer.createElectionsOfficer( 	firstName, lastName, userName,
        															password, emailAddress, address, zipCode);
	}

	@Override
	public long createElectoralDistrict(String name, String zipCode) throws EVException {
		ElectoralDistrictCtrl ctrlCreateElectoralDistrict = new ElectoralDistrictCtrl( objectLayer );
        return ctrlCreateElectoralDistrict.createElectoralDistrict( name, zipCode );
	}

	@Override
	public long createIssue(String question, Ballot ballot) throws EVException {
		BallotItemCtrl ctrlCreateIssue = new BallotItemCtrl( objectLayer );
        return ctrlCreateIssue.createIssue( question, ballot );
	}

	@Override
	public long createPoliticalParty(String name) throws EVException {
		PoliticalPartyCtrl ctrlCreatePoliticalParty = new PoliticalPartyCtrl( objectLayer );
        return ctrlCreatePoliticalParty.createPoliticalParty( name );
	}

	@Override
	public long createVoter(String firstName, String lastName, String userName, String password, String emailAddress,
			String address, String zipCode, String voterId, int age, ElectoralDistrict electoralDistrict) throws EVException {
		VoterCtrl ctrlCreateVoter = new VoterCtrl( objectLayer );
        return ctrlCreateVoter.createVoter( firstName, lastName, userName, password, emailAddress, 
        									address, zipCode, voterId, age, electoralDistrict);
	}

	@Override
	public long createVoteRecord(Date date, Voter voter, Ballot ballot) throws EVException {
		VoteRecordCtrl ctrlCreateVoteRecord = new VoteRecordCtrl( objectLayer );
        return ctrlCreateVoteRecord.CreateVoteRecord( date, voter, ballot );
	}

	@Override
	public List<Ballot> findBallots(ElectoralDistrict electoralDistrict) throws EVException {
		BallotCtrl ctrlFindBallots = new BallotCtrl( objectLayer );
        return ctrlFindBallots.findBallots( electoralDistrict );
	}

	@Override
	public List<BallotItem> findBallotItems(Ballot ballot) throws EVException {
		BallotItemCtrl ctrlFindBallotItems = new BallotItemCtrl( objectLayer );
        return ctrlFindBallotItems.findBallotItems( ballot );
	}

	@Override
	public List<Candidate> findCandidates(Election election) throws EVException {
		CandidateCtrl ctrlFindCandidates = new CandidateCtrl( objectLayer );
        return ctrlFindCandidates.findCandidates( election );
	}

	@Override
	public List<Candidate> findCandidates(PoliticalParty politicalParty) throws EVException {
		CandidateCtrl ctrlFindCandidates = new CandidateCtrl( objectLayer );
        return ctrlFindCandidates.findCandidates( politicalParty );
	}

	@Override
	public List<Election> findElections(Ballot ballot) throws EVException {
		ElectionCtrl ctrlFindElections = new ElectionCtrl( objectLayer );
        return ctrlFindElections.findElections( ballot );
	}

	@Override
	public List<Issue> findIssues(Ballot ballot) throws EVException {
		BallotItemCtrl ctrlFindIssues = new BallotItemCtrl( objectLayer );
        return ctrlFindIssues.findIssues( ballot );
	}

	@Override
	public List<Voter> findVoters(ElectoralDistrict electoralDistrict) throws EVException {
		VoterCtrl ctrlFindVoters = new VoterCtrl( objectLayer );
        return ctrlFindVoters.findVoters( electoralDistrict );
	}

	@Override
	public List<VoteRecord> findVoteRecords(Ballot ballot) throws EVException {
		VoteRecordCtrl ctrlFindVoteRecords = new VoteRecordCtrl( objectLayer );
        return ctrlFindVoteRecords.findVoteRecords( ballot );
	}

	@Override
	public List<VoteRecord> findVoteRecords(Voter voter) throws EVException {
		VoteRecordCtrl ctrlFindVoteRecords = new VoteRecordCtrl( objectLayer );
        return ctrlFindVoteRecords.findVoteRecords( voter );
	}

	@Override
	public String login(Session session, String userName, String password) throws EVException {
		LoginCtrl ctrlVerifyUser = new LoginCtrl( objectLayer );
        return ctrlVerifyUser.login( session, userName, password );
	}

	@Override
	public void logout(String ssid) throws EVException {
		SessionManager.logout( ssid );
	}
}
