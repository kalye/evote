package edu.uga.cs.evote.logic;


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
import edu.uga.cs.evote.session.Session;


public interface LogicLayer
{
    public List<Ballot>         findAllBallots() throws EVException;
    public List<BallotItem>         findAllBallotItems() throws EVException;
    public List<Candidate>         findAllCandidates() throws EVException;
    public List<Election>         findAllElections() throws EVException;
    public List<ElectionsOfficer>         findAllElectionsOfficers() throws EVException;
    public List<ElectoralDistrict>         findAllElectoralDistricts() throws EVException;
    public List<Issue>         findAllIssues() throws EVException;
    public List<PoliticalParty>         findAllPoliticalParties() throws EVException;
    public List<Voter>         findAllVoters() throws EVException;
    public List<VoteRecord>         findAllVoteRecords() throws EVException;
    
    public long               addToBallot( BallotItem ballotItem, Ballot ballot ) throws EVException;
    public long               addToElection( Candidate candidate, Election election ) throws EVException;
    public long               addToElectoralDistrict( Voter voter, ElectoralDistrict electoralDistrict ) throws EVException;
    public long               addToPoliticalParty( Candidate candidate, PoliticalParty politicalParty ) throws EVException;
    
    public long               createBallot( Date openDate, Date closeDate, ElectoralDistrict electoralDistrict ) throws EVException;
    public long               createCandidate( String name, Election election, PoliticalParty politicalParty ) throws EVException;
    public long               createElection( String office, boolean isPartisan, Ballot ballot ) throws EVException;
    public long               createElectionsOfficer( 	String firstName, String lastName, String userName,
    													String password, String emailAddress, 
    													String address, String zipCode ) throws EVException;
    public long               createElectoralDistrict( String name, String zipCode ) throws EVException;
    public long               createIssue( String question, Ballot ballot ) throws EVException;
    public long               createPoliticalParty( String name ) throws EVException;
    public long               createVoter( 	String firstName, String lastName, String userName,
		     								String password, String emailAddress, String address, String zipCode,
		     								String voterId, int age, 
		     								ElectoralDistrict electoralDistrict  ) throws EVException;
    public long               createVoteRecord( Date date, Voter voter, Ballot ballot ) throws EVException; 
   
    public List<Ballot>       	findBallots( ElectoralDistrict electoralDistrict ) throws EVException;
    public List<BallotItem>		findBallotItems( Ballot ballot ) throws EVException;
    public List<Candidate>		findCandidates( Election election ) throws EVException;
    public List<Candidate>		findCandidates( PoliticalParty politicalParty ) throws EVException;
    public List<Election>		findElections( Ballot ballot ) throws EVException;
    public List<Issue>			findIssues( Ballot ballot ) throws EVException;
    public List<Voter>			findVoters( ElectoralDistrict electoralDistrict ) throws EVException;
    public List<VoteRecord>		findVoteRecords( Ballot ballot ) throws EVException;
    public List<VoteRecord>		findVoteRecords( Voter voter ) throws EVException; 
   
    public void               logout( String ssid ) throws EVException;
    public String             login( Session session, String userName, String password ) throws EVException;
}
