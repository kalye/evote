package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;

public class CandidateCtrl {

	private ObjectLayer objectLayer = null;
	
	public CandidateCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}

	public long createCandidate(String name, Election election, PoliticalParty politicalParty) throws EVException {
		
		Candidate				candidate = null;
		
		candidate = objectLayer.createCandidate( name, politicalParty, election );
        objectLayer.storeCandidate( candidate );

        return candidate.getId();
	}
	
	public List<Candidate> findAllCandidates()
            throws EVException
    {
        List<Candidate> 	candidates  = null;
        
        candidates = objectLayer.findCandidate( null );

        return candidates;
    }
	
	public List<Candidate> findCandidates(Election election) throws EVException {
		
		Candidate modelCandidate = objectLayer.createCandidate();
        modelCandidate.setElection(election);
        
        List<Candidate> candidates  = objectLayer.findCandidate(modelCandidate);
        
        return candidates;
	}

	public List<Candidate> findCandidates(PoliticalParty politicalParty) throws EVException {
		
		Candidate modelCandidate = objectLayer.createCandidate();
        modelCandidate.setPoliticalParty(politicalParty);
        
        List<Candidate> candidates  = objectLayer.findCandidate(modelCandidate);
        
        return candidates;
	}

}
