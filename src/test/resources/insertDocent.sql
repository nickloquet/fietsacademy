insert into docenten(voornaam, familienaam, wedde, emailadres, geslacht, campusid)
values('testM', 'testM', 1000, 'testM@fietsacademy.be', 'MAN', (select id from campussen where naam='test'));
insert into docenten(voornaam, familienaam, wedde, emailadres, geslacht, campusid)
values('testV', 'testV', 1000, 'testV@fietsacademy.be', 'VROUW',(select id from campussen where naam='test'));
insert into docentenbijnamen(docentid, bijnaam)
VALUES((select id from docenten where voornaam='testM'), 'test');