alter table venues
    add column title varchar(64) not null after address_id;