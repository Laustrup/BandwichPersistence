alter table subscriptions
    add column user_type enum(
        'ARTIST',
        'ORGANISATION_EMPLOYEE',
        'PARTICIPANT'
    ) not null;