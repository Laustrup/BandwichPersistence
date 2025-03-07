create table countries(
    id binary(16) not null,
    title varchar(32) not null,
    code int(3) not null,

    primary key (id)
);

create table addresses(
    id binary(16) not null,
    street varchar(64) not null,
    floor varchar(16),
    municipality varchar(32),
    zip varchar(8) not null,
    city varchar(64) not null,

    primary key (id)
);

create table contact_info(
    id binary(16) not null,
    address_id binary(16),
    country_id binary(16),
    email varchar(64) not null,

    primary key (id),
    constraint FK_contact_info_country
        foreign key (country_id)
            references countries(id)
                on update cascade
                on delete set null
);

create table phones(
    id binary(16) not null,
    country_id binary(16),
    contact_info_id binary(16),
    numbers bigint(18) not null,
    is_mobile boolean not null,
    is_business boolean not null,

    primary key (id),
    constraint FK_phone_country
        foreign key (country_id)
            references countries (id)
                on update cascade
                on delete set null,
    constraint FK_phone_contact_info
        foreign key (contact_info_id)
            references contact_info (id)
                on update cascade
                on delete set null
);

create table media(
    id binary(16) not null,
    endpoint varchar(64) not null unique,
    kind enum('IMAGE', 'MUSIC'),

    primary key (id)
);

create table albums(
    id binary(16) not null,
    title varchar(128) not null,
    timestamp datetime default now(),

    primary key (id)
);

create table album_media(
    album_id binary(16) not null,
    media_id binary(16) not null,

    primary key (album_id, media_id),
    constraint FK_album
        foreign key (album_id)
            references albums (id)
                on update cascade
                on delete cascade,
    constraint FK_media
        foreign key (media_id)
            references media (id)
                on update cascade
                on delete cascade
);

create table subscriptions(
    id binary(16) not null,
    status enum(
        'ACCEPTED',
        'BLOCKED',
        'DEACTIVATED',
        'SUSPENDED',
        'PENDING',
        'CLOSED'
    ) not null ,
    kind enum(
        'PAYING',
        'FREE'
     ) not null,

    primary key (id)
);

create table authorities(
    id binary(16) not null,
    level enum(
        'STANDARD',
        'ADMIN'
    ),

    primary key (id)
);

create table chat_rooms(
    id binary(16) not null,
    title varchar(124),
    timestamp datetime not null default now(),

    primary key (id)
);

create table messages(
    id binary(16) not null,
    chat_room_id binary(16) not null,
    content text not null,
    is_sent datetime,
    is_read datetime,
    is_edited boolean not null,
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_chat_room
        foreign key (chat_room_id)
            references chat_rooms (id)
                on update cascade
                on delete cascade
);

create table posts(
    id binary(16) not null,
    content varchar(2048) not null,
    is_sent datetime,
    is_edited boolean not null default false,
    is_read datetime,
    timestamp datetime not null default now(),

    primary key (id)
);

create table bands(
    id binary(16) not null,
    subscription_id binary(16) not null,
    name varchar(64) not null,
    description varchar(1024),
    runner varchar(2048),
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_subscription
        foreign key (subscription_id)
            references subscriptions (id)
                on update cascade
                on delete cascade
);

create table artists(
    id binary(16) not null,
    subscription_id binary(16) not null,
    contact_info_id binary(16) not null,
    username varchar(64) not null,
    first_name varchar(32) not null,
    last_name varchar(32) not null,
    description varchar(1024),
    runner varchar(2048),
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_artist_subscription
        foreign key (subscription_id)
            references subscriptions (id)
                on update cascade
                on delete cascade,
    constraint FK_artist_contact_info
        foreign key (contact_info_id)
            references contact_info (id)
                on update cascade
                on delete cascade
);

create table organisation(
    id binary(16) not null,
    contact_info_id binary(16) not null,
    title varchar(64) not null,
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_contact_info
        foreign key (contact_info_id)
            references contact_info (id)
                on update cascade
                on delete cascade
);

create table chat_room_templates(
    id binary(16) not null,
    organisation_id binary(16) not null,
    title varchar(124),
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_organisation
        foreign key (organisation_id)
            references organisation (id)
                on update cascade
                on delete cascade
);

create table chat_room_templates_chatters(
    chat_room_template_id binary(16) not null,
    organisation_employee_id binary(16) not null,

    primary key (chat_room_template_id, organisation_employee_id),
    constraint FK_chat_room_template
        foreign key (chat_room_template_id)
            references chat_room_templates (id)
                on update cascade
                on delete cascade
);

create table venues(
    id binary(16) not null,
    address_id binary(16) not null,
    description varchar(2048),
    stage_setup varchar(2048),
    size int(8),

    primary key (id),
    constraint FK_address
        foreign key (address_id)
            references addresses (id)
                on update cascade
                on delete cascade
);

create table venue_areas(
    id binary(16) not null,
    venue_id binary(16) not null,
    title varchar(32) not null,

    primary key (id),
    constraint FK_venue
        foreign key (venue_id)
            references venues (id)
                on update cascade
                on delete cascade
);

create table organisation_venues(
    organisation_id binary(16) not null,
    venue_id binary(16) not null,

    primary key (organisation_id, venue_id),
    constraint FK_organisation_venue
        foreign key (organisation_id)
            references organisation (id)
                on update cascade
                on delete cascade,
    constraint FK_venue_organisation
        foreign key (venue_id)
            references venues (id)
                on update cascade
                on delete cascade
);

create table organisation_employees(
    id binary(16) not null,
    subscription_id binary(16) not null,
    contact_info_id binary(16) not null,
    username varchar(64) not null,
    first_name varchar(32) not null,
    last_name varchar(32) not null,
    description varchar(1024),
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_organisation_employees_subscription
        foreign key (subscription_id)
            references subscriptions (id)
            on update cascade
            on delete cascade,
    constraint FK_organisation_employees_contact_info
        foreign key (contact_info_id)
            references contact_info (id)
            on update cascade
            on delete cascade
);

create table organisation_organisation_employees(
    organisation_id binary(16) not null,
    organisation_employee_id binary(16) not null,

    primary key (organisation_id, organisation_employee_id),
    constraint FK_organisation_organisation_employees_organisation
        foreign key (organisation_id)
            references organisation (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation_organisation_employees_organisation_employee
        foreign key (organisation_employee_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade
);

create table organisation_employee_roles(
    id binary(16) not null,
    organisation_employee_id binary(16) not null,
    title enum(
        'BOOKER',
        'PR',
        'LEADER'
    ) not null,

    primary key (id),
    constraint FK_organisation_employee
        foreign key (organisation_employee_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade
);

create table band_posts_artist(
    id binary(16) not null,
    receiver_id binary(16) not null,
    poster_id binary(16) not null,

    primary key (id, receiver_id, poster_id),
    constraint FK_receiver
        foreign key (receiver_id)
            references bands (id)
                on update cascade
                on delete cascade,
    constraint FK_poster
        foreign key (poster_id)
            references artists (id)
                on update cascade
                on delete cascade
);

create table band_posts_organisation_employee(
    id binary(16) not null,
    receiver_id binary(16) not null,
    poster_id binary(16) not null,

    primary key (id, receiver_id, poster_id),
    constraint FK_band_posts_organisation_employee_receiver
        foreign key (receiver_id)
            references bands (id)
                on update cascade
                on delete cascade,
    constraint FK_band_posts_organisation_employee_poster
        foreign key (poster_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade
);

create table venue_posts_organisation_employee(
    id binary(16) not null,
    receiver_id binary(16) not null,
    poster_id binary(16) not null,

    primary key (id, receiver_id, poster_id),
    constraint FK_venue_posts_organisation_employee_receiver
        foreign key (receiver_id)
            references venues (id)
                on update cascade
                on delete cascade,
    constraint FK_venue_posts_organisation_employee_poster
        foreign key (poster_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade
);

create table events(
   id binary(16) not null,
   venue_id binary(16) not null,
   contact_info_id binary(16) not null,
   chat_room_id binary(16) not null,
   address_id binary(16) not null,
   open_doors datetime,
   start datetime,
   end datetime,
   description varchar(2048),
   is_public datetime,
   is_cancelled datetime,
   sold_out datetime,
   zone_id varchar(64) not null default 'UTC',

   primary key (id),
   constraint FK_event_venue
       foreign key (venue_id)
           references venues (id)
           on update cascade
           on delete cascade,
   constraint FK_event_contact_info_id
       foreign key (contact_info_id)
           references contact_info (id)
           on update cascade
           on delete cascade,
   constraint FK_event_chat_room
       foreign key (contact_info_id)
           references chat_rooms (id)
           on update cascade
           on delete cascade,
   constraint FK_event_address_id
       foreign key (address_id)
           references addresses (id)
           on update cascade
           on delete cascade
);

create table event_posts_organisation_employee(
    id binary(16) not null,
    receiver_id binary(16) not null,
    poster_id binary(16) not null,

    primary key (id, receiver_id, poster_id),
    constraint FK_event_posts_organisation_employee_receiver
        foreign key (receiver_id)
            references events (id)
                on update cascade
                on delete cascade,
    constraint FK_event_posts_organisation_employee_poster
        foreign key (poster_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade
);

create table artist_history(
    id binary(16) not null,
    artist_id binary(16) not null,
    title varchar(32) not null,
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_artist_history_artist
        foreign key (artist_id)
            references artists (id)
                on update cascade
                on delete cascade
);

create table event_history(
    id binary(16) not null,
    event_id binary(16) not null,
    title varchar(32) not null,
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_event_history_event
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade
);

create table organisation_employee_history(
    id binary(16) not null,
    organisation_employee_id binary(16) not null,
    title varchar(32) not null,
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_organisation_employee_history_organisation_employee
        foreign key (organisation_employee_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade
);

create table artist_history_details(
    id binary(16) not null,
    artist_history_id binary(16) not null,
    content varchar(128) not null,

    primary key (id),
    constraint FK_artist_history_details_artist_history
        foreign key (artist_history_id)
            references artist_history (id)
                on update cascade
                on delete cascade
);

create table event_history_details(
    id binary(16) not null,
    event_history_id binary(16) not null,
    content varchar(128) not null,

    primary key (id),
    constraint FK_event_history_details_event_history
        foreign key (event_history_id)
            references event_history (id)
                on update cascade
                on delete cascade
);

create table organisation_employee_history_details(
    id binary(16) not null,
    organisation_employee_history_id binary(16) not null,
    content varchar(128) not null,

    primary key (id),
    constraint FK_organisation_employee_history
        foreign key (organisation_employee_history_id)
            references organisation_employee_history (id)
                on update cascade
                on delete cascade
);

create table artist_following_artist(
    follower_id binary(16) not null,
    followed_id binary(16) not null,
    notify boolean not null,

    primary key (follower_id, followed_id),
    constraint FK_follower
        foreign key (follower_id)
            references artists (id)
                on update cascade
                on delete cascade,
    constraint FK_followed
        foreign key (followed_id)
            references artists (id)
                on update cascade
                on delete cascade
);

create table artist_requests_artist(
    sender_id binary(16) not null,
    receiver_id binary(16) not null,
    event_id binary(16) not null,
    is_approved boolean not null default false,

    primary key (receiver_id, sender_id, event_id),
    constraint FK_artist_requests_artist_sender
        foreign key (sender_id)
            references artists (id)
            on update cascade
            on delete cascade,
    constraint FK_artist_requests_artist_receiver
        foreign key (receiver_id)
            references artists (id)
                on update cascade
                on delete cascade,
    constraint FK_artist_requests_artist_event
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade
);

create table artist_requests_organisation_employee(
    sender_id binary(16) not null,
    receiver_id binary(16) not null,
    event_id binary(16) not null,
    is_approved boolean not null default false,

    primary key (receiver_id, sender_id, event_id),
    constraint FK_artist_requests_organisation_employee_sender
        foreign key (sender_id)
            references artists (id)
            on update cascade
            on delete cascade,
    constraint FK_artist_requests_organisation_employee_receiver
        foreign key (receiver_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade,
    constraint FK_artist_requests_organisation_employee_event
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade
);

create table organisation_employee_requests_artist(
    sender_id binary(16) not null,
    receiver_id binary(16) not null,
    event_id binary(16) not null,
    is_approved boolean not null default false,

    primary key (receiver_id, sender_id, event_id),
    constraint FK_organisation_employee_requests_artist_sender
        foreign key (sender_id)
            references organisation_employees (id)
            on update cascade
            on delete cascade,
    constraint FK_organisation_employee_requests_artist_receiver
        foreign key (receiver_id)
            references artists (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation_employee_requests_artist_event
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade
);

create table organisation_employee_requests_organisation_employee(
    sender_id binary(16) not null,
    receiver_id binary(16) not null,
    event_id binary(16) not null,
    is_approved boolean not null default false,

    primary key (receiver_id, sender_id, event_id),
    constraint FK_organisation_employee_requests_organisation_employee_sender
        foreign key (sender_id)
            references organisation_employees (id)
            on update cascade
            on delete cascade,
    constraint FK_organisation_employee_requests_organisation_employee_receiver
        foreign key (receiver_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation_employee_requests_organisation_employee_event
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade
);

create table organisation_ratings_artist(
    id binary(16) not null,
    receiver_id binary(16) not null,
    rater_id binary(16),
    band_id binary(16),
    value int(1) not null,
    comment varchar(512),
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_organisation_ratings_artist_receiver
        foreign key (receiver_id)
            references organisation (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation_ratings_artist_rater
        foreign key (rater_id)
            references artists (id)
                on update cascade
                on delete set null,
    constraint FK_organisation_ratings_artist_band
        foreign key (band_id)
            references bands (id)
                on update cascade
                on delete set null
);

create table organisation_ratings_organisation(
    id binary(16) not null,
    receiver_id binary(16) not null,
    rater_id binary(16),
    organisation_id binary(16),
    value int(1) not null,
    comment varchar(512),
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_organisation_ratings_organisation_receiver
        foreign key (receiver_id)
            references organisation (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation_ratings_organisation_rater
        foreign key (rater_id)
            references organisation_employees (id)
                on update cascade
                on delete set null,
    constraint FK_organisation_ratings_organisation_organisation
        foreign key (organisation_id)
            references organisation (id)
                on update cascade
                on delete set null
);

create table event_ratings_artist(
    id binary(16) not null,
    receiver_id binary(16) not null,
    rater_id binary(16),
    band_id binary(16),
    value int(1) not null,
    comment varchar(512),
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_event_ratings_artist_receiver
        foreign key (receiver_id)
            references events (id)
                on update cascade
                on delete cascade,
    constraint FK_event_ratings_artist_rater
        foreign key (rater_id)
            references artists (id)
                on update cascade
                on delete set null,
    constraint FK_event_ratings_artist_band
        foreign key (band_id)
            references bands (id)
                on update cascade
                on delete set null
);

create table band_ratings_band(
    id binary(16) not null,
    receiver_id binary(16) not null,
    rater_id binary(16),
    band_id binary(16),
    value int(1) not null,
    comment varchar(512),
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_band_ratings_band_receiver
        foreign key (receiver_id)
            references bands (id)
            on update cascade
            on delete cascade,
    constraint FK_band_ratings_band_rater
        foreign key (rater_id)
            references artists (id)
            on update cascade
            on delete set null,
    constraint FK_band_ratings_band_band
        foreign key (band_id)
            references bands (id)
            on update cascade
            on delete set null
);

create table band_ratings_organisation_employee(
    id binary(16) not null,
    receiver_id binary(16) not null,
    rater_id binary(16),
    organisation_id binary(16),
    value int(1) not null,
    comment varchar(512),
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_band_ratings_organisation_employee_receiver
        foreign key (receiver_id)
            references bands (id)
            on update cascade
            on delete cascade,
    constraint FK_band_ratings_organisation_employee_rater
        foreign key (rater_id)
            references organisation_employees (id)
            on update cascade
            on delete set null,
    constraint FK_band_ratings_organisation_employee_organisation
        foreign key (organisation_id)
            references organisation (id)
            on update cascade
            on delete set null
);

create table venue_ratings_artist(
    id binary(16) not null,
    receiver_id binary(16) not null,
    rater_id binary(16),
    band_id binary(16),
    value int(1) not null,
    comment varchar(512),
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_venue_ratings_artist_receiver
        foreign key (receiver_id)
            references venues (id)
                on update cascade
                on delete cascade,
    constraint FK_venue_ratings_artist_rater
        foreign key (rater_id)
            references artists (id)
                on update cascade
                on delete set null,
    constraint FK_venue_ratings_artist_band
        foreign key (band_id)
            references bands (id)
            on update cascade
            on delete set null
);

create table band_membership(
    band_id binary(16) not null,
    artist_id binary(16) not null,
    association enum(
        'SESSION',
        'OWNER'
    )
);

create table ticket_option_template(
    id binary(16) not null,
    title varchar(32) not null,
    price decimal(19, 4),
    valuta varchar(8),
    is_sitting boolean not null default false,
    timestamp datetime not null default now(),

    primary key (id)
);

create table ticket_option_template_venue_areas(
    ticket_option_template_id binary(16) not null,
    venue_area_id binary(16) not null,

    primary key (ticket_option_template_id, venue_area_id),
    constraint FK_ticket_option_template
        foreign key (ticket_option_template_id)
            references ticket_option_template (id)
                on update cascade
                on delete cascade,
    constraint FK_venue_area
        foreign key (venue_area_id)
            references venue_areas (id)
            on update cascade
            on delete cascade
);

create table ticket_options(
    id binary(16) not null,
    event_id binary(16) not null,
    venue_id binary(16) not null,
    title varchar(32) not null,
    price decimal(19, 4),
    valuta varchar(8),
    is_sitting boolean not null default false,
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_ticket_options_event
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade,
    constraint FK_ticket_options_venue
        foreign key (venue_id)
            references venues (id)
            on update cascade
            on delete cascade
);

create table tickets(
    id binary(16) not null,
    event_id binary(16),
    ticket_option_id binary(16),
    seat varchar(16),
    price decimal(19, 4),
    valuta varchar(8),
    is_arrived boolean not null default false,
    is_sitting boolean not null default false,
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_tickets_event
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete set null,
    constraint FK_tickets_ticket_option
        foreign key (ticket_option_id)
            references ticket_options (id)
                on update cascade
                on delete set null
);

create table artist_chat_rooms(
    id binary(16) not null,
    chat_room_id binary(16) not null,
    artist_id binary(16) not null,

    primary key (id),
    constraint FK_artist_chat_rooms_chat_room_1
        foreign key (chat_room_id)
            references chat_rooms (id)
                on update cascade
                on delete cascade,
    constraint FK_artist_chat_rooms_artist
        foreign key (artist_id)
            references artists (id)
                on update cascade
                on delete cascade
);

create table organisation_employee_chat_rooms(
    id binary(16) not null,
    chat_room_id binary(16) not null,
    organisation_employee_id binary(16) not null,

    primary key (id),
    constraint FK_artist_chat_rooms_chat_room_2
        foreign key (chat_room_id)
            references chat_rooms (id)
                on update cascade
                on delete cascade,
    constraint FK_artist_chat_rooms_organisation_employee
        foreign key (organisation_employee_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade
);

create table gigs(
    id binary(16) not null,
    event_id binary(16) not null,
    start datetime,
    end datetime,
    timestamp datetime not null default now(),

    primary key (id),
    constraint FK_gigs_event
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade
);

create table organisation_events(
    organisation_id binary(16) not null,
    event_id binary(16) not null,

    primary key (organisation_id, event_id),
    constraint FK_organisation_events_organisation
        foreign key (organisation_id)
            references organisation (id)
            on update cascade
            on delete cascade
);

create table acts(
    gig_id binary(16) not null,
    band_id binary(16) not null,

    primary key (gig_id, band_id),
    constraint FK_acts_gig
        foreign key (gig_id)
            references gigs (id)
                on update cascade
                on delete cascade,
    constraint FK_acts_band
        foreign key (band_id)
            references bands (id)
                on update cascade
                on delete cascade
);

create table artist_participation(
    artist_id binary(16) not null,
    event_id binary(16) not null,
    type enum(
        'ACCEPTED',
        'INTERESTED',
        'CANCELED',
        'INVITED'
    ),
    timestamp datetime not null default now(),

    primary key (artist_id, event_id),
    constraint FK_artist_participation_artist
        foreign key (artist_id)
            references artists(id)
                on update cascade
                on delete cascade,
    constraint FK_artist_participation_event
        foreign key (event_id)
            references events(id)
                on update cascade
                on delete cascade
);

create table organisation_employee_participation(
    organisation_employee_id binary(16) not null,
    event_id binary(16) not null,
    type enum(
        'ACCEPTED',
        'INTERESTED',
        'CANCELED',
        'INVITED'
    ),
    timestamp datetime not null default now(),

    primary key (organisation_employee_id, event_id),
    constraint FK_organisation_employee_participation_organisation_employee
        foreign key (organisation_employee_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation_employee_participation_event
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade
);

create table artist_authorities(
    artist_id binary(16) not null,
    authority_id binary(16) not null,

    primary key (artist_id, authority_id),
    constraint FK_artist_authorities_artist
        foreign key (artist_id)
            references artists (id)
            on update cascade
            on delete cascade,
    constraint FK_artist_authorities_authority
        foreign key (authority_id)
            references authorities (id)
            on update cascade
            on delete cascade
);

create table organisation_employee_authorities(
    organisation_employee_id binary(16) not null,
    authority_id binary(16) not null,

    primary key (organisation_employee_id, authority_id),
    constraint FK_organisation_employee_authorities_organisation_employee
        foreign key (organisation_employee_id)
            references organisation_employees (id)
            on update cascade
            on delete cascade,
    constraint FK_organisation_employee_authorities_authority
        foreign key (authority_id)
            references authorities (id)
            on update cascade
            on delete cascade
);

create table artist_albums(
    artist_id binary(16) not null,
    album_id binary(16) not null,

    primary key (artist_id, album_id),
    constraint FK_artist_albums_artist
        foreign key (artist_id)
            references artists (id)
                on update cascade
                on delete cascade,
    constraint FK_artist_albums_album
        foreign key (album_id)
            references albums (id)
                on update cascade
                on delete cascade
);

create table band_albums(
    band_id binary(16) not null,
    album_id binary(16) not null,

    primary key (band_id, album_id),
    constraint FK_band_albums_band
        foreign key (band_id)
            references bands (id)
                on update cascade
                on delete cascade,
    constraint FK_band_albums_album
        foreign key (album_id)
            references albums (id)
                on update cascade
                on delete cascade
);

create table organisation_albums(
    organisation_id binary(16) not null,
    album_id binary(16) not null,

    primary key (organisation_id, album_id),
    constraint FK_organisation_albums_band
        foreign key (organisation_id)
            references organisation (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation_albums_album
        foreign key (album_id)
            references albums (id)
                on update cascade
                on delete cascade
);

create table venue_albums(
    venue_id binary(16) not null,
    album_id binary(16) not null,

    primary key (venue_id, album_id),
    constraint FK_venue_albums_venue
        foreign key (venue_id)
            references venues (id)
                on update cascade
                on delete cascade,
    constraint FK_venue_albums_album
        foreign key (album_id)
            references albums (id)
                on update cascade
                on delete cascade
);

create table event_albums(
    event_id binary(16) not null,
    album_id binary(16) not null,

    primary key (event_id, album_id),
    constraint FK_event_albums_event
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade,
    constraint FK_event_albums_album
        foreign key (album_id)
            references albums (id)
                on update cascade
                on delete cascade
);

