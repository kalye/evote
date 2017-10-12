package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.entity.ElectionsOfficer;

public class ElectionsOfficerImpl extends UserImpl implements ElectionsOfficer{

    public ElectionsOfficerImpl(){

	super();
    }//ElectionsOfficerImpl

    public ElectionsOfficerImpl(String firstName, String lastName, String userName, 
			 String password, String emailAddress, String address, String zipCode){

	super(firstName, lastName, userName, password, emailAddress, address, zipCode);
    }//ElectionsOfficerImpl   
}//ElectionsOfficerImpl
