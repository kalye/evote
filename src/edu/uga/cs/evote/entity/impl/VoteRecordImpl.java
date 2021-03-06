package edu.uga.cs.evote.entity.impl;

import java.util.Date;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.persistence.impl.Persistent;

public class VoteRecordImpl extends Persistent implements VoteRecord{

    private Date date;
    private Voter voter;
    private Ballot ballot;
    private long ballotid;
    private long voterid;

    public VoteRecordImpl(){

	super(-1);
	date = null;
	voter = null;
	ballot = null;
    }//VoteRecordImpl

    public VoteRecordImpl(Date date, Voter voter, Ballot ballot) throws EVException{

	super(-1);
	setDate(date);
	setVoter(voter);
	setBallot(ballot);
    }//VoteRecordImpl

    public Date getDate(){

	return date;
    }//getDate

    public void setDate(Date date){

	this.date = date;
    }//setDate
    
    public Voter getVoter() throws EVException{

        return voter;
    }//getVoter

    public void setVoter(Voter voter) throws EVException{

	this.voter = voter;
    }//setVoter

    public Ballot getBallot() throws EVException{

	return ballot;
    }//getBallot

    public void setBallot( Ballot ballot ) throws EVException{

	this.ballot = ballot;
    }//setBallot

	public long getBallotid() {
		return ballotid;
	}

	public void setBallotid(long ballotid) {
		this.ballotid = ballotid;
	}

	public long getVoterid() {
		return voterid;
	}

	public void setVoterid(long voterid) {
		this.voterid = voterid;
	}
    
    
}//VoteRecordImpl
