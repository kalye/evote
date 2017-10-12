package edu.uga.cs.evote.test.object;

import java.sql.Connection;
import java.text.SimpleDateFormat;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.db.DbUtils;
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
import edu.uga.cs.evote.entity.impl.UserImpl;
import edu.uga.cs.evote.entity.impl.VoteRecordImpl;
import edu.uga.cs.evote.entity.impl.VoterImpl;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class WriteTest
{
	public static void main(String[] args) throws EVException
	{
		Connection conn = null;
		ObjectLayer objectLayer = null;
		PersistenceLayer persistence = null;
		Voter voter1, voter2;
		VoteRecord voteRecord;
		PoliticalParty politicalParty1, politicalParty2, politicalParty3;
		Issue issue;
		ElectoralDistrict electoralDistrict;
		ElectionsOfficer electionOfficer;
		Election election;
		Candidate candidate;
		Ballot ballot1, ballot2;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD");
		// get a database connection
		try {
			conn = DbUtils.connect();
		} 
		catch (Exception seq) {
			System.err.println( "WriteTest: Unable to obtain a database connection" );
		}
		// obtain a reference to the ObjectModel module      
		objectLayer = new ObjectLayerImpl();
		// obtain a reference to Persistence module and connect it to the ObjectModel        
		persistence = new PersistenceLayerImpl( conn, objectLayer ); 
		objectLayer = new ObjectLayerImpl( persistence );  
		
		PoliticalParty pp = objectLayer.createPoliticalParty("No party");
        persistence.storePoliticalParty(pp);
				
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
		
		try {
			//Create 2 elections officers. Election Officer1
			System.out.println("================ Creating Election Officers ================");
			electionOfficer = objectLayer.createElectionsOfficer("Jhon","Smith","JhonSmith","Jhon123",
					"Jhon13@uga.edu", "725 Hill Rd, Athens GA", "70631");
			persistence.storeElectionsOfficer(electionOfficer);
			//Create 2 elections officers. Election Officer2
			electionOfficer = objectLayer.createElectionsOfficer("Amy","Jill","AmyJill", "Jill34", "AmyJ@uga.edu",
					"355 SouthRd, Athens Ga", "70635");
			persistence.storeElectionsOfficer(electionOfficer);

			//Create an electoral district.
			System.out.println("================ Creating Electoral District ===============");

			electoralDistrict = objectLayer.createElectoralDistrict("fulton", "00001");
			persistence.storeElectoralDistrict(electoralDistrict);
			System.out.println("===================== Creating Voters ======================");

			//Create 2 voters so that they all belong of the electoral district you created. voter1
			voter1 = objectLayer.createVoter("Kal", "Teklu", "k15@uga.edu", "uga@edu1234", "k15@uga.edu", "", "", 22);
			persistence.storeVoterBelongsToElectoralDistrict(voter1, electoralDistrict);

			//Create 2 voters so that they all belong of the electoral district you created. voter2
			voter2 = objectLayer.createVoter("John", "Dow", "jd15@uga.edu", "jd@1234", "jd15@uga.edu", "", "", 23);
			persistence.storeVoterBelongsToElectoralDistrict(voter2, electoralDistrict);

			//Create 2 political parties. political party 1
			System.out.println("================ Creating Political Parties ================");

			politicalParty1 = objectLayer.createPoliticalParty("Green Party");
			persistence.storePoliticalParty(politicalParty1);

			//Create 2 political parties. political party 2

			politicalParty2 = objectLayer.createPoliticalParty("Blue Party");
			persistence.storePoliticalParty(politicalParty2);

			//politicalParty3
			politicalParty3 = objectLayer.createPoliticalParty("Yellow Party");
			persistence.storePoliticalParty(politicalParty3);
			
			System.out.println("============== Creating Ballots w/ BallotItems =============");

			//Create 2 ballots, each with 3 issues and 3 elections.
			ballot1 = objectLayer.createBallot(format.parse("2016-10-26"), format.parse("2016-11-08"), true, electoralDistrict);
			persistence.storeBallot(ballot1);
			//issue 1 ballot 1
			issue = objectLayer.createIssue("should we eat banana for breakfast?", ballot1);
			ballot1.addBallotItem(issue);
			persistence.storeBallotIncludesBallotItem(ballot1, issue);
			
			//issue 2 ballot 1
			issue = objectLayer.createIssue("should we eat doughnuts for breakfast?", ballot1);
			ballot1.addBallotItem(issue);
			persistence.storeBallotIncludesBallotItem(ballot1, issue);
			
			//issue 3 ballot 1
			issue = objectLayer.createIssue("should we  eat cereals for breakfast?", ballot1);
			ballot1.addBallotItem(issue);
			persistence.storeBallotIncludesBallotItem(ballot1, issue);

			//election 1 ballot 1-partisian
			election = objectLayer.createElection("fulton", true, ballot1);
			ballot1.addBallotItem(election);
			persistence.storeBallotIncludesBallotItem(ballot1, election);

			System.out.println("================== Creating Candidates =====================");

			//candidate 1 election 1
			candidate = objectLayer.createCandidate("Jimmy John", politicalParty1, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);
			
			//candidate 2 election 1
			candidate = objectLayer.createCandidate("John Dow", politicalParty2, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);
			
			//candidate 3 election 1
			candidate = objectLayer.createCandidate("James Cameron", politicalParty3, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);

			//election 2 ballot 1-non-partisian
			election = objectLayer.createElection("Minister for Magic", false, ballot1);
			ballot1.addBallotItem(election);
			persistence.storeBallotIncludesBallotItem(ballot1, election);
			
			PoliticalParty noParty = objectLayer.createPoliticalParty("No party");
			noParty = persistence.restorePoliticalParty(noParty).get(0);
			//candidate 1 election 2
			candidate = objectLayer.createCandidate("Harry Potter", noParty, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);

			//candidate 2 election 2
			candidate = objectLayer.createCandidate("Hermione Granger-Weasley", noParty, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);
			
			//candidate 3 election 2
			candidate = objectLayer.createCandidate("Ronald Weasley", noParty, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);
			
			//election 3 ballot 1-non-partisian
			election = objectLayer.createElection("Ruler of the Galaxy", false, ballot1);
			ballot1.addBallotItem(election);
			persistence.storeBallotIncludesBallotItem(ballot1, election);

			//candidate 1 election 3
			candidate = objectLayer.createCandidate("Captain Kirk", noParty, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);
			
			//candidate 2 election 3
			candidate = objectLayer.createCandidate("Commander Spock", noParty, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);
			
			//candidate 3 election 3
			candidate = objectLayer.createCandidate("Darth Vader", noParty, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);

			System.out.println("============== Creating Ballots w/ BallotItems =============");

			//Create 2 ballots, each with 3 issues and 3 elections.
			ballot2 = objectLayer.createBallot(format.parse("2016-10-26"), format.parse("2016-11-08"), true, electoralDistrict);
			persistence.storeBallot(ballot2);
			//issue 1 ballot 2
			issue = objectLayer.createIssue("flat tax rate of 15%", ballot2);
			ballot2.addBallotItem(issue);
			persistence.storeBallotIncludesBallotItem(ballot2, issue);
			
			//issue 2 ballot 2
			issue = objectLayer.createIssue("no tax below income $100,000", ballot2);
			ballot2.addBallotItem(issue);
			persistence.storeBallotIncludesBallotItem(ballot2, issue);
			
			//issue 3 ballot 2
			issue = objectLayer.createIssue("15% tax rate for income bracket of $0 - $100000 and 35% above $100K", ballot2);
			ballot2.addBallotItem(issue);
			persistence.storeBallotIncludesBallotItem(ballot2, issue);

			//election 1 ballot 2-partisian
			election = objectLayer.createElection("Most Important English Author", true, ballot2);
			ballot2.addBallotItem(election);
			persistence.storeBallotIncludesBallotItem(ballot2, election);
			
			System.out.println("================== Creating Candidates =====================");

			//candidate 1 election 1
			candidate = objectLayer.createCandidate("William Shakespeare", politicalParty1, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);
			
			//candidate 2 election 1
			candidate = objectLayer.createCandidate("Agatha Christie", politicalParty2, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);
			
			//candidate 3 election 1
			candidate = objectLayer.createCandidate("Neil Gaiman", politicalParty3, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);
			

			//election 2 ballot 2-non-partisian
			election = objectLayer.createElection("Best Movie Character", false, ballot2);
			ballot2.addBallotItem(election);
			persistence.storeBallotIncludesBallotItem(ballot2, election);
			
			//candidate 1 election 2
			candidate = objectLayer.createCandidate("James Bond", noParty, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);
			
			//candidate 2 election 2
			candidate = objectLayer.createCandidate("Captain Jack Sparrow", noParty, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);
			
			//candidate 3 election 2
			candidate = objectLayer.createCandidate("any character played by Will Smith", noParty, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);
			
			//election 3 ballot 2-non-partisian
			election = objectLayer.createElection("Best Actor", false, ballot2);
			ballot2.addBallotItem(election);
			persistence.storeBallotIncludesBallotItem(ballot2, election);
			
			//candidate 1 election 3
			candidate = objectLayer.createCandidate("Adam Smith", noParty, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);

			//candidate 2 election 3
			candidate = objectLayer.createCandidate("Brad Pit", noParty, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);
			
			//candidate 3 election 3
			candidate = objectLayer.createCandidate("James Brown", noParty, election);
			election.addCandidate(candidate);
			persistence.storeCandidateIsCandidateInElection(candidate, election);
			
			System.out.println("================== Creating Vote Record ====================");

			//voting on ballot1
			//vote record for voter1
			voteRecord = objectLayer.createVoteRecord();
			voteRecord.setBallot(persistence.restoreBallot(ballot1).get(0));
			voteRecord.setVoter(persistence.restoreVoter(voter1).get(0));
			voteRecord.setDate(format.parse("2016-11-08"));
			persistence.storeVoteRecord(voteRecord);
		
			//vote record for voter2
			voteRecord = objectLayer.createVoteRecord();
			voteRecord.setBallot(ballot1);
			voteRecord.setVoter(voter2);
			voteRecord.setDate(format.parse("2016-11-08"));
			persistence.storeVoteRecord(voteRecord);
			
			//voting on issue1
			issue = persistence.restoreIssue(new IssueImpl("should we eat banana for breakfast?", ballot1)).get(0);
			//voter1
			issue.addYesVote();
			//voter2
			issue.addYesVote();
			persistence.storeBallotIncludesBallotItem(ballot1, issue);
			
			//issue2
			issue = persistence.restoreIssue(new IssueImpl("should we eat doughnuts for breakfast?", ballot1)).get(0);
			//voter1
			issue.addNoVote();
			//voter2
			issue.addYesVote();
			persistence.storeBallotIncludesBallotItem(ballot1, issue);
			
			//issue3
			issue = persistence.restoreIssue(new IssueImpl("should we  eat cereals for breakfast?", ballot1)).get(0);
			//voter1
			issue.addNoVote();
			//voter2
			issue.addNoVote();
			persistence.storeBallotIncludesBallotItem(ballot1, issue);
			
			//election1
			election = persistence.restoreElection(new ElectionImpl("fulton", true, null)).get(0);
			//voter1
			candidate = election.getCandidates().get(0);
			candidate.addVote();
			persistence.storeCandidate(candidate);
			election.addVote();
			objectLayer.storeElection(election);
			//voter2
			candidate = election.getCandidates().get(2);
			candidate.addVote();
			persistence.storeCandidate(candidate);
			election.addVote();
			objectLayer.storeElection(election);
			
			//election2
			election = persistence.restoreElection(new ElectionImpl("Minister for Magic", false, ballot1)).get(0);
			//voter1
			candidate = election.getCandidates().get(2);
			candidate.addVote();
			persistence.storeCandidate(candidate);
			election.addVote();
			objectLayer.storeElection(election);
			//voter2
			candidate = election.getCandidates().get(1);
			candidate.addVote();
			objectLayer.storeCandidate(candidate);
			election.addVote();
			objectLayer.storeElection(election);
			
			//election3
			election = persistence.restoreElection(new ElectionImpl("Ruler of the Galaxy", false, ballot1)).get(0);
			//voter1
			candidate = election.getCandidates().get(1);
			candidate.addVote();
			persistence.storeCandidate(candidate);
			election.addVote();
			objectLayer.storeElection(election);
			//voter2
			candidate = election.getCandidates().get(0);
			candidate.addVote();
			persistence.storeCandidate(candidate);
			election.addVote();
			objectLayer.storeElection(election);
			
			//voters voting on ballot2
			//vote record for voter1
			voteRecord = objectLayer.createVoteRecord();
			voteRecord.setBallot(ballot2);
			voteRecord.setVoter(voter1);
			voteRecord.setDate(format.parse("2016-11-08"));
			persistence.storeVoteRecord(voteRecord);
		
			//vote record for voter2
			voteRecord = objectLayer.createVoteRecord();
			voteRecord.setBallot(ballot2);
			voteRecord.setVoter(voter2);
			voteRecord.setDate(format.parse("2016-11-08"));
			persistence.storeVoteRecord(voteRecord);
			
			//voting on issue1
			issue = persistence.restoreIssue(new IssueImpl("flat tax rate of 15%", ballot2)).get(0);
			//voter1
			issue.addNoVote();
			//voter2
			issue.addYesVote();
			objectLayer.storeIssue(issue);
			
			//issue2
			issue = persistence.restoreIssue(new IssueImpl("no tax below income $100,000", ballot2)).get(0);
			//voter1
			issue.addYesVote();
			//voter2
			issue.addYesVote();
			objectLayer.storeIssue(issue);
			
			//issue3
			issue = persistence.restoreIssue(new IssueImpl("15% tax rate for income bracket of $0 - $100000 and 35% above $100K", ballot2)).get(0);
			//voter1
			issue.addYesVote();
			//voter2
			issue.addNoVote();
			objectLayer.storeIssue(issue);
			
			//election1
			election = persistence.restoreElection(new ElectionImpl("Most Important English Author", true, ballot2)).get(0);
			//voter1
			candidate = election.getCandidates().get(2);
			candidate.addVote();
			objectLayer.storeCandidate(candidate);
			election.addVote();
			objectLayer.storeElection(election);
			//voter2
			candidate = election.getCandidates().get(2);
			candidate.addVote();
			objectLayer.storeCandidate(candidate);
			election.addVote();
			objectLayer.storeElection(election);
			
			//election2
			election = persistence.restoreElection(new ElectionImpl("Best Movie Character", false, ballot2)).get(0);
			//voter1
			candidate = election.getCandidates().get(1);
			candidate.addVote();
			objectLayer.storeCandidate(candidate);
			election.addVote();
			objectLayer.storeElection(election);
			//voter2
			candidate = election.getCandidates().get(0);
			candidate.addVote();
			objectLayer.storeCandidate(candidate);
			election.addVote();
			objectLayer.storeElection(election);
			
			//election3
			election = persistence.restoreElection(new ElectionImpl("Best Actor", false, ballot2)).get(0);
			//voter1
			candidate = election.getCandidates().get(0);
			candidate.addVote();
			objectLayer.storeCandidate(candidate);
			election.addVote();
			objectLayer.storeElection(election);
			//voter2
			candidate = election.getCandidates().get(0);
			candidate.addVote();
			objectLayer.storeCandidate(candidate);
			election.addVote();
			objectLayer.storeElection(election);
			
		}catch( EVException ce) {
			System.err.println( "Exception: " + ce );
			ce.printStackTrace();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
		finally {
			// close the connections
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
