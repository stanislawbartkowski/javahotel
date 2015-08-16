create table numhistory (num numeric(12,2), numkey varchar(20));

insert into numhistory values(1.1,10);
insert into numhistory values(1.2,10);
insert into numhistory values(1.3,10);
insert into numhistory values(1.4,10);
insert into numhistory values(1.55,10);
insert into numhistory values(1.66,10);

create table blobtable (id number(12), filename varchar2(100), fileb blob);


create table personal_data (id integer, name varchar(100));
