#
# this SQL file populates the database with eVote system information
#

#
# Creating Electoral District
#

INSERT INTO electoralDistrict (id,
                              name)
                              VALUES (1,
                                     'Leaf Village');

#
# Creating Election Officer #1
#

INSERT INTO userPerson (id,
                        kind,
                        userName,
                        password,
                        email,
                        firstName,
                        lastName,
                        address,
			electoralDistrictid)
                        VALUES (1,
                               'electionsOfficer',
                               'BobSmith',
                               'abc123',
                               'bobsmith@gmail.com',
                               'Bob',
                               'Smith',
                               '123 Bob Smith Lane',
				1);

#
# Creating Election Officer #2
#
INSERT INTO userPerson (id,
                        kind,
                        userName,
                        password,
                        email,
                        firstName,
                        lastName,
                        address,
			electoralDistrictid)
                        VALUES (2,
                               'electionsOfficer',
                               'JoeBob',
                               'def456',
                               'joebob@gmail.com',
                               'Joe',
                               'Bob',
                               '456 Joe Bob Lane',
				1);


#
# Creating Voter #1
#
INSERT INTO userPerson (id,
                        kind,
                        userName,
                        password,
                        email,
                        firstName,
                        lastName,
                        address,
                        voterID,
			electoralDistrictid)
			VALUES(3,
                               'voter',
                               'SpongeBobSquarePants',
                               'krabbypatty',
                               'krustykrab@shellmail.com',
                               'SpongeBob',
                               'SquarePants',
                               '124 Conch Street, Bikini Bottom, Pacific Ocean',
                               3,
			       1);

#
# Creating Voter #2
#

INSERT INTO userPerson (id,
                        kind,
                        userName,
                        password,
                        email,
                        firstName,
                        lastName,
                        address,
                        voterID,
			electoralDistrictid)
                        VALUES (4,
                               	'voter',
                               	'SandyCheeks',
                               	'ihearttexas',
                               	'sandy@biggerintexas.com',
                               	'Sandy',
                               	'Cheeks',                                                 
                               	'Bikini Bottom, Pacific Ocean',
                               	4,
                               	1);


#
# Creating Political Party #1
#

INSERT INTO politicalParty(id,
                           name)
                           VALUES(1,
                                 'Republican');

#
# Creating Political Party #2
#

INSERT INTO politicalParty(id,
                           name)
                           VALUES(2,
                                 'Democratic');


#
# Creating Ballot #1
#


INSERT INTO ballot(id,
                   openDate,
                   closeDate,
                   approved,
                   electoralDistrictid)
                   VALUES (1,
                          '2016-10-07 10:56:00',
                          '2016-10-08 10:56:00',
                          true,
			  1);

#
# Creating Election #1
#

INSERT INTO ballotItem(id,
                       kind,
                       voteCount,
                       ballotid,
                       office,
		       isPartisan)
                       VALUES (1,
                              'election',
                              2,
                              1,
                              'Lord and Master of All Things Big and Small',
                              false);


INSERT INTO candidate(id,
                      name,
                      voteCount,
                      electionid)
                      VALUES (1,
                             'John Adams',
                             1,
			     1);

INSERT INTO candidate(id,
                      name,
                      voteCount,
                      electionid)
                      VALUES (2,
                             'Fella #2',
                             0,
			     1);

INSERT INTO candidate(id,
                      name,
                      voteCount,
                      electionid)
                      VALUES (3,
                             'Harambe',
                             1,
			     1);

#
# Creating Election #2
#

INSERT INTO ballotItem(id,
                       kind,
                       voteCount,
                       ballotid,
                       office,
                       isPartisan)
                       VALUES (2,
                              'election',
                              2,
			      1,
                              'Governor',
                              true);


INSERT INTO candidate(id,
                      name,
                      voteCount,
                      partyid,
                      electionid)
                      VALUES (4,
                             'Person who Governs',
                             1,
			     1,
			     2);


INSERT INTO candidate(id,
                      name,
                      voteCount,
                      partyid,
                      electionid)
                      VALUES (5,
                             'Governing Person',
                             0,
			     2,
			     2);


INSERT INTO candidate(id,
                      name,
                      voteCount,
                      partyid,
                      electionid)
                      VALUES (6,
                             'No Governing',
                             1,
			     1,
			     2);

#
# Creating Election #3
#
INSERT INTO ballotItem(id,
                       kind,
                       voteCount,
                       ballotid,
		       office,
                       isPartisan)
                       VALUES (3,
                              'election',
                              2,
			      1,
                              'Mayor',
                              true);


INSERT INTO candidate(id,
                      name,
                      voteCount,
                      partyid,
                      electionid)
                      VALUES (7,
                             'Mayor McMayorson',
                             0,
		   	     2,
			     3);

INSERT INTO candidate(id,
                      name,
                      voteCount,
                      partyid,
                      electionid)
                      VALUES (8,
                             'Maybe Mayor',
                             1,
			     2,
			     3);

INSERT INTO candidate(id,
                      name,
                      voteCount,
                      partyid,
                      electionid)
                      VALUES (9,
                             'Meh Mayor',
                             1,
			     1,
			     3);

#
# Creating Issue #1
#
INSERT INTO ballotItem(id,
                       kind,
                       voteCount,
                       ballotid,
                       question,
                       yesCount,
                       isPartisan)
                       VALUES (4,
                              'issue',
                              2,
			      1,
                              'Do you want free cookies in our city?',
                              1,
                              false);


#
# Creating Issue #2
#
INSERT INTO ballotItem(id,
                       kind,
                       voteCount,
                       ballotid,
                       question,
                       yesCount,
                       isPartisan)
                       VALUES (5,
                              'issue',
                              2,
			      1,
                              'Should we lower taxes?',
                              2,
                              false);


#
# Creating Issue #3
#
INSERT INTO ballotItem(id,
                       kind,
                       voteCount,
                       ballotid,
                       question,
		       yesCount,
                       isPartisan)
                       VALUES (6,
                              'issue',
                              2,
			      1,
                              'Should we provide free candy for people who pay taxes?',
                              2,
                              false);


#
# Creating Ballot #2
#


INSERT INTO ballot(id,
                   openDate,
                   closeDate,
                   approved,
                   electoralDistrictid)
                   VALUES (2,
                          '2016-10-14 13:00:00',
                          '2016-10-15 13:00:00',
                          true,
			  1);

#
# Creating Election #1
#

INSERT INTO ballotItem(id,
                       kind,
                       voteCount,
                       ballotid,
                       office,
		       isPartisan)
                       VALUES (7,
                              'election',
                              2,
			      2,
                              'President',
                              true);

INSERT INTO candidate(id,
                      name,
                      voteCount,
                      partyid,
                      electionid)
                      VALUES (10,
                             'Dean donttaxtherich Dumph',
                             0,
			     1,
			     7);

INSERT INTO candidate(id,
                      name,
                      voteCount,
                      partyid,
                      electionid)
                      VALUES (11,
                             'Hill bengazi Array',
                             1,
			     2,
			     7);

INSERT INTO candidate(id,
                      name,
                      voteCount,
                      partyid,
                      electionid)
                      VALUES (12,
                             'Someother Dude',
                             1,
			     2,
			     7);

#
# Creating Election #2
#

INSERT INTO ballotItem(id,
                       kind,
                       voteCount,
                       ballotid,
                       office,
                       isPartisan)
                       VALUES (8,
                              'election',
                              2,
			      2,
                              'Governor',
                              true);

INSERT INTO candidate(id,
                      name,
                      voteCount,
                      partyid,
                      electionid)
                      VALUES (13,
                             'Person that Governs',
                             1,
			     1,
			     8);

INSERT INTO candidate(id,
                      name,
                      voteCount,
                      partyid,
                      electionid)
                      VALUES (14,
                             'Governing Dude',
                             0,
			     2,
			     8);

INSERT INTO candidate(id,
                      name,
                      voteCount,
                      partyid,
                      electionid)
                      VALUES (15,
                             'Dude Governing',
                             1,
			     1,
			     8);

#
# Creating Election #3
#
INSERT INTO ballotItem(id,
                       kind,
                       voteCount,
                       ballotid,
		       office,
                       isPartisan)
                       VALUES (9,
                              'election',
                              2,
			      2,
                              'Mayor',
                              true);


INSERT INTO candidate(id,
                      name,
                      voteCount,
                      partyid,
                      electionid)
                      VALUES (16,
                             'Mayor McMayorson Dude',
                             1,
			     2,
			     9);

INSERT INTO candidate(id,
                      name,
                      voteCount,
                      partyid,
                      electionid)
                      VALUES (17,
                             'Maybe Mayor Girl',
                             1,
			     2,
			     9);

INSERT INTO candidate(id,
                      name,
                      voteCount,
                      partyid,
                      electionid)
                      VALUES (18,
                             'Meh Mayor Person',
                             0,
			     1,
			     9);

#
# Creating Issue #1
#
INSERT INTO ballotItem(id,
                       kind,
                       voteCount,
                       ballotid,
                       question,
                       yesCount,
                       isPartisan)
                       VALUES (10,
                              'issue',
                              2,
			      2,
                              'Should the city provide free transportation by unicycle?',
                              1,
                              false);


#
# Creating Issue #2
#
INSERT INTO ballotItem(id,
                       kind,
                       voteCount,
                       ballotid,
                       question,
                       yesCount,
                       isPartisan)
                       VALUES (11,
                              'issue',
                              2,
			      2,
                              'Should we make a new dog park?',
                              2,
                              false);

#
# Creating Issue #3
#
INSERT INTO ballotItem(id,
                       kind,
                       voteCount,
                       ballotid,
                       question,
		       yesCount,
                       isPartisan)
                       VALUES (12,
                              'issue',
                              2,
			      2,
                              'Should we change the name of the city to The City of Townsville?',
                              2,
                              false);

#
# Creating record of voting for both voters
#

INSERT INTO voteRecord (voterid,
			ballotid,
			date)
			VALUES (3,
				1,
				NOW());

INSERT INTO voteRecord (voterid,
			ballotid,
			date)
			VALUES (3,
				2,
				NOW());

INSERT INTO voteRecord (voterid,
			ballotid,
			date)
			VALUES (4,
				1,
				NOW());

INSERT INTO voteRecord (voterid,
			ballotid,
			date)
			VALUES (4,
				2,
				NOW());
