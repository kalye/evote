package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.persistence.impl.Persistent;

public abstract class BallotItemImpl extends Persistent implements BallotItem{

    protected int voteCount = 0;
    protected Ballot ballot;
    protected String kind;

    public BallotItemImpl(){

	super(-1);
	ballot = null;
    }//BallotItemImpl

    public BallotItemImpl(Ballot ballot) throws EVException{

	super(-1);
	setBallot(ballot);
    }//BallotItemImpl

	public int getVoteCount(){

	return voteCount;
    }//getVoteCount

    public void setVoteCount(int voteCount) throws EVException{

	if(voteCount >= 0){

	    this.voteCount = voteCount;
	} else{

	    throw new EVException("setVoteCount");
	}//if
    }//setVoteCount

    public void addVote(){

	voteCount++;
    }//addVote

    public Ballot getBallot() throws EVException{

	if(ballot == null){
            if(isPersistent()) {

            	ballot = getPersistenceLayer().restoreBallotIncludesBallotItem(this);
            } else{

                return null;
            }//if
	}//if

	return ballot;
    }//getBallot

    public void setBallot(Ballot ballot) throws EVException{

    	this.ballot = ballot;
    }//setBallot
    
    public String getKind(){
    	return this.kind;
    }
    public void setKind(String kind){
    	this.kind = kind;
    }
}//BallotItemImpl