package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.object.ObjectLayer;

public class ElectionsOfficerCtrl {

	private ObjectLayer objectLayer = null;
	
	public ElectionsOfficerCtrl(ObjectLayer objectLayer) {
		this.objectLayer = objectLayer;
	}

	public long createElectionsOfficer(String firstName, String lastName, String userName, String password,
			String emailAddress, String address, String zipCode) throws EVException {
		
		ElectionsOfficer				electionsOfficer = null;
		
		electionsOfficer = objectLayer.createElectionsOfficer( 	firstName, lastName, userName, 
																password, emailAddress, address, zipCode );
        objectLayer.storeElectionsOfficer( electionsOfficer );

        return electionsOfficer.getId();
	}
	public void updateElectionOfficer(ElectionsOfficer electionsOfficer) throws EVException{
		objectLayer.storeElectionsOfficer(electionsOfficer);
	}
	public List<ElectionsOfficer> findAllElectionsOfficers()
            throws EVException
    {
        List<ElectionsOfficer> 	electionsOfficers  = null;
        
        electionsOfficers = objectLayer.findElectionsOfficer( null );

        return electionsOfficers;
    }
}
