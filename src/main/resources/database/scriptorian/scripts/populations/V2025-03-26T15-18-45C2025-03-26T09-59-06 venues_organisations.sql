-- --
-- This script generates some venues and organisations to use for development
-- --

set
    -- works with 123
    -- password = 'password'
    @password = '$2y$05$Ad01b.9//2.NKwsL/6y/HeWk3TgdMDve4ThnFPKt.5FMfP2GHbRke£d01',

    -- Iværksted
    @address_id_vaerkstedsvej = 0x1111111111111111a111111111111111,
    @contact_info_id_ivaerksted = 0x1111111111111111a111111111111112,
    
    @ivaerksted_jens_id = 0x1111111111111111a111111111111113,
        @contact_info_jens_ivaerksted = 'jens@ivaerkstedet.dk',
    @ivaerksted_birthe_id = 0x1111111111111111a111111111111114,
        @contact_info_birthe_ivaerksted = 'birthe@ivaerkstedet.dk',

    -- Arena
    @address_id_arenavej = 0x1111111111111111a111111111111115,
    @contact_info_id_arena = 0x1111111111111111a111111111111116,

    @arena_john_id = 0x1111111111111111a111111111111117,
        @contact_info_john_arena = 'john@arena.com',
    @arena_james_id = 0x1111111111111111a111111111111118,
        @contact_info_james_arena = 'james@arena.com',
    @arena_joanna_id = 0x1111111111111111a111111111111119,
        @contact_info_joanna_arena = 'joanna@arena.com',
    @arena_susanne_id = 0x1111111111111111a111111111111121,
        @contact_info_susanne_arena = 'susanne@arena.com',
    @arena_hans_id = 0x1111111111111111a111111111111131,
        @contact_info_hans_arena = 'hans@arena.com',
    
    -- Plural Place
    @address_id_jensenvej = 0x1111111111111111a111111111111141,
        -- Twogether
        @contact_info_id_twogether = 0x1111111111111111a111111111111151,

        @twogether_jimmy_id = 0x1111111111111111a111111111111161,
            @contact_info_jimmy_twogether = 'jimmy@twogether.com',
        @twogether_hanne_id = 0x1111111111111111a111111111111171,
            @contact_info_hanne_twogether = 'hanne@twogether.com',
        @twogether_xi_id = 0x1111111111111111a111111111111181,
            @contact_info_xi_twogether = 'xi@twogether.com',
        
        -- Jamsters
        @contact_info_id_jamsters = 0x1111111111111111a111111111111191,

        @jamsters_tue_id = 0x1111111111111111a111111111111211,
            @contact_info_tue_jamsters = 'tue@jamsters.dk'
;

insert into addresses(id, street, floor, municipality, zip, city) values (
    @address_id_vaerkstedsvej,
    'Værkstedsvej 57',
    null,
    'Sjælland',
    '4600',
    'Køge'
), (
    @address_id_arenavej,
    'Arenavej 2',
    null,
    'København',
    '2100',
    'København Ø'
), (
    @address_id_jensenvej,
    'Jensensvej 42A',
    '1. th.',
    'Fyn',
    '5000',
    'Odense C'
);

insert into contact_info(id, address_id, email, locale) values (
    @contact_info_id_ivaerksted,
    @address_id_vaerkstedsvej,
    'contact@ivaerkstedet.dk',
    'da_DK'
), (
    unhex(md5(uuid())),
    @address_id_vaerkstedsvej,
    @contact_info_jens_ivaerksted,
    'da_DK'
), (
    unhex(md5(uuid())),
    @address_id_vaerkstedsvej,
    @contact_info_birthe_ivaerksted,
    'da_DK'
), (
    @contact_info_id_arena,
    @address_id_arenavej,
    'contact@arena.com',
    'da_DK'
), (
    unhex(md5(uuid())),
    @address_id_arenavej,
    @contact_info_hans_arena,
    'da_DK'
), (
    unhex(md5(uuid())),
    @address_id_arenavej,
    @contact_info_james_arena,
    'da_DK'
), (
    unhex(md5(uuid())),
    @address_id_arenavej,
    @contact_info_joanna_arena,
    'da_DK'
), (
    unhex(md5(uuid())),
    @address_id_arenavej,
    @contact_info_john_arena,
    'da_DK'
), (
    unhex(md5(uuid())),
    @address_id_arenavej,
    @contact_info_susanne_arena,
    'da_DK'
), (
    @contact_info_id_twogether,
    @address_id_jensenvej,
    'contact@twogether.com',
    'da_DK'
), (
    unhex(md5(uuid())),
    @address_id_jensenvej,
    @contact_info_hanne_twogether,
    'da_DK'
), (
    unhex(md5(uuid())),
    @address_id_jensenvej,
    @contact_info_jimmy_twogether,
    'da_DK'
), (
    unhex(md5(uuid())),
    @address_id_jensenvej,
    @contact_info_xi_twogether,
    'da_DK'
), (
    @contact_info_id_jamsters,
    @address_id_jensenvej,
    'contact@jamsters.dk',
    'da_DK'
), (
    unhex(md5(uuid())),
    @address_id_jensenvej,
    @contact_info_tue_jamsters,
    'da_DK'
);

insert into phones(id, contact_info_id, country_digits, numbers, is_mobile, is_business) values (
    unhex(md5(uuid())),
    @contact_info_id_ivaerksted,
    45,
    12345678,
    true,
    false
), (
    unhex(md5(uuid())),
    @contact_info_id_arena,
    45,
    87654321,
    false,
    true
), (
    unhex(md5(uuid())),
    @contact_info_id_twogether,
    45,
    76543210,
    true,
    false
), (
    unhex(md5(uuid())),
    @contact_info_id_jamsters,
    46,
    01234567,
    false,
    false
);

insert into subscriptions(id, status, kind, user_type) VALUES (
    @ivaerksted_jens_id,
    'ACCEPTED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
), (
    @ivaerksted_birthe_id,
    'PENDING',
    'FREE',
    'ORGANISATION_EMPLOYEE'
), (
    @arena_john_id,
    'ACCEPTED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
), (
    @arena_james_id,
    'ACCEPTED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
), (
    @arena_joanna_id,
    'ACCEPTED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
), (
    @arena_susanne_id,
    'ACCEPTED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
), (
    @arena_hans_id,
    'ACCEPTED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
), (
    @twogether_hanne_id,
    'ACCEPTED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
), (
    @twogether_jimmy_id,
    'ACCEPTED',
    'FREE',
    'ORGANISATION_EMPLOYEE'
), (
    @twogether_xi_id,
    'SUSPENDED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
), (
    @jamsters_tue_id,
    'ACCEPTED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
);

insert into venues(id, address_id, title, description, stage_setup, size) values (
    unhex(md5(uuid())),
    @address_id_vaerkstedsvej,
    'Iværkstedet',
    'This is a smaller venue',
    'The stage is very tiny and has only room for top 5 band members',
    54
), (
    unhex(md5(uuid())),
    @address_id_arenavej,
    'Arena',
    'A big venue',
    'Big place for up 500 musicians, there is the necessarily gear for sound crew and backstage available',
    2000
), (
    unhex(md5(uuid())),
    @address_id_jensenvej,
    'Plural Place',
    'A special venue, that is meant for plural amount of organisations',
    '',
    121
);

insert into organisations(id, contact_info_id, title) values (
    unhex(md5(uuid())),
    @contact_info_id_ivaerksted,
    'Iværksted'
), (
    unhex(md5(uuid())),
    @contact_info_id_arena,
    'Arena'
), (
    unhex(md5(uuid())),
    @contact_info_id_twogether,
    'Twogether'
), (
    unhex(md5(uuid())),
    @contact_info_id_jamsters,
    'Jamsters'
);

insert into organisation_employees(
    id,
    contact_info_id,
    password,
    username,
    first_name,
    last_name,
    description
) values (
    @ivaerksted_jens_id,
    (select id from contact_info where email = @contact_info_jens_ivaerksted limit 1),
    @password,
    'jens',
    'Jens',
    'Jensen',
    'Jeg hedder Jens'
), (
    @ivaerksted_birthe_id,
    (select id from contact_info where email = @contact_info_birthe_ivaerksted limit 1),
    @password,
    'birthe',
    'Birthe',
    'Berthelsen',
    'Jeg hedder Birthe'
), (
    @arena_hans_id,
    (select id from contact_info where email = @contact_info_hans_arena limit 1),
    @password,
    'hans',
    'Hans',
    'Hansen',
    'Jeg hedder Hans'
), (
    @arena_susanne_id,
    (select id from contact_info where email = @contact_info_susanne_arena limit 1),
    @password,
    'susanne',
    'Susanne',
    'Simonsen',
    'Jeg hedder Susanne'
), (
    @arena_joanna_id,
    (select id from contact_info where email = @contact_info_joanna_arena limit 1),
    @password,
    'joanna',
    'Joanna Edel',
    'Johansen',
    'Jeg hedder Joanna'
), (
    @arena_john_id,
    (select id from contact_info where email = @contact_info_john_arena limit 1),
    @password,
    'john',
    'John',
    'Johnson',
    'Jeg hedder John'
), (
    @arena_james_id,
    (select id from contact_info where email = @contact_info_james_arena limit 1),
    @password,
    'james',
    'James',
    'Jamerson',
    'Jeg hedder James'
), (
    @twogether_xi_id,
    (select id from contact_info where email = @contact_info_xi_twogether limit 1),
    @password,
    'xi',
    'Xi',
    'Xang',
    'Jeg hedder Xi'
), (
    @twogether_jimmy_id,
    (select id from contact_info where email = @contact_info_jimmy_twogether limit 1),
    @password,
    'jimmy',
    'Jimmy',
    'Jensen',
    'Jeg hedder Jimmy'
), (
    @twogether_hanne_id,
    (select id from contact_info where email = @contact_info_hanne_twogether limit 1),
    @password,
    'hanne',
    'Hanne',
    'Hansen',
    'Jeg hedder Hanne'
), (
    @jamsters_tue_id,
    (select id from contact_info where email = @contact_info_tue_jamsters limit 1),
    @password,
    'tue',
    'Tue',
    'Tirsdag',
    'Jeg hedder Tue'
);

insert into organisation_employments(organisation_id, organisation_employee_id, role) values (
    (select id from organisations where contact_info_id = @contact_info_id_ivaerksted),
    (select id from organisation_employees where id = @ivaerksted_jens_id),
    'LEADER'
), (
    (select id from organisations where contact_info_id = @contact_info_id_ivaerksted),
    (select id from organisation_employees where id = @ivaerksted_birthe_id),
    'PR'
), (
    (select id from organisations where contact_info_id = @contact_info_id_arena),
    (select id from organisation_employees where id = @arena_hans_id),
    'LEADER'
), (
    (select id from organisations where contact_info_id = @contact_info_id_arena),
    (select id from organisation_employees where id = @arena_susanne_id),
    'PR'
), (
    (select id from organisations where contact_info_id = @contact_info_id_arena),
    (select id from organisation_employees where id = @arena_joanna_id),
    'BOOKER'
), (
    (select id from organisations where contact_info_id = @contact_info_id_arena),
    (select id from organisation_employees where id = @arena_john_id),
    'BOOKER'
), (
    (select id from organisations where contact_info_id = @contact_info_id_arena),
    (select id from organisation_employees where id = @arena_james_id),
    'BOOKER'
), (
    (select id from organisations where contact_info_id = @contact_info_id_twogether),
    (select id from organisation_employees where id = @twogether_xi_id),
    'BOOKER'
), (
    (select id from organisations where contact_info_id = @contact_info_id_twogether),
    (select id from organisation_employees where id = @twogether_jimmy_id),
    'PR'
), (
    (select id from organisations where contact_info_id = @contact_info_id_twogether),
    (select id from organisation_employees where id = @twogether_hanne_id),
    'LEADER'
), (
    (select id from organisations where contact_info_id = @contact_info_id_jamsters),
    (select id from organisation_employees where id = @jamsters_tue_id),
    'LEADER'
);