package edu.uga.cs.evote.logic.impl;

import java.util.Collections;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.object.ObjectLayer;

public class BallotItemCtrl {

	private ObjectLayer objectLayer = null;
    
    public BallotItemCtrl( ObjectLayer objectLayer )
    {
        this.objectLayer = objectLayer;
    }

    public List<BallotItem> findAllBallotItems()
            throws EVException
    {
        List<BallotItem> 	ballotItems  = Collections.<BallotItem>emptyList();
        
        ballotItems.addAll(objectLayer.findElection(null));
        ballotItems.addAll(objectLayer.findIssue(null));

        return ballotItems;
    }
    
    public long createIssue(String question, Ballot ballot) throws EVException {
		
		Issue				issue = null;
		
		issue = objectLayer.createIssue( question, ballot );
        objectLayer.storeIssue( issue );

        return issue.getId();
	}
    
    public long createElection(String office, Ballot ballot, boolean isPartisan) throws EVException{
    	
    	Election election = null;
    	
    	election = objectLayer.createElection(office, isPartisan, ballot);
    	objectLayer.storeElection(election);
    	
    	return election.getId();
    }
    
    public List<Issue> findAllIssues()
            throws EVException
    {
        List<Issue> 	issues  = null;
        
        issues = objectLayer.findIssue( null );

        return issues;
    }
    
    public List<Election> findAllElections() throws EVException{
    	
    	List<Election> elections = null;
    	
    	elections = objectLayer.findElection(null);
    	
    	return elections;
    }
    
    public List<Issue> findIssues(Ballot ballot) throws EVException {
		
		Issue modelIssue = objectLayer.createIssue();
        modelIssue.setBallot(ballot);
        
        List<Issue> issues  = objectLayer.findIssue(modelIssue);
        
        return issues;
	}
    
    public List<Election> findElections(Ballot ballot) throws EVException{
    	
    	Election modelElection = objectLayer.createElection();
    	modelElection.setBallot(ballot);
    	
    	List<Election> elections = objectLayer.findElection(modelElection);
    	
    	return elections;
    }
    
    public List<BallotItem> findBallotItems( Ballot ballot )
            throws EVException
    {
        Election modelElection = objectLayer.createElection();
        Issue modelIssue = objectLayer.createIssue();
        modelElection.setBallot(ballot);
        modelIssue.setBallot(ballot);
        
        List<BallotItem> 	ballotItems  = Collections.<BallotItem>emptyList();
        ballotItems.addAll(objectLayer.findIssue(modelIssue));
        ballotItems.addAll(objectLayer.findElection(modelElection));
        
        return ballotItems;
    }
}
