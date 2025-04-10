-- --
-- First initialization of this project
-- Participators are not included yet

-- Constraint syntax is:
-- PK = Primary key
-- FK = Foreign key

-- {Key}_{Primary}__{Foreign}
-- --

create table countries(
    id binary(16) not null,
    title varchar(32) not null,
    code int(3) not null,

    constraint PK_countries
        primary key (id)
);

create table addresses(
    id binary(16) not null,
    street varchar(64) not null,
    floor varchar(16),
    municipality varchar(32),
    zip varchar(8) not null,
    city varchar(64) not null,

    constraint PK_addresses
        primary key (id)
);

create table contact_info(
    id binary(16) not null,
    address_id binary(16),
    country_id binary(16),
    email varchar(64) not null,

    constraint PK_contact_info
        primary key (id),
    constraint FK_contact_info__countries
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

    constraint PK_phones
        primary key (id),
    constraint FK_phones__countries
        foreign key (country_id)
            references countries (id)
                on update cascade
                on delete set null,
    constraint FK_phones__contact_info
        foreign key (contact_info_id)
            references contact_info (id)
                on update cascade
                on delete set null
);

create table media(
    id binary(16) not null,
    endpoint varchar(64) not null unique,
    kind enum('IMAGE', 'MUSIC'),

    constraint PK_media
        primary key (id)
);

create table albums(
    id binary(16) not null,
    title varchar(128) not null,
    timestamp datetime default now(),

    constraint PK_albums
        primary key (id)
);

create table album_media(
    album_id binary(16) not null,
    media_id binary(16) not null,

    constraint PK_album_media
        primary key (album_id, media_id),
    constraint FK_media__albums
        foreign key (album_id)
            references albums (id)
                on update cascade
                on delete cascade,
    constraint FK_album__media
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

    constraint PK_subscriptions
        primary key (id)
);

create table authorities(
    id binary(16) not null,
    level enum(
        'STANDARD',
        'ADMIN'
    ),

    constraint PK_authorities
        primary key (id)
);

create table chat_rooms(
    id binary(16) not null,
    title varchar(124),
    timestamp datetime not null default now(),

    constraint PK_chat_rooms
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

    constraint PK_messages
        primary key (id),
    constraint FK_messages__chat_rooms
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

    constraint PK_posts
        primary key (id)
);

create table bands(
    id binary(16) not null,
    subscription_id binary(16) not null,
    name varchar(64) not null,
    description varchar(1024),
    runner varchar(2048),
    timestamp datetime not null default now(),

    constraint PK_bands
        primary key (id),
    constraint FK_bands__subscriptions
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

    constraint PK_artists
        primary key (id),
    constraint FK_artists__subscriptions
        foreign key (subscription_id)
            references subscriptions (id)
                on update cascade
                on delete cascade,
    constraint FK_artists__contact_info
        foreign key (contact_info_id)
            references contact_info (id)
                on update cascade
                on delete cascade
);

create table organisations(
    id binary(16) not null,
    contact_info_id binary(16) not null,
    title varchar(64) not null,
    timestamp datetime not null default now(),

    constraint PK_organisations
        primary key (id),
    constraint FK_organisations__contact_info
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

    constraint PK_chat_room_templates
        primary key (id),
    constraint FK_chat_room_templates__organisations
        foreign key (organisation_id)
            references organisations (id)
                on update cascade
                on delete cascade
);

create table chat_room_templates_chatters(
    chat_room_template_id binary(16) not null,
    organisation_employee_id binary(16) not null,

    constraint PK_chat_room_templates_chatters
        primary key (chat_room_template_id, organisation_employee_id),
    constraint FK_chat_room_template_chatters__chat_room_templates
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

    constraint PK_venues
        primary key (id),
    constraint FK_venues__addresses
        foreign key (address_id)
            references addresses (id)
                on update cascade
                on delete cascade
);

create table venue_areas(
    id binary(16) not null,
    venue_id binary(16) not null,
    title varchar(32) not null,

    constraint PK_venues_areas
        primary key (id),
    constraint FK_venue_areas__venues
        foreign key (venue_id)
            references venues (id)
                on update cascade
                on delete cascade
);

create table organisation_venues(
    organisation_id binary(16) not null,
    venue_id binary(16) not null,

    constraint PK_organisation_venues
        primary key (organisation_id, venue_id),
    constraint FK_venues__organisations
        foreign key (organisation_id)
            references organisations (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation__venues
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

    constraint PK_organisation_employees
        primary key (id),
    constraint FK_organisation_employees__subscriptions
        foreign key (subscription_id)
            references subscriptions (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation_employees__contact_info
        foreign key (contact_info_id)
            references contact_info (id)
                on update cascade
                on delete cascade
);

create table organisation_employments(
    organisation_id binary(16) not null,
    organisation_employee_id binary(16) not null,
    role enum(
        'BOOKER',
        'PR',
        'LEADER'
    ) not null,

    constraint PK_organisation_employments
        primary key (organisation_id, organisation_employee_id),
    constraint FK_organisation_employments__organisations
        foreign key (organisation_id)
            references organisations (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation_employments__organisation_employee
        foreign key (organisation_employee_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade
);

create table band_posts__artists(
    id binary(16) not null,
    receiver_id binary(16) not null,
    poster_id binary(16) not null,

    constraint PK_band_posts__artists
        primary key (id, receiver_id, poster_id),
    constraint FK_band_posts__artists__receiver
        foreign key (receiver_id)
            references bands (id)
                on update cascade
                on delete cascade,
    constraint FK_band_posts__artists__poster
        foreign key (poster_id)
            references artists (id)
                on update cascade
                on delete cascade
);

create table band_posts__organisation_employees(
    id binary(16) not null,
    receiver_id binary(16) not null,
    poster_id binary(16) not null,

    constraint PK_band_posts__organisation_employees
        primary key (id, receiver_id, poster_id),
    constraint FK_band_posts__organisation_employees__receiver
        foreign key (receiver_id)
            references bands (id)
                on update cascade
                on delete cascade,
    constraint FK_band_posts__organisation_employees__poster
        foreign key (poster_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade
);

create table venue_posts__organisation_employees(
    id binary(16) not null,
    receiver_id binary(16) not null,
    poster_id binary(16) not null,

    constraint PK_venue_posts__organisation_employees
        primary key (id, receiver_id, poster_id),
    constraint FK_venue_posts__organisation_employees__receiver
        foreign key (receiver_id)
            references venues (id)
                on update cascade
                on delete cascade,
    constraint FK_venue_posts__organisation_employees__poster
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

   constraint PK_events
       primary key (id),
   constraint FK_events__venues
       foreign key (venue_id)
           references venues (id)
               on update cascade
               on delete cascade,
   constraint FK_events__contact_info
       foreign key (contact_info_id)
           references contact_info (id)
               on update cascade
               on delete cascade,
   constraint FK_events__chat_rooms
       foreign key (contact_info_id)
           references chat_rooms (id)
               on update cascade
               on delete cascade,
   constraint FK_events__addresses
       foreign key (address_id)
           references addresses (id)
               on update cascade
               on delete cascade
);

create table event_posts__organisation_employees(
    id binary(16) not null,
    receiver_id binary(16) not null,
    poster_id binary(16) not null,

    constraint PK_event_posts__organisation_employees
        primary key (id, receiver_id, poster_id),
    constraint FK_event_posts__organisation_employees__receiver
        foreign key (receiver_id)
            references events (id)
                on update cascade
                on delete cascade,
    constraint FK_event_posts__organisation_employees__poster
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

    constraint PK_artist_history
        primary key (id),
    constraint FK_history__artists
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

    constraint PK_event_history
        primary key (id),
    constraint FK_history__events
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

    constraint PK_organisation_employee_history
        primary key (id),
    constraint FK_history__organisation_employees
        foreign key (organisation_employee_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade
);

create table artist_history_details(
    id binary(16) not null,
    artist_history_id binary(16) not null,
    content varchar(128) not null,

    constraint PK_artist_history_details
        primary key (id),
    constraint FK_details__artist_history
        foreign key (artist_history_id)
            references artist_history (id)
                on update cascade
                on delete cascade
);

create table event_history_details(
    id binary(16) not null,
    event_history_id binary(16) not null,
    content varchar(128) not null,

    constraint PK_event_history_details
        primary key (id),
    constraint FK_details__event_history
        foreign key (event_history_id)
            references event_history (id)
                on update cascade
                on delete cascade
);

create table organisation_employee_history_details(
    id binary(16) not null,
    organisation_employee_history_id binary(16) not null,
    content varchar(128) not null,

    constraint PK_organisation_employee_history_details
        primary key (id),
    constraint FK_details__organisation_employee_history
        foreign key (organisation_employee_history_id)
            references organisation_employee_history (id)
                on update cascade
                on delete cascade
);

create table artists_following_artists(
    follower_id binary(16) not null,
    followed_id binary(16) not null,
    notify boolean not null,

    constraint PK_artists_following_artists
        primary key (follower_id, followed_id),
    constraint FK_artists_following_artists__follower
        foreign key (follower_id)
            references artists (id)
                on update cascade
                on delete cascade,
    constraint FK_artists_following_artists__followed
        foreign key (followed_id)
            references artists (id)
                on update cascade
                on delete cascade
);

create table artists_requests_artists(
    sender_id binary(16) not null,
    receiver_id binary(16) not null,
    event_id binary(16) not null,
    is_approved boolean not null default false,

    constraint PK_artists_requests_artists
        primary key (receiver_id, sender_id, event_id),
    constraint FK_artists_requests_artists__sender
        foreign key (sender_id)
            references artists (id)
            on update cascade
            on delete cascade,
    constraint FK_artists_requests_artists__receiver
        foreign key (receiver_id)
            references artists (id)
                on update cascade
                on delete cascade,
    constraint FK_artists_requests_artists__events
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade
);

create table artists_requests_organisation_employees(
    sender_id binary(16) not null,
    receiver_id binary(16) not null,
    event_id binary(16) not null,
    is_approved boolean not null default false,

    constraint PK_artists_requests_organisation_employees
        primary key (receiver_id, sender_id, event_id),
    constraint FK_artists_requests_organisation_employees__sender
        foreign key (sender_id)
            references artists (id)
            on update cascade
            on delete cascade,
    constraint FK_artists_requests_organisation_employees__receiver
        foreign key (receiver_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade,
    constraint FK_artists_requests_organisation_employees__event
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade
);

create table organisation_employees_requests_artists(
    sender_id binary(16) not null,
    receiver_id binary(16) not null,
    event_id binary(16) not null,
    is_approved boolean not null default false,

    constraint PK_organisation_employees_requests_artists
        primary key (receiver_id, sender_id, event_id),
    constraint FK_organisation_employees_requests_artists__sender
        foreign key (sender_id)
            references organisation_employees (id)
            on update cascade
            on delete cascade,
    constraint FK_organisation_employees_requests_artists__receiver
        foreign key (receiver_id)
            references artists (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation_employees_requests_artists_events
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade
);

create table organisation_employees_requests_organisation_employees(
    sender_id binary(16) not null,
    receiver_id binary(16) not null,
    event_id binary(16) not null,
    is_approved boolean not null default false,

    constraint PK_organisation_employees_requests_organisation_employees
        primary key (receiver_id, sender_id, event_id),
    constraint FK_organisation_employees_requests_organisation_employees__send
        foreign key (sender_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation_employees_requests_organisation_employees__rec
        foreign key (receiver_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation_employees_requests_organisation_employees__event
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade
);

create table organisation_ratings__artists(
    id binary(16) not null,
    receiver_id binary(16) not null,
    rater_id binary(16),
    band_id binary(16),
    value int(1) not null,
    comment varchar(512),
    timestamp datetime not null default now(),

    constraint PK_organisation_ratings__artists
        primary key (id),
    constraint FK_organisation_ratings__artists__receiver
        foreign key (receiver_id)
            references organisations (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation_ratings__artists__rater
        foreign key (rater_id)
            references artists (id)
                on update cascade
                on delete set null,
    constraint FK_organisation_ratings__artists__band
        foreign key (band_id)
            references bands (id)
                on update cascade
                on delete set null
);

create table organisation_ratings__organisations(
    id binary(16) not null,
    receiver_id binary(16) not null,
    rater_id binary(16),
    organisation_id binary(16),
    value int(1) not null,
    comment varchar(512),
    timestamp datetime not null default now(),

    constraint PK_organisation_ratings__organisations
        primary key (id),
    constraint FK_organisation_ratings__organisations__receiver
        foreign key (receiver_id)
            references organisations (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation_ratings__organisations__rater
        foreign key (rater_id)
            references organisation_employees (id)
                on update cascade
                on delete set null,
    constraint FK_organisation_ratings__organisations__organisations
        foreign key (organisation_id)
            references organisations (id)
                on update cascade
                on delete set null
);

create table event_ratings__artists(
    id binary(16) not null,
    receiver_id binary(16) not null,
    rater_id binary(16),
    band_id binary(16),
    value int(1) not null,
    comment varchar(512),
    timestamp datetime not null default now(),

    constraint PK_event_ratings__artists
        primary key (id),
    constraint FK_event_ratings__artists__receiver
        foreign key (receiver_id)
            references events (id)
                on update cascade
                on delete cascade,
    constraint FK_event_ratings__artists__rater
        foreign key (rater_id)
            references artists (id)
                on update cascade
                on delete set null,
    constraint FK_event_ratings__artists__band
        foreign key (band_id)
            references bands (id)
                on update cascade
                on delete set null
);

create table band_ratings__artists(
    id binary(16) not null,
    receiver_id binary(16) not null,
    rater_id binary(16),
    band_id binary(16),
    value int(1) not null,
    comment varchar(512),
    timestamp datetime not null default now(),

    constraint PK_band_ratings__artists
        primary key (id),
    constraint FK_band_ratings__artists__receiver
        foreign key (receiver_id)
            references bands (id)
            on update cascade
            on delete cascade,
    constraint FK_band_ratings__artists__rater
        foreign key (rater_id)
            references artists (id)
            on update cascade
            on delete set null,
    constraint FK_band_ratings__artists__band
        foreign key (band_id)
            references bands (id)
            on update cascade
            on delete set null
);

create table band_ratings__organisation_employees(
    id binary(16) not null,
    receiver_id binary(16) not null,
    rater_id binary(16),
    organisation_id binary(16),
    value int(1) not null,
    comment varchar(512),
    timestamp datetime not null default now(),

    constraint PK_band_ratings__organisation_employees
        primary key (id),
    constraint FK_band_ratings__organisation_employees__receiver
        foreign key (receiver_id)
            references bands (id)
                on update cascade
                on delete cascade,
    constraint FK_band_ratings__organisation_employees__rater
        foreign key (rater_id)
            references organisation_employees (id)
                on update cascade
                on delete set null,
    constraint FK_band_ratings__organisation_employees__organisation
        foreign key (organisation_id)
            references organisations (id)
                on update cascade
                on delete set null
);

create table venue_ratings__artists(
    id binary(16) not null,
    receiver_id binary(16) not null,
    rater_id binary(16),
    band_id binary(16),
    value int(1) not null,
    comment varchar(512),
    timestamp datetime not null default now(),

    constraint PK_venue_ratings__artists
        primary key (id),
    constraint FK_venue_ratings__artists__receiver
        foreign key (receiver_id)
            references venues (id)
                on update cascade
                on delete cascade,
    constraint FK_venue_ratings__artists__rater
        foreign key (rater_id)
            references artists (id)
                on update cascade
                on delete set null,
    constraint FK_venue_ratings__artists__band
        foreign key (band_id)
            references bands (id)
            on update cascade
            on delete set null
);

create table band_memberships(
    band_id binary(16) not null,
    artist_id binary(16) not null,
    association enum(
        'SESSION',
        'OWNER'
    ),

    constraint PK_band_memberships
        primary key (band_id, artist_id),
    constraint FK_band_memberships__bands
        foreign key (band_id)
            references bands (id)
                on update cascade
                on delete cascade,
    constraint FK_band_memberships__artists
        foreign key (artist_id)
            references artists (id)
                on update cascade
                on delete cascade
);

create table ticket_option_templates(
    id binary(16) not null,
    title varchar(32) not null,
    price decimal(19, 4),
    valuta varchar(8),
    is_sitting boolean not null default false,
    timestamp datetime not null default now(),

    constraint PK_ticket_option_templates
        primary key (id)
);

create table ticket_option_templates_venue_areas(
    ticket_option_template_id binary(16) not null,
    venue_area_id binary(16) not null,

    constraint PK_ticket_option_templates_venue_areas
        primary key (ticket_option_template_id, venue_area_id),
    constraint FK_venue_areas__ticket_option_templates
        foreign key (ticket_option_template_id)
            references ticket_option_templates (id)
                on update cascade
                on delete cascade,
    constraint FK_ticket_option_templates__venue_areas
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

    constraint PK_ticket_options
        primary key (id),
    constraint FK_ticket_options__events
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade,
    constraint FK_ticket_options__venues
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

    constraint PK_tickets
        primary key (id),
    constraint FK_tickets__events
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete set null,
    constraint FK_tickets__ticket_options
        foreign key (ticket_option_id)
            references ticket_options (id)
                on update cascade
                on delete set null
);

create table artist_chat_rooms(
    id binary(16) not null,
    chat_room_id binary(16) not null,
    artist_id binary(16) not null,

    constraint PK_artist_chat_rooms
        primary key (id),
    constraint FK_artists__chat_rooms
        foreign key (chat_room_id)
            references chat_rooms (id)
                on update cascade
                on delete cascade,
    constraint FK_chat_rooms__artists
        foreign key (artist_id)
            references artists (id)
                on update cascade
                on delete cascade
);

create table organisation_employee_chat_rooms(
    id binary(16) not null,
    chat_room_id binary(16) not null,
    organisation_employee_id binary(16) not null,

    constraint PK_organisation_employee_chat_rooms
        primary key (id),
    constraint FK_organisation_employee__chat_rooms
        foreign key (chat_room_id)
            references chat_rooms (id)
                on update cascade
                on delete cascade,
    constraint FK_chat_rooms__organisation_employees
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

    constraint PK_gigs
        primary key (id),
    constraint FK_gigs__events
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade
);

create table organisation_events(
    organisation_id binary(16) not null,
    event_id binary(16) not null,

    constraint PK_organisation_events
        primary key (organisation_id, event_id),
    constraint FK_events__organisations
        foreign key (organisation_id)
            references organisations (id)
                on update cascade
                on delete cascade,
    constraint FK_organisations__events
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade
);

create table acts(
    gig_id binary(16) not null,
    band_id binary(16) not null,

    constraint PK_acts
        primary key (gig_id, band_id),
    constraint FK_acts__gigs
        foreign key (gig_id)
            references gigs (id)
                on update cascade
                on delete cascade,
    constraint FK_acts__bands
        foreign key (band_id)
            references bands (id)
                on update cascade
                on delete cascade
);

create table artist_event_participations(
    artist_id binary(16) not null,
    event_id binary(16) not null,
    implementation enum(
        'ACCEPTED',
        'INTERESTED',
        'CANCELED',
        'INVITED'
    ),
    timestamp datetime not null default now(),

    constraint PK_artist_event_participations
        primary key (artist_id, event_id),
    constraint FK_event_participations__artists
        foreign key (artist_id)
            references artists(id)
                on update cascade
                on delete cascade,
    constraint FK_artists__event_participations
        foreign key (event_id)
            references events(id)
                on update cascade
                on delete cascade
);

create table organisation_employee_event_participations(
    organisation_employee_id binary(16) not null,
    event_id binary(16) not null,
    implementation enum(
        'ACCEPTED',
        'INTERESTED',
        'CANCELED',
        'INVITED'
    ),
    timestamp datetime not null default now(),

    constraint PK_organisation_employee_event_participations
        primary key (organisation_employee_id, event_id),
    constraint FK_event_participations__organisation_employees
        foreign key (organisation_employee_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation_employees__event_participations
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade
);

create table artist_authorities(
    artist_id binary(16) not null,
    authority_id binary(16) not null,

    constraint PK_artist_authorities
        primary key (artist_id, authority_id),
    constraint FK_authorities_artists
        foreign key (artist_id)
            references artists (id)
                on update cascade
                on delete cascade,
    constraint FK_artist_authorities
        foreign key (authority_id)
            references authorities (id)
                on update cascade
                on delete cascade
);

create table organisation_employee_authorities(
    organisation_employee_id binary(16) not null,
    authority_id binary(16) not null,

    constraint PK_organisation_employee_authorities
        primary key (organisation_employee_id, authority_id),
    constraint FK_authorities_organisation_employees
        foreign key (organisation_employee_id)
            references organisation_employees (id)
                on update cascade
                on delete cascade,
    constraint FK_organisation_employee_authorities
        foreign key (authority_id)
            references authorities (id)
                on update cascade
                on delete cascade
);

create table artist_albums(
    artist_id binary(16) not null,
    album_id binary(16) not null,

    constraint PK_artist_albums
        primary key (artist_id, album_id),
    constraint FK_albums__artists
        foreign key (artist_id)
            references artists (id)
                on update cascade
                on delete cascade,
    constraint FK_artists__albums
        foreign key (album_id)
            references albums (id)
                on update cascade
                on delete cascade
);

create table band_albums(
    band_id binary(16) not null,
    album_id binary(16) not null,

    constraint PK_band_albums
        primary key (band_id, album_id),
    constraint FK_albums__bands
        foreign key (band_id)
            references bands (id)
                on update cascade
                on delete cascade,
    constraint FK_bands__albums
        foreign key (album_id)
            references albums (id)
                on update cascade
                on delete cascade
);

create table organisation_albums(
    organisation_id binary(16) not null,
    album_id binary(16) not null,

    constraint PK_organisation_albums
        primary key (organisation_id, album_id),
    constraint FK_albums__organisations
        foreign key (organisation_id)
            references organisations (id)
                on update cascade
                on delete cascade,
    constraint FK_organisations__albums
        foreign key (album_id)
            references albums (id)
                on update cascade
                on delete cascade
);

create table venue_albums(
    venue_id binary(16) not null,
    album_id binary(16) not null,

    constraint PK_venue_albums
        primary key (venue_id, album_id),
    constraint FK_albums__venues
        foreign key (venue_id)
            references venues (id)
                on update cascade
                on delete cascade,
    constraint FK_venues__albums
        foreign key (album_id)
            references albums (id)
                on update cascade
                on delete cascade
);

create table event_albums(
    event_id binary(16) not null,
    album_id binary(16) not null,

    constraint PK_event_albums
        primary key (event_id, album_id),
    constraint FK_albums__events
        foreign key (event_id)
            references events (id)
                on update cascade
                on delete cascade,
    constraint FK_events__albums
        foreign key (album_id)
            references albums (id)
                on update cascade
                on delete cascade
);

