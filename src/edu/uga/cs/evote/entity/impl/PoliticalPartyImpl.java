package edu.uga.cs.evote.entity.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.persistence.impl.Persistent;

public class PoliticalPartyImpl extends Persistent implements PoliticalParty{

    private String name;
    private List<Candidate> candidates;

    public PoliticalPartyImpl(){

	super(-1);
	candidates = null;
    }//PoliticalPartyImpl

    public PoliticalPartyImpl(String name){

	this();
	setName(name);
    }//PoliticalPartyImpl

    public String getName(){

	return name;
    }//getName

    public void setName(String name){

	this.name = name;
    }//setName

    public List<Candidate> getCandidates() throws EVException{

	if(candidates == null){
            if(isPersistent()) {

                candidates = getPersistenceLayer().restoreCandidateIsMemberOfPoliticalParty(this);
            } else{

                throw new EVException( "PoliticalParty object is not persistent");
            }//if
        }//if

        return candidates;
    }//getCandidates
}//PoliticalPartyImpl


