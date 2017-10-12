package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;

public class LoginCtrl
{ 
    private ObjectLayer objectLayer = null;
    
    public LoginCtrl( ObjectLayer objectLayer )
    {
        this.objectLayer = objectLayer;
    }
    
    public String login( Session session, String userName, String password )
            throws EVException
    {
        String ssid = null;
        
        Voter modelVoter = objectLayer.createVoter();
        ElectionsOfficer modelElectionsOfficer = objectLayer.createElectionsOfficer();
        
        modelVoter.setUserName(userName);
        modelElectionsOfficer.setUserName( userName );

        List<Voter> voters;
        List<ElectionsOfficer> electionsOfficers;
        voters = objectLayer.findVoter( modelVoter );
        electionsOfficers = objectLayer.findElectionsOfficer(modelElectionsOfficer);

        if( voters.size() > 0) {
        	Voter person;
            person = voters.get( 0 );

            if(!person.getPassword().equals(password)){
            	throw new EVException("invalid password");
            }
            
            session.setUser( person );
            ssid = SessionManager.storeSession( session );
        } else if(electionsOfficers.size() > 0){
        	ElectionsOfficer person = electionsOfficers.get(0);
        	
        	if(!person.getPassword().equals(password)){
            	throw new EVException("invalid password");
            }
        	
        	session.setUser(person);
        	ssid = SessionManager.storeSession(session);
        }else
            throw new EVException( "SessionManager.login: Invalid User Name or Password" );
        
        return ssid;
    }
}
