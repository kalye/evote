package edu.uga.cs.evote.entity.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;


public class ElectionImpl extends BallotItemImpl implements Election{

    private String office;
    private boolean isPartisan = false;
    private List<Candidate> candidates;

    public ElectionImpl(){

	super();
	setKind("election"); 
	candidates = null;
    }//ElectionImpl

    public ElectionImpl(String office, boolean isPartisan, Ballot ballot) throws EVException{

	super(ballot);
	this.setOffice(office);
	setIsPartisan(isPartisan);
	setKind("election");
	candidates = null;
    }//ElectionImpl

	public String getOffice(){

	return office;
    }//getOffice

    public void setOffice(String office){

	this.office = office;
    }//setOffice

    public boolean getIsPartisan(){

	return isPartisan;
    }//getIsPartisan

    public void setIsPartisan(boolean isPartisan){

	this.isPartisan = isPartisan;
    }//setIsPartisan

    public List<Candidate> getCandidates() throws EVException{

	if(candidates == null){
            if(isPersistent()) {

                candidates = getPersistenceLayer().restoreCandidateIsCandidateInElection(this);
            } else{

                throw new EVException( "Election object is not persistent");
            }//if
        }//if

        return candidates;
    }//getCandidates

    public void addCandidate(Candidate candidate) throws EVException{

    	getCandidates().add(candidate);
    }//addCandidate

    public void deleteCandidate(Candidate candidate) throws EVException{

	getPersistenceLayer().deleteCandidateIsCandidateInElection(candidate, this);
    }//deleteCandidate
}
