package edu.uga.cs.evote.entity.impl;

import java.util.Date;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.persistence.impl.Persistent;

public class BallotImpl extends Persistent implements Ballot{

    private Date openDate;
    private Date closeDate;
    private ElectoralDistrict electoralDistrict;
    private List<BallotItem> ballotItems;
    private List<VoteRecord> voterVoteRecords;
    private boolean isApproved = false;

    public BallotImpl(){

	super(-1);
	openDate = null;
	closeDate = null;
	electoralDistrict = null;
	ballotItems = null;
	voterVoteRecords = null;
    }//BallotImpl

    public BallotImpl(Date openDate, Date closeDate, ElectoralDistrict electoralDistrict) throws EVException{

	this();
	setOpenDate(openDate);
	setCloseDate(closeDate);
	setElectoralDistrict(electoralDistrict);
    }//BallotImpl

    public Date getOpenDate(){

	return openDate;
    }//getOpenDate

    public void setOpenDate(Date openDate){

	this.openDate = openDate;
    }//setOpenDate

    public Date getCloseDate(){

	return closeDate;
    }//getCloseDate

    public void setCloseDate(Date closeDate){

	this.closeDate = closeDate;
    }//setCloseDate

    public ElectoralDistrict getElectoralDistrict() throws EVException{

	if(electoralDistrict == null){
	    if(isPersistent()) {
		
	    	if(getPersistenceLayer() == null) System.out.println("ElectoralDistrict persistence layer = null..");
	    	electoralDistrict = getPersistenceLayer().restoreElectoralDistrictHasBallotBallot(this);
	    } 
	}
	return electoralDistrict;
    }//getElectoralDistrict

    public void setElectoralDistrict( ElectoralDistrict electoralDistrict ) throws EVException{
    	
	this.electoralDistrict = electoralDistrict;  
	
    }//setElectoralDistrict

    public List<BallotItem> getBallotItems() throws EVException{

	if(ballotItems == null){

            if(isPersistent()) {
                
		ballotItems = getPersistenceLayer().restoreBallotIncludesBallotItem(this);
            } else{

                throw new EVException("Ballot object is not persistent");
            }//if
	}//if
            
	return ballotItems;
    }//getBallotItems

    public void addBallotItem(BallotItem ballotItem) throws EVException{

	getPersistenceLayer().storeBallotIncludesBallotItem(this, ballotItem);
    }//addBallotItem

    public void deleteBallotItem(BallotItem ballotItem) throws EVException{

	getPersistenceLayer().deleteBallotIncludesBallotItem(this, ballotItem);
    }//deleteBallotItem

    public boolean getIsApproved(){
    	return isApproved;
    }
    
    public void setIsApproved(boolean isApproved){
    	this.isApproved = isApproved;
    }
    
    public List<VoteRecord> getVoterVoteRecords() throws EVException{

	if(voterVoteRecords == null){

            if(isPersistent()) {

                voterVoteRecords = getPersistenceLayer().restoreVoteRecord(new VoteRecordImpl(null, null, this));
            } else{

                throw new EVException("Ballot object is not persistent");
            }//if
	}//if

	return voterVoteRecords;
    }//getVoterVoteRecords
}//BallotImpl.java
