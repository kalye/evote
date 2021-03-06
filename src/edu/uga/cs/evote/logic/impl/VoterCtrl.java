package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;

public class VoterCtrl {

	private ObjectLayer objectLayer = null;
	
	public VoterCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}
	
	public long createVoter(String firstName, String lastName, String userName, String password, String emailAddress,
			String address, String zipCode, String voterId, int age, ElectoralDistrict electoralDistrict) throws EVException {
		
		Voter				voter = null;
		
		voter = objectLayer.createVoter( 	firstName, lastName, userName, password, emailAddress, address, zipCode,
											age);
		
		voter.setElectoralDistrict(electoralDistrict);
		voter.setVoterId(voterId);
		
        objectLayer.storeVoter( voter );

        return voter.getId();
	}
	public void updateVoter(Voter voter) throws EVException{
		objectLayer.storeVoter( voter );
	}
	public List<Voter> findAllVoters()
            throws EVException
    {
        List<Voter> 	voters  = null;
        
        voters = objectLayer.findVoter( null );

        return voters;
    }
	
	public List<Voter> findVoters(ElectoralDistrict electoralDistrict) throws EVException {
		
		Voter modelVoter = objectLayer.createVoter();
        modelVoter.setElectoralDistrict(electoralDistrict);
        
        List<Voter> voters  = objectLayer.findVoter(modelVoter);
        
        return voters;
	}

}
