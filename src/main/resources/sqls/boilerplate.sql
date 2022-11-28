--------------------------------------------------------
-- Will set the tables with the default values assigned.
-- Deletes former tables.
-- ONLY EXECUTE THIS SCRIPT WHEN DEVELOPING.
--------------------------------------------------------
USE bandwich_db;

--------------------------------------------------------
-- Deleting tables, if they exists.
--------------------------------------------------------

DROP TABLE IF EXISTS contact_informations;
DROP TABLE IF EXISTS subscriptions;
DROP TABLE IF EXISTS album_endpoints;
DROP TABLE IF EXISTS event_albums;
DROP TABLE IF EXISTS user_albums;
DROP TABLE IF EXISTS albums;
DROP TABLE IF EXISTS ratings;
DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS event_bulletins;
DROP TABLE IF EXISTS bulletins;
DROP TABLE IF EXISTS mails;
DROP TABLE IF EXISTS chatters;
DROP TABLE IF EXISTS chat_rooms;
DROP TABLE IF EXISTS followings;
DROP TABLE IF EXISTS participations;
DROP TABLE IF EXISTS acts;
DROP TABLE IF EXISTS gigs;
DROP TABLE IF EXISTS `events`;
DROP TABLE IF EXISTS venues;
DROP TABLE IF EXISTS gear;
DROP TABLE IF EXISTS band_members;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS cards;

--------------------------------------------------------
-- Creating tables.
--------------------------------------------------------
CREATE TABLE cards(
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    `type` ENUM('VISA',
        'AMERICAN_EXPRESS',
        'DANCARD') NOT NULL,
    `owner` VARCHAR(60) NOT NULL,
    numbers BIGINT(16) NOT NULL,
    expiration_month INT(2) NOT NULL,
    expiration_year INT(2) NOT NULL,
    cvv INT(3) NOT NULL,

    PRIMARY KEY(id)
);

CREATE TABLE users(
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL,
    `password` VARCHAR(30) NOT NULL,
    first_name VARCHAR(20),
    last_name VARCHAR(40),
    `description` VARCHAR(500),
    `timestamp` DATETIME NOT NULL,
    kind ENUM('BAND',
        'ARTIST',
        'VENUE',
        'PARTICIPANT') NOT NULL,
    
    PRIMARY KEY(id)
);

CREATE TABLE band_members(
    artist_id BIGINT(20) NOT NULL,
    band_id BIGINT(20) NOT NULL,

    PRIMARY KEY(artist_id, band_id),
    FOREIGN KEY(artist_id) REFERENCES users(id),
    FOREIGN KEY(band_id) REFERENCES users(id)
);

CREATE TABLE gear(
    user_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    `description` VARCHAR(500) NOT NULL,

    PRIMARY KEY(user_id),
    FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE venues(
    user_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    `size` INT(6),
    location VARCHAR(50) NOT NULL,

    PRIMARY KEY(user_id),
    FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE `events`(
    id BIGINT(20) NOT NULL AUTO_INCREMENT,

    open_doors DATETIME,
    `description` VARCHAR(1000),

    is_cancelled BOOL,
    is_voluntary BOOL,
    is_public BOOL,
    is_sold_out BOOL,

    location VARCHAR(50) NOT NULL,
    price DOUBLE,
    tickets_url VARCHAR(100),

    venue_id BIGINT(20) NOT NULL,

    `timestamp` DATETIME NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(venue_id) REFERENCES venues(user_id)
);

CREATE TABLE gigs(
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    event_id BIGINT(20) NOT NULL,
    `start` DATETIME,
    `end` DATETIME,

    PRIMARY KEY(id),
    FOREIGN KEY(event_id) REFERENCES events(id)
);

CREATE TABLE acts(
    user_id BIGINT(20) NOT NULL,
    gig_id BIGINT(20) NOT NULL,

    PRIMARY KEY(user_id, gig_id),
    FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(gig_id) REFERENCES gigs(id)
);

CREATE TABLE participations(
    event_id BIGINT(20) NOT NULL,
    participant_id BIGINT(20) NOT NULL,
    `type` ENUM('ACCEPTED',
        'IN_DOUBT',
        'CANCEL',
        'INVITED'),

    PRIMARY KEY(event_id, participant_id),
    FOREIGN KEY(event_id) REFERENCES events(id),
    FOREIGN KEY(participant_id) REFERENCES users(id)
);

CREATE TABLE followings(
    follower_id BIGINT(20) NOT NULL,
    lead_id BIGINT(20) NOT NULL,

    PRIMARY KEY(follower_id,lead_id),
    FOREIGN KEY(follower_id) REFERENCES users(id),
    FOREIGN KEY(lead_id) REFERENCES users(id)
);

CREATE TABLE chat_rooms(
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    responsible_id BIGINT(20) NOT NULL,

    PRIMARY KEY(id),
    FOREIGN KEY(responsible_id) REFERENCES users(id)
);

CREATE TABLE chatters(
    chat_room_id BIGINT(20) NOT NULL,
    user_id BIGINT(20) NOT NULL,

    PRIMARY KEY(chat_room_id, user_id),
    FOREIGN KEY(chat_room_id) REFERENCES  chat_rooms(id),
    FOREIGN KEY(user_id) REFERENCES  users(id)
);

CREATE TABLE mails(
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    author_id BIGINT(20) NOT NULL,
    content VARCHAR(1000),
    is_sent BOOL NOT NULL,
    is_edited BOOL,
    is_public BOOL NOT NULL,
    chat_room_id BIGINT(20) NOT NULL,

    PRIMARY KEY(id),
    FOREIGN KEY(author_id) REFERENCES users(id),
    FOREIGN KEY(chat_room_id) REFERENCES chat_rooms(id)
);

CREATE TABLE bulletins(
    id BIGINT(20) NOT NULL,
    author_id BIGINT(20) NOT NULL,
    content VARCHAR(1000),
    is_sent BOOL NOT NULL,
    is_edited BOOL,
    is_public BOOL NOT NULL,
    receiver_id BIGINT(20) NOT NULL,

    PRIMARY KEY(id),
    FOREIGN KEY(author_id) REFERENCES users(id),
    FOREIGN KEY(receiver_id) REFERENCES users(id)    
);

CREATE TABLE event_bulletins(
    event_id BIGINT(20) NOT NULL,
    bulletin_id BIGINT(20) NOT NULL,

    PRIMARY KEY(event_id, bulletin_id),
    FOREIGN KEY(event_id) REFERENCES `events`(id),
    FOREIGN KEY(bulletin_id) REFERENCES bulletins(id)
);

CREATE TABLE requests(
    user_id BIGINT(20) NOT NULL,
    event_id BIGINT(20) NOT NULL,
    is_approved BOOL,
    message VARCHAR(250),

    PRIMARY KEY(user_id,event_id),
    FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(event_id) REFERENCES events(id)
);

CREATE TABLE ratings(
    appointed_id BIGINT(20) NOT NULL,
    judge_id BIGINT(20) NOT NULL,
    `value` INT(1) NOT NULL,

    PRIMARY KEY(appointed_id, judge_id),
    FOREIGN KEY(appointed_id) REFERENCES users(id),
    FOREIGN KEY(judge_id) REFERENCES users(id)
);

CREATE TABLE albums(
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    title VARCHAR(100),
    kind ENUM('IMAGE',
        'MUSIC') NOT NULL,
    `timestamp` DATETIME NOT NULL,

    PRIMARY KEY(id)
);

CREATE TABLE user_albums(
    user_id BIGINT(20) NOT NULL,
    album_id BIGINT(20) NOT NULL,

    PRIMARY KEY(user_id, album_id),
    FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(album_id) REFERENCES albums(id)
);

CREATE TABLE event_albums(
    event_id BIGINT(20) NOT NULL,
    album_id BIGINT(20) NOT NULL,

    PRIMARY KEY(event_id, album_id),
    FOREIGN KEY(event_id) REFERENCES events(id),
    FOREIGN KEY(album_id) REFERENCES albums(id)
);

CREATE TABLE album_endpoints(
    album_id BIGINT(20) NOT NULL,
    `value` VARCHAR(100) NOT NULL,

    PRIMARY KEY(album_id, `value`),
    FOREIGN KEY(album_id) REFERENCES albums(id)
);

CREATE TABLE subscriptions(
    user_id BIGINT(20) NOT NULL,
    subscription_status ENUM('ACCEPTED',
        'BLOCKED',
        'DISACTIVATED',
        'CLOSED') NOT NULL,
    subscription_type ENUM('FREEMIUM',
        'PREEMIUM_BAND',
        'PREEMIUM_ARTIST') NOT NULL,
    /* Offer */
    offer_type ENUM('FREE_TRIAL',
        'SALE') NOT NULL,
    offer_expires DATETIME,
    offer_effect DOUBLE,

    card_id BIGINT(8),
    PRIMARY KEY (user_id),
    FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE contact_informations(
    user_id BIGINT(20) NOT NULL,
    email VARCHAR(30),
    /* Phone */
    first_digits INT(3),
    phone_number INT(10),
    phone_is_mobile BOOL,
    street VARCHAR(50),
    floor VARCHAR(10),
    postal VARCHAR(10),
    city VARCHAR(20),
    /* Country */
    country_title VARCHAR(50),
    country_indexes ENUM('DK',
        'SE',
        'DE'),

    PRIMARY KEY(user_id),
    FOREIGN KEY(user_id) REFERENCES users(id)
);
