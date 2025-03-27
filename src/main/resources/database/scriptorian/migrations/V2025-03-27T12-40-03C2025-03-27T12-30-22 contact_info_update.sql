alter table countries
    modify column code varchar(4) not null
;

alter table phones
    add column country_digits int(4) not null after contact_info_id,
    drop constraint FK_phones__countries,
    drop column country_id
;