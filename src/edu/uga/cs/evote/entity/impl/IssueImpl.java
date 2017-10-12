package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Issue;

public class IssueImpl extends BallotItemImpl implements Issue{
   
    private String question;
    private int yesCount = 0;

    public IssueImpl(){

	super();
	setKind("issue");
    }//IssueImpl

    public IssueImpl(String question, Ballot ballot) throws EVException{

	super(ballot);
	setQuestion(question);
	setKind("issue");
    }//IssueImpl

    @Override
	public String getQuestion(){

	return question;
    }//getQuestion

    public void setQuestion(String question){

	this.question = question;
    }//setQuestion

    public int getYesCount(){

	return yesCount;
    }//getYesCount

    public void setYesCount(int yesCount) throws EVException{

	if(yesCount >= 0){

	    this.yesCount = yesCount;
	} else{

	    throw new EVException("setYesCount invalid");
	}//if
    }//setYesCount

    public int getNoCount(){

    	int noCount = super.getVoteCount() - yesCount;

	return noCount;
    }//getNoCount

    public void addYesVote(){

    	super.addVote();
    	yesCount++;
    }//addYesVote

    public void addNoVote(){

    	voteCount++;
    }//addNoVote
}//IssueImpl
