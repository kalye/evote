select * from userPerson where kind = 'electionsOfficer';

select * from electoralDistrict;

select u.firstName, u.lastName, e.name from userPerson u, electoralDistrict e where u.electoralDistrictid = e.id and u.kind = 'voter';

select * from politicalParty;

select b.id, b.openDate, b.closeDate, b.approved, b.electoralDistrictid, bi.kind, bi.voteCount, bi.question, bi.yesCount, bi.office, bi.isPartisan from ballot b, ballotItem bi where b.id = bi.ballotid;

select b.id, b.openDate, b.closeDate, b.approved, b.electoralDistrictid, bi.kind, bi.voteCount, bi.question, bi.yesCount, bi.office, bi.isPartisan from ballot b, ballotItem bi where b.id = bi.ballotid and bi.kind = 'issue';
select b.id, b.openDate, b.closeDate, b.approved, b.electoralDistrictid, bi.kind, bi.voteCount, bi.question, bi.yesCount, bi.office, bi.isPartisan, c.name from ballot b, ballotItem bi, candidate c where b.id = bi.ballotid and bi.kind = 'election' and c.electionid = b.id and bi.isPartisan = false;
select b.id, b.openDate, b.closeDate, b.approved, b.electoralDistrictid, bi.kind, bi.voteCount, bi.question, bi.yesCount, bi.office, bi.isPartisan, c.name, p.name from ballot b, ballotItem bi, candidate c, politicalParty p where b.id = bi.ballotid and bi.kind = 'election' and c.electionid = bi.id and c.partyid = p.id and bi.isPartisan = true;
select b.id, bi.kind, bi.voteCount, bi.question, bi.yesCount, bi.office, bi.isPartisan from ballot b, ballotItem bi where b.id = bi.ballotid and bi.kind = 'issue';
#or select b.id, bi.kind, bi.voteCount, bi.question, bi.yesCount, count(v.ballotid) from ballot b, ballotItem bi, voteRecord v where b.id = bi.ballotid  and v.ballotid and bi.kind = 'issue' group by v.ballotid;
select b.id, bi.kind, bi.voteCount, bi.office, bi.isPartisan, c.voteCount as candidateVotes, c.name from ballot b, ballotItem bi, candidate c where b.id = bi.ballotid and bi.id = c.electionid and bi.kind = 'election';
