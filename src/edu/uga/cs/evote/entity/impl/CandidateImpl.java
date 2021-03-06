package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.persistence.impl.Persistent;

public class CandidateImpl extends Persistent implements Candidate{

    private String name;    
    private int voteCount = 0;
    private Election election;
    private PoliticalParty politicalParty;

    public CandidateImpl(){

    	super(-1);
    	election = null;
    	politicalParty = null;
    }//Candidate

    public CandidateImpl(String name, Election election, PoliticalParty politicalParty) throws EVException{

    	super(-1);
    	setName(name);
    	setElection(election);
    	setPoliticalParty(politicalParty);
    }//Candidate

    public String getName(){

    	return name;
    }//getName

    public void setName(String name){

    	this.name = name;
    }//setName

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

    public Election getElection() throws EVException{

    	if(election == null){
            if(isPersistent()) {

            	election = getPersistenceLayer().restoreCandidateIsCandidateInElection(this);
            }//if
    	}//if
    	
        return election;
    }//getElection

    public void setElection(Election election) throws EVException{

		this.election = election;
    }//setElection

    public PoliticalParty getPoliticalParty() throws EVException{

    	if(politicalParty == null){
            	if(isPersistent()) {

            		politicalParty = getPersistenceLayer().restoreCandidateIsMemberOfPoliticalParty(this);
            	}//if
    	}//if

        return politicalParty;
    }//getPoliticalParty

    public void setPoliticalParty(PoliticalParty politicalParty) throws EVException{

		this.politicalParty = politicalParty;
    }//setPoliticalParty

}//CandiadateImpl
