package edu.uga.cs.evote.entity.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.entity.Voter;

public class VoterImpl extends UserImpl implements Voter{

    private String voterId;
    private int age;
    private ElectoralDistrict electoralDistrict;
    List<VoteRecord> ballotVoteRecords;

    public VoterImpl(){

	super();
	electoralDistrict = null;
	ballotVoteRecords = null;
    }//VoterImpl

    public VoterImpl(String firstName, String lastName, String userName,
		     String password, String emailAddress, String address, String zipCode,
		     String voterId, int age, ElectoralDistrict electoralDistrict) throws EVException{

	super(firstName, lastName, userName,
	      password, emailAddress, address, zipCode);

	setVoterId(voterId);
	setAge(age);
	setElectoralDistrict(electoralDistrict);
	ballotVoteRecords = null;
    }//VoterImpl

   public String getVoterId(){

	return voterId;
    }//getVoterId

    public void setVoterId(String voterId){

	this.voterId = voterId;
    }//setVoterId

    public int getAge(){

	return age;
    }//getAge

    public void setAge(int age){

	this.age = age;
    }//setAge

    public ElectoralDistrict getElectoralDistrict() throws EVException{

	if(electoralDistrict == null){
            if(isPersistent()) {

                electoralDistrict = getPersistenceLayer().restoreVoterBelongsToElectoralDistrict(this);
            } else{

            	return null;
                //throw new EVException( "Voter object is not persistent");
            }//if
        }//if

        return electoralDistrict;
    }//getElectoralDistrict

    public void setElectoralDistrict(ElectoralDistrict electoralDistrict) throws EVException{

	this.electoralDistrict = electoralDistrict;
    }//setElectoralDistrict

    public List<VoteRecord> getBallotVoteRecords() throws EVException{

	if(ballotVoteRecords == null){
            if(isPersistent()) {

                ballotVoteRecords = getPersistenceLayer().restoreVoteRecord(new VoteRecordImpl(null, this, null));
            } else{

                throw new EVException( "Voter object is not persistent");
            }//if
        }//if

        return ballotVoteRecords;
    }//getBallotVoteRecords
}//VoterImpl

