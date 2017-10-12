package edu.uga.cs.evote.logic.impl;

import java.util.Date;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;

public class VoteRecordCtrl {

	private ObjectLayer objectLayer = null;
	
	public VoteRecordCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}
	
	public long CreateVoteRecord(Date date, Voter voter, Ballot ballot) throws EVException {
		
		VoteRecord				voteRecord = null;
		
		voteRecord = objectLayer.createVoteRecord( ballot, voter, date );
		
		objectLayer.storeVoteRecord( voteRecord );

        return voteRecord.getId();
	}
	
	public List<VoteRecord> findAllVoteRecords()
            throws EVException
    {
        List<VoteRecord> 	voteRecords  = null;
        
        voteRecords = objectLayer.findVoteRecord( null );

        return voteRecords;
    }
	
	public List<VoteRecord> findVoteRecords(Ballot ballot) throws EVException {
		VoteRecord modelVoteRecord = objectLayer.createVoteRecord();
        modelVoteRecord.setBallot(ballot);
        
        List<VoteRecord> voteRecords  = objectLayer.findVoteRecord(modelVoteRecord);
        
        return voteRecords;
	}

	public List<VoteRecord> findVoteRecords(Voter voter) throws EVException {
		VoteRecord modelVoteRecord = objectLayer.createVoteRecord();
        modelVoteRecord.setVoter(voter);
        
        List<VoteRecord> voteRecords  = objectLayer.findVoteRecord(modelVoteRecord);
        
        return voteRecords;
	}

}
