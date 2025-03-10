alter table artists
    add column password varchar(128) not null after contact_info_id;

alter table organisation_employees
    add column password varchar(128) not null after contact_info_id;