package edu.uga.cs.evote.entity.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.persistence.impl.Persistent;

public class ElectoralDistrictImpl extends Persistent implements ElectoralDistrict{
 
    private String name;
    private List<Voter> voters;
    private List<Ballot> ballots;
	private String zipCode;

    public ElectoralDistrictImpl(){
    	super(-1);
    	voters = null;
    	ballots = null;
    }//ElectoralDistrictImpl

    public ElectoralDistrictImpl(String name, String zipCode){

    	super(-1);
    	voters = null;
    	ballots = null;
    	setName(name);
    	setZipCode(zipCode);
    }//ElectoralDistrictImpl

    public void setZipCode(String zipCode) {
		
    	this.zipCode = zipCode;
	}

	public String getName(){

    	return name;
    }//getName

    public void setName(String name){

    	this.name = name;
    }//setName

    public List<Voter> getVoters() throws EVException{
    	
    	if(voters == null){
            if(isPersistent()) {

                voters = getPersistenceLayer().restoreVoterBelongsToElectoralDistrict(this);
            } else{

                throw new EVException( "ElectoralDistrict object is not persistent");
            }//if
        }//if

        return voters;
    }//getVoters

    public List<Ballot> getBallots() throws EVException{
    	if(ballots == null){
            if(isPersistent()) {

                ballots = getPersistenceLayer().restoreElectoralDistrictHasBallotBallot(this);
            } else{

                throw new EVException( "Election object is not persistent");
            }//if
        }//if

        return ballots;
    }//getBallots

    public void addBallot(Ballot ballot) throws EVException{

    	getPersistenceLayer().storeElectoralDistrictHasBallotBallot(this, ballot);
    }//addBallot

    public void deleteBallot(Ballot ballot) throws EVException{

    	getPersistenceLayer().deleteElectoralDistrictHasBallotBallot(this, ballot);
    }//deleteBallot

	@Override
	public String getZipCode() {
		
		return zipCode;
	}
}//ElectoralDistrictImpl
