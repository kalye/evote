package edu.uga.cs.evote.test.object;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.db.DbUtils;
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
import edu.uga.cs.evote.entity.impl.BallotImpl;
import edu.uga.cs.evote.entity.impl.BallotItemImpl;
import edu.uga.cs.evote.entity.impl.CandidateImpl;
import edu.uga.cs.evote.entity.impl.ElectionImpl;
import edu.uga.cs.evote.entity.impl.ElectionsOfficerImpl;
import edu.uga.cs.evote.entity.impl.ElectoralDistrictImpl;
import edu.uga.cs.evote.entity.impl.IssueImpl;
import edu.uga.cs.evote.entity.impl.PoliticalPartyImpl;
import edu.uga.cs.evote.entity.impl.UserImpl;
import edu.uga.cs.evote.entity.impl.VoteRecordImpl;
import edu.uga.cs.evote.entity.impl.VoterImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class UpdateTest
{
	public static void main(String[] args) throws EVException
	{
		Connection  conn = null;
		ObjectLayer objectLayer = null;
		PersistenceLayer persistence = null;

		// get a database connection
		try {
			conn = DbUtils.connect();
		} 
		catch (Exception seq) {
			System.err.println( "UpdateTest: Unable to obtain a database connection" );
		}

		if( conn == null ) {
			System.out.println( "UpdateTest: failed to connect to the database" );
			return;
		}

		// obtain a reference to the ObjectModel module      
		objectLayer = new ObjectLayerImpl();
		// obtain a reference to Persistence module and connect it to the ObjectModel        
		persistence = new PersistenceLayerImpl( conn, objectLayer ); 
		// connect the ObjectModel module to the Persistence module
		objectLayer = new ObjectLayerImpl( persistence ); 
		
		BallotImpl.setPersistenceLayer(persistence);
		if(BallotImpl.getPersistenceLayer() == null) System.out.println("no persistence for ballot");
		
		BallotItemImpl.setPersistenceLayer(persistence);
		if(BallotItemImpl.getPersistenceLayer() == null) System.out.println("no persistence for ballotitem");
		
		CandidateImpl.setPersistenceLayer(persistence);
		if(CandidateImpl.getPersistenceLayer() == null) System.out.println("no persistence for candidate");
		
		ElectionImpl.setPersistenceLayer(persistence);
		if(ElectionImpl.getPersistenceLayer() == null) System.out.println("no persistence for election");
		
		ElectionsOfficerImpl.setPersistenceLayer(persistence);
		if(ElectionsOfficerImpl.getPersistenceLayer() == null) System.out.println("no persistence for electionsofficer");
		
		ElectoralDistrictImpl.setPersistenceLayer(persistence);
		if(ElectoralDistrictImpl.getPersistenceLayer() == null) System.out.println("no persistence for electoraldistrict");
		
		IssueImpl.setPersistenceLayer(persistence);
		if(IssueImpl.getPersistenceLayer() == null) System.out.println("no persistence for issue");
		
		PoliticalPartyImpl.setPersistenceLayer(persistence);
		if(PoliticalPartyImpl.getPersistenceLayer() == null) System.out.println("no persistence for party");
		
		UserImpl.setPersistenceLayer(persistence);
		if(UserImpl.getPersistenceLayer() == null) System.out.println("no persistence for user");
		
		VoteRecordImpl.setPersistenceLayer(persistence);
		if(VoteRecordImpl.getPersistenceLayer() == null) System.out.println("no persistence for voterecord");
		
		VoterImpl.setPersistenceLayer(persistence);
		if(VoterImpl.getPersistenceLayer() == null) System.out.println("no persistence for voter");

		List<Ballot> ballots = null;
		Voter voter;
		VoteRecord voteRecord;
		PoliticalParty politicalParty;
		Issue issue;
		ElectoralDistrict electoralDistrict;
		ElectionsOfficer electionOfficer;
		Election election;
		Ballot ballot;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD");
		try {
			System.out.println("====================Updating==================");
			System.out.println("==============Updating ElectionsOfficer================");
			//ElectionsOfficer 
			electionOfficer = objectLayer.createElectionsOfficer("Jhon","Smith","JhonSmith",null,
					null, null, null);
			List<ElectionsOfficer> electionOfficers = persistence.restoreElectionsOfficer(electionOfficer);
			
			for(ElectionsOfficer elOfficer: electionOfficers){
				System.out.println("ElectionsOfficer id: " + elOfficer.getId());
				System.out.println("ElectionsOfficer name: " + elOfficer.getFirstName() + " " + elOfficer.getLastName());
				System.out.println("changing last name to Johnny...");
				elOfficer.setLastName("Johnny");
				persistence.storeElectionsOfficer(elOfficer);
			}
			
			System.out.println("==============Updating ElectoralDistrict================");
			//ElectoralDistrict
			electoralDistrict = objectLayer.createElectoralDistrict("fulton", null);
			
			List<ElectoralDistrict> electionDistricts = persistence.restoreElectoralDistrict(electoralDistrict);
			
			for(ElectoralDistrict elDistrict: electionDistricts){
				System.out.println("ElectoralDistrict id: " + elDistrict.getId());
				System.out.println("ElectoralDistrict name: " + elDistrict.getName());
				elDistrict.setName("Gwinnett");
				System.out.println("Changing ElectoralDistrict name to Gwinnett...");
				persistence.storeElectoralDistrict(elDistrict);
			}
			
			System.out.println("==============Updating Voter================");
			//voter
			voter = objectLayer.createVoter(null, null, "k15@uga.edu", null, null, null, null, 22);
			List<Voter> voters = persistence.restoreVoter(voter);
			for(Voter modelVoter: voters){
				System.out.println("Voter id: " + modelVoter.getId());
				System.out.println("Voter name: " + modelVoter.getFirstName() + " " + modelVoter.getLastName());
				modelVoter.setLastName("Kali");
				System.out.println("Changing Voter lastName to Kali...");
				persistence.storeVoter(modelVoter);
			}
			
			System.out.println("==============Updating PoliticalParty================");
			//political party
			politicalParty = objectLayer.createPoliticalParty("Green Party");
			List<PoliticalParty> politicalParties = persistence.restorePoliticalParty(politicalParty);
			for(PoliticalParty pParty: politicalParties){
				System.out.println("Political Party id: " + pParty.getId());
				System.out.println("Political Party name: " + pParty.getName());
				pParty.setName("Green Tea Party");
				System.out.println("Changing party name to Green Tea Party");
				persistence.storePoliticalParty(pParty);
			}
			
			System.out.println("==============Updating Ballot================");
			//ballot
			ballot = objectLayer.createBallot(format.parse("2016-10-26"), format.parse("2016-11-08"), true, null);
			ballots = persistence.restoreBallot(ballot);
			
			for(Ballot modelBallot: ballots){
				System.out.println("Ballot id: " + modelBallot.getId());
				System.out.println("Ballot open Date: " + modelBallot.getOpenDate());
				System.out.println("Ballot close Date: " + modelBallot.getCloseDate());
				modelBallot.setCloseDate(format.parse("2016-11-09"));
				System.out.println("Changing Ballot closeDate to 2016-11-09");
				persistence.storeBallot(modelBallot);
				
				List<BallotItem> ballotItems = modelBallot.getBallotItems();
				for(BallotItem ballotItem: ballotItems){
					System.out.println("	BallotItem id: " + ballotItem.getId());
					System.out.println("	BallotItem type/kind: " + ballotItem.getKind());
					System.out.println("	BallotItem vote count: " + ballotItem.getVoteCount());
					ballotItem.addVote();
					System.out.println("Adding vote to BallotItem...");
					persistence.storeBallotIncludesBallotItem(modelBallot, ballotItem);
				}
			}
			
			System.out.println("==============Updating Election================");
			//election
			election = objectLayer.createElection("Minister for Magic", true, null);
			List<Election> elections = persistence.restoreElection(election);
			for(Election modelElection: elections){
				System.out.println("Election id: " + modelElection.getId());
				System.out.println("Election is Partisan: " + modelElection.getIsPartisan());
				System.out.println("Election running for office: " + modelElection.getOffice());
				modelElection.setOffice("Gwinnett Mayor");
				System.out.println("Changing office to Gwinnett Mayor...");
				persistence.storeElection(modelElection);
				
				List<Candidate> candidates = modelElection.getCandidates();
				int i = 0;
				for(Candidate modelCandidate: candidates){
					System.out.println("Candidate id: " + modelCandidate.getId());
					System.out.println("Candidate name: " + modelCandidate.getName());
					System.out.println("Candidate vote count: " + modelCandidate.getVoteCount());
					if(i == 0){
						modelCandidate.setName("Jimmy John");
						System.out.println("Changing name to Jimmy John...");
						persistence.storeCandidate(modelCandidate);
					}
					i++;
				}
			}
			
			System.out.println("==============Updating Issue================");
			//Election
			issue = objectLayer.createIssue("flat tax rate of 15%", null);
			issue.setKind("issue");
			List<Issue> issues = persistence.restoreIssue(issue);
			for(Issue modelIssue: issues){
				System.out.println("Issue id: " + modelIssue.getId());
				System.out.println("Issue question: " + modelIssue.getQuestion());
				System.out.println("Candidate yes count: " + modelIssue.getYesCount());
				modelIssue.setQuestion("flat tax rate of 16%");
				System.out.println("Changing Issue question...");
				persistence.storeIssue(modelIssue);
			}
			
			System.out.println("==============Updating VoteRecord================");
			//VoteRecord
			voteRecord = objectLayer.createVoteRecord();
			voteRecord.setVoter(voters.get(0));
			List<VoteRecord> voteRecords = persistence.restoreVoteRecord(voteRecord);
			for(VoteRecord modelVoteRecord: voteRecords){
				System.out.println("voteRecord id: " + modelVoteRecord.getId());
				System.out.println("voteRecord date: " + modelVoteRecord.getDate());
				System.out.println("voteRecord ballot id: " + modelVoteRecord.getBallotid());
				System.out.println("voteRecord voter id: " + modelVoteRecord.getVoterid());
				modelVoteRecord.setDate(new Date());
				System.out.println("Changing VoteRecord date...");
				persistence.storeVoteRecord(modelVoteRecord);
			}
			
			System.out.println("============================Done Updating================================");
		}
		catch( EVException ce)
		{
			System.err.println( "BallotException: " + ce );
			ce.printStackTrace();
		}
		catch( Exception e)
		{
			System.err.println( "Exception: " + e );
			e.printStackTrace();
		}
		finally {
			// close the connection
			try {
				conn.close();
			}
			catch( Exception e ) {
				System.err.println( "Exception: " + e );
				e.printStackTrace();
			}
		}        
	}    
}
