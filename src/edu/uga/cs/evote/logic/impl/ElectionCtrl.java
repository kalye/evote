package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.object.ObjectLayer;

public class ElectionCtrl {

	private ObjectLayer objectLayer;
	
	public ElectionCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}

	public long addToElection(Candidate candidate, Election election) throws EVException {
		candidate.setElection(election);
		objectLayer.storeCandidate(candidate);
		return candidate.getId();
	}
	
	public long createElection(String office, boolean isPartisan, Ballot ballot) throws EVException {
		
		Election				election = null;
		
		election = objectLayer.createElection( office, isPartisan, ballot );
        objectLayer.storeElection( election );

        return election.getId();
	}

	public List<Election> findAllElections()
            throws EVException
    {
        List<Election> 	elections  = null;
        
        elections = objectLayer.findElection( null );

        return elections;
    }
	
	public List<Election> findElections(Ballot ballot) throws EVException {
		
		Election modelElection = objectLayer.createElection();
        modelElection.setBallot(ballot);
        
        List<Election> elections  = objectLayer.findElection(modelElection);
        
        return elections;
	}
}
