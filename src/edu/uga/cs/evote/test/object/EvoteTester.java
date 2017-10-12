package edu.uga.cs.evote.test.object;

import edu.uga.cs.evote.EVException;

//import org.junit.Test;

/*import edu.uga.cs.evote.object.ObjectLayer;
import evote.src.edu.uga.clubs.object.impl.ObjectLayerImpl;
import evote.src.edu.uga.clubs.persistence.impl.DbUtils;
import evote.src.edu.uga.clubs.persistence.impl.PersistenceLayerImpl;
import evote.src.edu.uga.cs.evote.EVException;
import evote.src.edu.uga.cs.evote.entity.Ballot;
import evote.src.edu.uga.cs.evote.entity.BallotItem;
import evote.src.edu.uga.cs.evote.entity.Candidate;
import evote.src.edu.uga.cs.evote.entity.Election;
import evote.src.edu.uga.cs.evote.entity.ElectionsOfficer;
import evote.src.edu.uga.cs.evote.entity.ElectoralDistrict;
import evote.src.edu.uga.cs.evote.entity.Issue;
import evote.src.edu.uga.cs.evote.entity.PoliticalParty;
import evote.src.edu.uga.cs.evote.entity.VoteRecord;
import evote.src.edu.uga.cs.evote.entity.Voter;
import evote.src.edu.uga.cs.evote.persistence.PersistenceLayer;*/

public class EvoteTester {

    public static void main(String[] args) throws EVException{
	
	System.out.println("============================ Start Write Test =============================");	
	WriteTest.main(null);
	System.out.println("============================ Start Read Test ==============================");
	ReadTest.main(null);
	System.out.println("============================ Start Update Test ============================");
	UpdateTest.main(null);
	System.out.println("============================ Start Read Test ==============================");
	ReadTest.main(null);
	System.out.println("============================ Start Delete Test ============================");
	DeleteTest.main(null);
	System.out.println("============================ Start Read Test ==============================");
	ReadTest.main(null);
    }//main
}
