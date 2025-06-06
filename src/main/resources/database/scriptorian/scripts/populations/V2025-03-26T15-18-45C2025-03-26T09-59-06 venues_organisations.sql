-- --
-- This script generates some venues and organisations to use for development
-- --

set
    -- works with 123
    -- password = 'password'
    @password = '$2y$05$Ad01b.9//2.NKwsL/6y/HeWk3TgdMDve4ThnFPKt.5FMfP2GHbRke£d01',

    -- Iværksted
    @address_id_vaerkstedsvej = unhex(md5(uuid())),
    @contact_info_id_ivaerksted = unhex(md5(uuid())),
    
    @subscription_id_ivaerksted_jens = unhex(md5(uuid())),
        @contact_info_jens_ivaerksted = 'jens@ivaerkstedet.dk',
    @subscription_id_ivaerksted_birthe = unhex(md5(uuid())),
        @contact_info_birthe_ivaerksted = 'birthe@ivaerkstedet.dk',

    -- Arena
    @address_id_arenavej = unhex(md5(uuid())),
    @contact_info_id_arena = unhex(md5(uuid())),
    
    @subscription_id_arena_john = unhex(md5(uuid())),
        @contact_info_john_arena = 'john@arena.com',
    @subscription_id_arena_james = unhex(md5(uuid())),
        @contact_info_james_arena = 'james@arena.com',
    @subscription_id_arena_joanna = unhex(md5(uuid())),
        @contact_info_joanna_arena = 'joanna@arena.com',
    @subscription_id_arena_susanne = unhex(md5(uuid())),
        @contact_info_susanne_arena = 'susanne@arena.com',
    @subscription_id_arena_hans = unhex(md5(uuid())),
        @contact_info_hans_arena = 'hans@arena.com',
    
    -- Plural Place
    @address_id_jensenvej = unhex(md5(uuid())),
        -- Twogether
        @contact_info_id_twogether = unhex(md5(uuid())),

        @subscription_id_twogether_jimmy = unhex(md5(uuid())),
            @contact_info_jimmy_twogether = 'jimmy@twogether.com',
        @subscription_id_twogether_hanne = unhex(md5(uuid())),
            @contact_info_hanne_twogether = 'hanne@twogether.com',
        @subscription_id_twogether_xi = unhex(md5(uuid())),
            @contact_info_xi_twogether = 'xi@twogether.com',
        
        -- Jamsters
        @contact_info_id_jamsters = unhex(md5(uuid())),

        @subscription_id_jamsters_tue = unhex(md5(uuid())),
            @contact_info_tue_jamsters = 'tue@jamsters.dk',

    -- Denmark
    @country_id_denmark = unhex(md5(uuid())),

    -- Sweden
    @country_id_sweden = unhex(md5(uuid()))
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

insert into countries(id, title, code) VALUES (
    @country_id_denmark,
    'Denmark',
    'DK'
), (
    @country_id_sweden,
    'Sweden',
    'SE'
);

insert into contact_info(id, address_id, country_id, email) values (
    @contact_info_id_ivaerksted,
    @address_id_vaerkstedsvej,
    @country_id_denmark,
    'contact@ivaerkstedet.dk'
), (
    unhex(md5(uuid())),
    @address_id_vaerkstedsvej,
    @country_id_denmark,
    @contact_info_jens_ivaerksted
), (
    unhex(md5(uuid())),
    @address_id_vaerkstedsvej,
    @country_id_denmark,
    @contact_info_birthe_ivaerksted
), (
    @contact_info_id_arena,
    @address_id_arenavej,
    @country_id_denmark,
    'contact@arena.com'
), (
    unhex(md5(uuid())),
    @address_id_arenavej,
    @country_id_denmark,
    @contact_info_hans_arena
), (
    unhex(md5(uuid())),
    @address_id_arenavej,
    @country_id_denmark,
    @contact_info_james_arena
), (
    unhex(md5(uuid())),
    @address_id_arenavej,
    @country_id_denmark,
    @contact_info_joanna_arena
), (
    unhex(md5(uuid())),
    @address_id_arenavej,
    @country_id_denmark,
    @contact_info_john_arena
), (
    unhex(md5(uuid())),
    @address_id_arenavej,
    @country_id_denmark,
    @contact_info_susanne_arena
), (
    @contact_info_id_twogether,
    @address_id_jensenvej,
    @country_id_denmark,
    'contact@twogether.com'
), (
    unhex(md5(uuid())),
    @address_id_jensenvej,
    @country_id_denmark,
    @contact_info_hanne_twogether
), (
    unhex(md5(uuid())),
    @address_id_jensenvej,
    @country_id_denmark,
    @contact_info_jimmy_twogether
), (
    unhex(md5(uuid())),
    @address_id_jensenvej,
    @country_id_denmark,
    @contact_info_xi_twogether
), (
    @contact_info_id_jamsters,
    @address_id_jensenvej,
    @country_id_denmark,
    'contact@jamsters.dk'
), (
    unhex(md5(uuid())),
    @address_id_jensenvej,
    @country_id_denmark,
    @contact_info_tue_jamsters
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
    @subscription_id_ivaerksted_jens,
    'ACCEPTED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
), (
    @subscription_id_ivaerksted_birthe,
    'PENDING',
    'FREE',
    'ORGANISATION_EMPLOYEE'
), (
    @subscription_id_arena_john,
    'ACCEPTED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
), (
    @subscription_id_arena_james,
    'ACCEPTED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
), (
    @subscription_id_arena_joanna,
    'ACCEPTED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
), (
    @subscription_id_arena_susanne,
    'ACCEPTED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
), (
    @subscription_id_arena_hans,
    'ACCEPTED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
), (
    @subscription_id_twogether_hanne,
    'ACCEPTED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
), (
    @subscription_id_twogether_jimmy,
    'ACCEPTED',
    'FREE',
    'ORGANISATION_EMPLOYEE'
), (
    @subscription_id_twogether_xi,
    'SUSPENDED',
    'PAYING',
    'ORGANISATION_EMPLOYEE'
), (
    @subscription_id_jamsters_tue,
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
    subscription_id,
    contact_info_id,
    password,
    username,
    first_name,
    last_name,
    description
) values (
    unhex(md5(uuid())),
    @subscription_id_ivaerksted_jens,
    (select id from contact_info where email = @contact_info_jens_ivaerksted limit 1),
    @password,
    'jens',
    'Jens',
    'Jensen',
    'Jeg hedder Jens'
), (
    unhex(md5(uuid())),
    @subscription_id_ivaerksted_birthe,
    (select id from contact_info where email = @contact_info_birthe_ivaerksted limit 1),
    @password,
    'birthe',
    'Birthe',
    'Berthelsen',
    'Jeg hedder Birthe'
), (
    unhex(md5(uuid())),
    @subscription_id_arena_hans,
    (select id from contact_info where email = @contact_info_hans_arena limit 1),
    @password,
    'hans',
    'Hans',
    'Hansen',
    'Jeg hedder Hans'
), (
    unhex(md5(uuid())),
    @subscription_id_arena_susanne,
    (select id from contact_info where email = @contact_info_susanne_arena limit 1),
    @password,
    'susanne',
    'Susanne',
    'Simonsen',
    'Jeg hedder Susanne'
), (
    unhex(md5(uuid())),
    @subscription_id_arena_joanna,
    (select id from contact_info where email = @contact_info_joanna_arena limit 1),
    @password,
    'joanna',
    'Joanna Edel',
    'Johansen',
    'Jeg hedder Joanna'
), (
    unhex(md5(uuid())),
    @subscription_id_arena_john,
    (select id from contact_info where email = @contact_info_john_arena limit 1),
    @password,
    'john',
    'John',
    'Johnson',
    'Jeg hedder John'
), (
    unhex(md5(uuid())),
    @subscription_id_arena_james,
    (select id from contact_info where email = @contact_info_james_arena limit 1),
    @password,
    'james',
    'James',
    'Jamerson',
    'Jeg hedder James'
), (
    unhex(md5(uuid())),
    @subscription_id_twogether_xi,
    (select id from contact_info where email = @contact_info_xi_twogether limit 1),
    @password,
    'xi',
    'Xi',
    'Xang',
    'Jeg hedder Xi'
), (
    unhex(md5(uuid())),
    @subscription_id_twogether_jimmy,
    (select id from contact_info where email = @contact_info_jimmy_twogether limit 1),
    @password,
    'jimmy',
    'Jimmy',
    'Jensen',
    'Jeg hedder Jimmy'
), (
    unhex(md5(uuid())),
    @subscription_id_twogether_hanne,
    (select id from contact_info where email = @contact_info_hanne_twogether limit 1),
    @password,
    'hanne',
    'Hanne',
    'Hansen',
    'Jeg hedder Hanne'
), (
    unhex(md5(uuid())),
    @subscription_id_jamsters_tue,
    (select id from contact_info where email = @contact_info_tue_jamsters limit 1),
    @password,
    'tue',
    'Tue',
    'Tirsdag',
    'Jeg hedder Tue'
);

insert into organisation_employments(organisation_id, organisation_employee_id, role) values (
    (select id from organisations where contact_info_id = @contact_info_id_ivaerksted),
    (select id from organisation_employees where subscription_id = @subscription_id_ivaerksted_jens),
    'LEADER'
), (
    (select id from organisations where contact_info_id = @contact_info_id_ivaerksted),
    (select id from organisation_employees where subscription_id = @subscription_id_ivaerksted_birthe),
    'PR'
), (
    (select id from organisations where contact_info_id = @contact_info_id_arena),
    (select id from organisation_employees where subscription_id = @subscription_id_arena_hans),
    'LEADER'
), (
    (select id from organisations where contact_info_id = @contact_info_id_arena),
    (select id from organisation_employees where subscription_id = @subscription_id_arena_susanne),
    'PR'
), (
    (select id from organisations where contact_info_id = @contact_info_id_arena),
    (select id from organisation_employees where subscription_id = @subscription_id_arena_joanna),
    'BOOKER'
), (
    (select id from organisations where contact_info_id = @contact_info_id_arena),
    (select id from organisation_employees where subscription_id = @subscription_id_arena_john),
    'BOOKER'
), (
    (select id from organisations where contact_info_id = @contact_info_id_arena),
    (select id from organisation_employees where subscription_id = @subscription_id_arena_james),
    'BOOKER'
), (
    (select id from organisations where contact_info_id = @contact_info_id_twogether),
    (select id from organisation_employees where subscription_id = @subscription_id_twogether_xi),
    'BOOKER'
), (
    (select id from organisations where contact_info_id = @contact_info_id_twogether),
    (select id from organisation_employees where subscription_id = @subscription_id_twogether_jimmy),
    'PR'
), (
    (select id from organisations where contact_info_id = @contact_info_id_twogether),
    (select id from organisation_employees where subscription_id = @subscription_id_twogether_hanne),
    'LEADER'
), (
    (select id from organisations where contact_info_id = @contact_info_id_jamsters),
    (select id from organisation_employees where subscription_id = @subscription_id_jamsters_tue),
    'LEADER'
);