 #
 # this SQL file creates the schema for the clubs database
 #

 # remove the existing tables
 #
 DROP TABLE IF EXISTS candidate;
 DROP TABLE IF EXISTS politicalParty;
 DROP TABLE IF EXISTS voteRecord;
 DROP TABLE IF EXISTS ballotItem;
 DROP TABLE IF EXISTS ballot;
 DROP TABLE IF EXISTS userPerson;
 DROP TABLE IF EXISTS electoralDistrict;
 
 #
 # Table definition for table 'electoralDistrict'
 #
 CREATE TABLE electoralDistrict(
        id         INT UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
        name       VARCHAR(255) NOT NULL,
        zipCode	   VARCHAR(255) NOT NULL
 ) ENGINE=InnoDB;

 #
 # Table definition for table 'userPerson'
 #
 CREATE TABLE userPerson (
	id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	kind	    ENUM('voter', 'electionsOfficer') NOT NULL,
	userName    VARCHAR(255) NOT NULL UNIQUE,
	password    VARCHAR(255) NOT NULL,
	email       VARCHAR(255) NOT NULL,
	firstName   VARCHAR(255) NOT NULL,
	lastName    VARCHAR(255) NOT NULL,
	address     VARCHAR(255) NOT NULL,
	zipCode		VARCHAR(255) NOT NULL,
	voterID	    VARCHAR(255),	
	age	    INT UNSIGNED,
	electoralDistrictid INT UNSIGNED,

	FOREIGN KEY(electoralDistrictid) REFERENCES electoralDistrict(id)
 ) ENGINE=InnoDB;

 #
 # Table definition for table 'ballot'
 #
CREATE TABLE ballot(
        id         INT UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
        openDate   DATETIME NOT NULL,
        closeDate  DATETIME NOT NULL,
        approved   BOOLEAN NOT NULL,
        electoralDistrictid INT UNSIGNED NOT NULL,
	
        FOREIGN KEY(electoralDistrictid) REFERENCES electoralDistrict(id)
 ) ENGINE=InnoDB;

 #
 # Table definition for table 'ballotItem'
 #
 CREATE TABLE ballotItem (
	id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	kind	    ENUM('election', 'issue') NOT NULL,
	voteCount   INT UNSIGNED,
	ballotid    INT UNSIGNED NOT NULL,
	question    VARCHAR(255),
	yesCount    INT UNSIGNED,
	office 	    VARCHAR(255),
	isPartisan  BOOLEAN NOT NULL,

	FOREIGN KEY(ballotid) REFERENCES ballot(id)
 ) ENGINE=InnoDB;

 #
 # Table definition for table 'politicalParty'
 #
 CREATE TABLE politicalParty(
	id		INT UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	name		VARCHAR(255) NOT NULL
 ) ENGINE=InnoDB;

 #
 # Table definition for table 'candidate'
 #
 CREATE TABLE candidate (
	id		INT UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	name		VARCHAR(255) NOT NULL,
	voteCount	INT UNSIGNED,
	partyid		INT UNSIGNED,
	electionid	INT UNSIGNED NOT NULL,

	FOREIGN KEY(partyid) REFERENCES politicalParty(id),
	FOREIGN KEY(electionid) REFERENCES ballotItem(id)
 ) ENGINE=InnoDB;
  
 #
 # Table definition for table 'voteRecord', which
 # is a many-to-many relationship
 # between voter and ballot
 #
 CREATE TABLE voteRecord (
	id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	voterid     INT UNSIGNED NOT NULL,
	ballotid    INT UNSIGNED NOT NULL,
	date	    DATETIME,

	FOREIGN KEY(voterid) REFERENCES userPerson(id),
	FOREIGN KEY(ballotid) REFERENCES ballot(id)
 ) ENGINE=InnoDB;

INSERT INTO userPerson(kind, username, password, email, firstName, lastName, address)
	VALUES("electionsOfficer", "team4", "team4", "team4@uga.edu", "Team", "Four", "UGA");
	
INSERT INTO electoralDistrict(name, zipCode)
	VALUES("Little Whinging", "12345");
	
INSERT INTO politicalParty(name)
	VALUES("No Party");
