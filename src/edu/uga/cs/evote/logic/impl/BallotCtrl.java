package edu.uga.cs.evote.logic.impl;

import java.util.Date;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.object.ObjectLayer;

public class BallotCtrl {

	private ObjectLayer objectLayer = null;
	
	public BallotCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}

	public long addToBallot(BallotItem ballotItem, Ballot ballot) throws EVException {
		ballotItem.setBallot(ballot);
		if(ballotItem.getKind().equals("issue")) objectLayer.storeIssue((Issue)ballotItem);
		else objectLayer.storeElection((Election)ballotItem);
		
		return ballotItem.getId();
	}
	
	public long createBallot(Date openDate, Date closeDate, ElectoralDistrict electoralDistrict) throws EVException {
		
		Ballot				ballot = null;
		
		ballot = objectLayer.createBallot( openDate, closeDate, false, electoralDistrict );
        objectLayer.storeBallot( ballot );

        return ballot.getId();
	}
	
	public List<Ballot> findAllBallots()
            throws EVException
    {
        List<Ballot> 	ballots  = null;
        
        ballots = objectLayer.findBallot( null );

        return ballots;
    }
	
	public List<Ballot> findBallots( ElectoralDistrict electoralDistrict )
            throws EVException
    {
        Ballot modelBallot = objectLayer.createBallot();
        modelBallot.setElectoralDistrict(electoralDistrict);
        
        List<Ballot> ballots  = objectLayer.findBallot(modelBallot);
        
        return ballots;
    }

}
