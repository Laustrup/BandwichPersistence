drop table event_history;
drop table event_history_details;
drop table artist_history;
drop table artist_history;
drop table organisation_employee_history;
drop table organisation_employee_history_details;

alter table events
    add column `title` varchar(64) not null;

alter table gigs
    add column `title` varchar(64) not null;

alter table artist_event_participations
    change implementation `type` enum('ACCEPTED', 'INTERESTED', 'CANCELED', 'INVITED');

alter table organisation_employee_event_participations
    change implementation `type` enum('ACCEPTED', 'INTERESTED', 'CANCELED', 'INVITED');