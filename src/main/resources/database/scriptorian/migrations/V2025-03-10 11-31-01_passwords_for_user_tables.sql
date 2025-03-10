alter table artists
    add column password varchar(128) not null;

alter table organisation_employees
    add column password varchar(128) not null;