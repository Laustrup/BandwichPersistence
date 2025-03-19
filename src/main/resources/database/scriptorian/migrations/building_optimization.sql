alter table subscriptions
    add column user_type enum(
        'ARTIST',
        'ORGANISATION_EMPLOYEE',
        'PARTICIPANT'
    ) not null;

create table not_booleans(
    id binary(16) not null,
    argument enum(
        'FALSE',
        'TRUE',
        'UNDEFINED',
        'BELOW_HALF',
        'ABOVE_HALF'
    ) not null,
    message varchar(256),

    constraint PK_not_booleans
        primary key (id)
);

alter table events
    add column is_charity binary(16) after address_id,
    add constraint FK_events__is_charity
        foreign key (is_charity)
            references not_booleans (id)
                on update cascade
                on delete set null;