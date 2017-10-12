package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.entity.User;
import edu.uga.cs.evote.persistence.impl.Persistent;

public abstract class UserImpl extends Persistent implements User{

	    private String firstName;
	    private String lastName;
	    private String userName;
	    private String password;
	    private String emailAddress;
	    private String address;
	    private String zipCode;
	
	    public UserImpl(){
	
	    	super(-1);
	    }//UserImpl
	    
	    public UserImpl(String firstName, String lastName, String userName,
		    String password, String emailAddress, String address, String zipCode){

	    	this();
	    	setFirstName(firstName);
	    	setLastName(lastName);
	    	setUserName(userName);
	    	setPassword(password);
	    	setEmailAddress(emailAddress);
	    	setAddress(address);
	    	setZipCode(zipCode);
	    }//UserImpl

    public String getFirstName(){

    	return firstName;
    }//getFirstName

    public void setFirstName(String firstName){

    	this.firstName = firstName;
    }//setFirstName

    public String getLastName(){

    	return lastName;
    }//getLastName

    public void setLastName(String lastName){

    	this.lastName = lastName;
    }//setLastName

    public String getUserName(){

    	return userName;
    }//getUserName

    public void setUserName(String userName){

    	this.userName = userName;
    }//setUserName

    public String getPassword(){

    	return password;
    }//getPassword

    public void setPassword(String password){

    	this.password = password;
    }//setPassword

    public String getEmailAddress(){

    	return emailAddress;
    }//getEmailAddress

    public void setEmailAddress(String emailAddress){

    	this.emailAddress = emailAddress;
    }//setEmailAddress

    public String getAddress(){

    	return address;
    }//getAddress

    public void setAddress(String address){

    	this.address = address;
    }//setAddress
    
    public String getZipCode(){
    	return zipCode;
    }
    
    public void setZipCode(String zipCode){
    	this.zipCode = zipCode;
    }
    
}//UserImpl

