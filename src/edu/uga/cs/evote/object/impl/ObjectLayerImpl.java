package edu.uga.cs.evote.object.impl;

import java.util.Date;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.entity.impl.BallotImpl;
import edu.uga.cs.evote.entity.impl.BallotItemImpl;
import edu.uga.cs.evote.entity.impl.CandidateImpl;
import edu.uga.cs.evote.entity.impl.ElectionImpl;
import edu.uga.cs.evote.entity.impl.ElectionsOfficerImpl;
import edu.uga.cs.evote.entity.impl.ElectoralDistrictImpl;
import edu.uga.cs.evote.entity.impl.IssueImpl;
import edu.uga.cs.evote.entity.impl.PoliticalPartyImpl;
import edu.uga.cs.evote.entity.impl.VoteRecordImpl;
import edu.uga.cs.evote.entity.impl.VoterImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.persistence.PersistenceLayer;

public class ObjectLayerImpl implements ObjectLayer {

	PersistenceLayer persistence = null;
    
    public ObjectLayerImpl()
    {
        this.persistence = null;
    }
    
    public ObjectLayerImpl( PersistenceLayer persistence ) 
    {
        this.persistence = persistence;
        BallotItemImpl.setPersistenceLayer(persistence);
        
    }
	
	@Override
	public ElectionsOfficer createElectionsOfficer(String firstName, String lastName, String userName, String password,
			String emailAddress, String address, String zipCode) throws EVException {
		
		ElectionsOfficerImpl eo = new ElectionsOfficerImpl(firstName, lastName, userName, password,
				emailAddress, address, zipCode);
        return eo;
	}

	@Override
	public ElectionsOfficer createElectionsOfficer() {
		
		ElectionsOfficerImpl eo = new ElectionsOfficerImpl();
        return eo;
	}

	@Override
	public List<ElectionsOfficer> findElectionsOfficer(ElectionsOfficer modelElectionsOfficer) throws EVException {
		
		return persistence.restoreElectionsOfficer(modelElectionsOfficer);
	}

	@Override
	public void storeElectionsOfficer(ElectionsOfficer electionsOfficer) throws EVException {

		persistence.storeElectionsOfficer(electionsOfficer);
	}

	@Override
	public void deleteElectionsOfficer(ElectionsOfficer electionsOfficer) throws EVException {
		
		persistence.deleteElectionsOfficer(electionsOfficer);
	}

	@Override
	public Voter createVoter(String firstName, String lastName, String userName, String password, String emailAddress,
			String address, String zipCode, int age) throws EVException {
		
		VoterImpl v = new VoterImpl(firstName, lastName, userName, password, emailAddress, address, zipCode, null, age, null);
		return v;
	}

	@Override
	public Voter createVoter() {
		
		VoterImpl v = new VoterImpl();
		return v;
	}

	@Override
	public List<Voter> findVoter(Voter modelVoter) throws EVException {
		
		return persistence.restoreVoter(modelVoter);
	}

	@Override
	public void storeVoter(Voter voter) throws EVException {
		
		persistence.storeVoter(voter);
	}

	@Override
	public void deleteVoter(Voter voter) throws EVException {
		
		persistence.deleteVoter(voter);
	}

	@Override
	public PoliticalParty createPoliticalParty(String name) throws EVException {
		
		PoliticalPartyImpl pp = new PoliticalPartyImpl(name);
		return pp;
	}

	@Override
	public PoliticalParty createPoliticalParty() {
		
		PoliticalPartyImpl pp = new PoliticalPartyImpl();
		return pp;
	}

	@Override
	public List<PoliticalParty> findPoliticalParty(PoliticalParty modelPoliticalParty) throws EVException {
		
		return persistence.restorePoliticalParty(modelPoliticalParty);
	}

	@Override
	public void storePoliticalParty(PoliticalParty politicalParty) throws EVException {
		
		persistence.storePoliticalParty(politicalParty);
	}

	@Override
	public void deletePoliticalParty(PoliticalParty politicalParty) throws EVException {
		
		persistence.deletePoliticalParty(politicalParty);
	}

	@Override
	public ElectoralDistrict createElectoralDistrict(String name, String zipCode) throws EVException {
		
		ElectoralDistrictImpl ed = new ElectoralDistrictImpl(name, zipCode);
		return ed;
	}

	@Override
	public ElectoralDistrict createElectoralDistrict() {
		
		ElectoralDistrictImpl ed = new ElectoralDistrictImpl();
		return ed;
	}

	@Override
	public List<ElectoralDistrict> findElectoralDistrict(ElectoralDistrict modelElectoralDistrict) throws EVException {
		
		return persistence.restoreElectoralDistrict(modelElectoralDistrict);
	}

	@Override
	public void storeElectoralDistrict(ElectoralDistrict electoralDistrict) throws EVException {
		
		persistence.storeElectoralDistrict(electoralDistrict);
	}

	@Override
	public void deleteElectoralDistrict(ElectoralDistrict electoralDistrict) throws EVException {
		
		persistence.deleteElectoralDistrict(electoralDistrict);
	}

	@Override
	public Ballot createBallot(Date openDate, Date closeDate, boolean approved, ElectoralDistrict electoralDistrict)
			throws EVException {
		
		BallotImpl b = new BallotImpl(openDate, closeDate, electoralDistrict);
		b.setIsApproved(approved);
		return b;
	}

	@Override
	public Ballot createBallot() {
		
		BallotImpl b = new BallotImpl();
		return b;
	}

	@Override
	public List<Ballot> findBallot(Ballot modelBallot) throws EVException {
		
		return persistence.restoreBallot(modelBallot);
	}

	@Override
	public void storeBallot(Ballot ballot) throws EVException {
		
		persistence.storeBallot(ballot);
	}

	@Override
	public void deleteBallot(Ballot ballot) throws EVException {
		
		persistence.deleteBallot(ballot);
	}

	@Override
	public Candidate createCandidate(String name, PoliticalParty politicalParty, Election election) throws EVException {
		
		CandidateImpl c = new CandidateImpl(name, election, politicalParty);
		return c;
	}

	@Override
	public Candidate createCandidate() {
		
		CandidateImpl c = new CandidateImpl();
		return c;
	}

	@Override
	public List<Candidate> findCandidate(Candidate modelCandidate) throws EVException {
		
		return persistence.restoreCandidate(modelCandidate);
	}

	@Override
	public void storeCandidate(Candidate candidate) throws EVException {
		
		persistence.storeCandidate(candidate);
	}

	@Override
	public void deleteCandidate(Candidate candidate) throws EVException {
		
		persistence.deleteCandidate(candidate);
	}

	@Override
	public Issue createIssue(String question, Ballot ballot) throws EVException {
		
		IssueImpl i = new IssueImpl(question, ballot);
		return i;
	}

	@Override
	public Issue createIssue() {
		
		IssueImpl i = new IssueImpl();
		return i;
	}

	@Override
	public List<Issue> findIssue(Issue modelIssue) throws EVException {
		
		return persistence.restoreIssue(modelIssue);
	}

	@Override
	public void storeIssue(Issue issue) throws EVException {
		
		persistence.storeIssue(issue);
	}

	@Override
	public void deleteIssue(Issue issue) throws EVException {
		
		persistence.deleteIssue(issue);
	}

	public Election createElection(String office, boolean isPartisan, Ballot ballot) throws EVException {
		
		ElectionImpl e = new ElectionImpl(office, isPartisan, ballot);
		return e;
	}

	@Override
	public Election createElection() {
		
		ElectionImpl e = new ElectionImpl();
		return e;
	}

	@Override
	public List<Election> findElection(Election modelElection) throws EVException {
		
		return persistence.restoreElection(modelElection);
	}

	@Override
	public void storeElection(Election election) throws EVException {
		
		persistence.storeElection(election);
	}

	@Override
	public void deleteElection(Election election) throws EVException {
		
		persistence.deleteElection(election);
	}

	@Override
	public VoteRecord createVoteRecord(Ballot ballot, Voter voter, Date date) throws EVException {
		
		VoteRecordImpl vr = new VoteRecordImpl(date, voter, ballot);
		return vr;
	}

	@Override
	public VoteRecord createVoteRecord() {
		
		VoteRecordImpl vr = new VoteRecordImpl();
		return vr;
	}

	@Override
	public List<VoteRecord> findVoteRecord(VoteRecord modelVoteRecord) throws EVException {
		
		return persistence.restoreVoteRecord(modelVoteRecord);
	}

	@Override
	public void storeVoteRecord(VoteRecord voteRecord) throws EVException {
		
		persistence.storeVoteRecord(voteRecord);
	}

	@Override
	public void deleteVoteRecord(VoteRecord voteRecord) throws EVException {
		
		persistence.deleteVoteRecord(voteRecord);
	}
}
