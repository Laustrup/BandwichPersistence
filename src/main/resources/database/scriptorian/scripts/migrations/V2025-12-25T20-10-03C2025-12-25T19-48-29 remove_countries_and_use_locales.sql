alter table contact_info
    drop constraint FK_contact_info__countries,
    drop column country_id,
    add column locale varchar(8) not null after email
;

drop table countries;
