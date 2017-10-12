package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;

public class PoliticalPartyCtrl {

	private ObjectLayer objectLayer;
	
	public PoliticalPartyCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}

	public long addToPoliticalParty(Candidate candidate, PoliticalParty politicalParty) throws EVException {
		candidate.setPoliticalParty(politicalParty);
		objectLayer.storeCandidate(candidate);
		
		return candidate.getId();
	}

	public long createPoliticalParty(String name) throws EVException {
		
		PoliticalParty				politicalParty = null;
		
		politicalParty = objectLayer.createPoliticalParty( name );
        objectLayer.storePoliticalParty( politicalParty );

        return politicalParty.getId();
	}
	
	public List<PoliticalParty> findAllPoliticalParties()
            throws EVException
    {
        List<PoliticalParty> 	politicalParties  = null;
        
        politicalParties = objectLayer.findPoliticalParty( null );

        return politicalParties;
    }
}
