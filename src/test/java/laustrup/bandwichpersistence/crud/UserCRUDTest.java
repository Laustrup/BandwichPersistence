package laustrup.bandwichpersistence.crud;

import laustrup.bandwichpersistence.JTest;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services.*;
import laustrup.bandwichpersistence.utilities.Liszt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserCRUDTest extends JTest {

    private final String _password = "123456789%&";

    @Test
    void canCRUDArtist() {
        //ARRANGE
        Artist expected = _items.get_artists()[_random.nextInt(_items.get_artistAmount())];
        User.Authority authority = User.Authority.ARTIST;

        //ACT
        begin();
        Artist actual = ArtistPersistenceService.get_instance().create(expected,_password);
        calculatePerformance("creating " + authority);

        //ASSERT
        asserting(expected,actual,authority);

        readUpdateDelete(expected,actual,authority);
    }

    @Test
    void canCRUDVenue() {
        //ARRANGE
        Venue expected = _items.get_venues()[_random.nextInt(_items.get_venueAmount())];
        User.Authority authority = User.Authority.VENUE;

        //ACT
        begin();
        Venue actual = VenuePersistenceService.get_instance().create(expected,_password);
        calculatePerformance("creating " + authority);

        //ASSERT
        asserting(expected,actual,authority);

        readUpdateDelete(expected,actual,authority);
    }

    @Test
    void canCRUDParticipant() {
        //ARRANGE
        Participant expected = _items.get_participants()[_random.nextInt(_items.get_participantAmount())];
        User.Authority authority = User.Authority.PARTICIPANT;

        //ACT
        begin();
        Participant actual = ParticipantPersistenceService.get_instance().create(expected,_password);
        calculatePerformance("creating " + authority);

        //ASSERT
        asserting(expected,actual,authority);

        readUpdateDelete(expected,actual,authority);
    }

    @Test
    void canCRUDBand() {
        //ARRANGE
        Band expected = _items.get_bands()[_random.nextInt(_items.get_bandAmount())];
        User.Authority authority = User.Authority.BAND;

        //ACT
        begin();
        Band actual = BandPersistenceService.get_instance().create(expected,_password);
        calculatePerformance("creating " + authority);

        //ASSERT
        asserting(expected,actual,authority);

        readUpdateDelete(expected,actual,authority);
    }

    private void readUpdateDelete(User expected, User actual, User.Authority authority) {
        if (actual != null) {
            expected = actual;

            //ACT
            actual = read(expected, authority);

            //ASSERT
            asserting(expected, actual, authority);

            if (actual != null) {
                // ACT
                expected.set_description("This is a new description");
                actual = update(expected,_password,authority);

                // ASSERT
                asserting(expected, actual, authority);
            } else fail();
        } else fail();

        // ACT and ASSERT
        assertTrue(delete(actual == null ? expected : actual, authority));
    }
    private User read(User user, User.Authority authority) {
        begin();
        user = Assembly.get_instance().getUser(user.get_primaryId());
        calculatePerformance("read " + authority);
        return user;
    }
    private User update(User user, String password, User.Authority authority) {
        begin();
        user = UserPersistenceService.get_instance().update(user, new Login(user.get_username(), password), "%&123456789");
        calculatePerformance("read " + authority);
        return user;
    }
    private boolean delete(User user, User.Authority authority) {
        begin();
        boolean result = UserPersistenceService.get_instance().delete(user).get_truth();
        calculatePerformance("delete " + authority);
        return result;
    }
    private void asserting(User expected, User actual, User.Authority authority) {
        switch (authority) {
            case PARTICIPANT -> assertParticipants((Participant) expected, (Participant) actual);
            case BAND -> assertBands((Band) expected,(Band) actual);
            case ARTIST -> assertArtists((Artist) expected,(Artist) actual);
            case VENUE -> assertVenues((Venue) expected,(Venue) actual);
            default -> fail();
        }
    }
    private void assertParticipants(Participant expected, Participant actual) {
        assertUsers(expected, actual);
        if (expected.get_idols().size() == actual.get_idols().size())
            for (int i = 1; i <= expected.get_idols().size(); i++)
                assertEquals(expected.get_idols().get(i).toString(),actual.get_idols().get(i).toString());
        else fail();
    }

    private void assertBands(Band expected, Band actual) {
        assertPerformers(expected, actual);
        if (expected.get_members().size() == actual.get_members().size())
            for (int i = 1; i <= expected.get_members().size(); i++)
                assertEquals(expected.get_members().get(i).toString(),actual.get_members().get(i).toString());
        else fail();
        assertEquals(expected.get_runner(),actual.get_runner());
    }

    private void assertArtists(Artist expected, Artist actual) {
        assertPerformers(expected,actual);
        if (expected.get_bands().size() == actual.get_bands().size())
            for (int i = 1; i <= expected.get_bands().size(); i++)
                assertEquals(expected.get_bands().get(i).toString(),actual.get_bands().get(i).toString());
        else fail();
        assertEquals(expected.get_runner(),actual.get_runner());
        assertRequests(expected.get_requests(),actual.get_requests());
    }

    private void assertPerformers(Performer expected, Performer actual) {
        assertUsers(expected, actual);
        if (expected.get_fans().size() == actual.get_fans().size())
            for (int i = 1; i <= expected.get_fans().size(); i++)
                assertEquals(expected.get_fans().get(i).toString(),actual.get_fans().get(i).toString());
        else fail();
        if (expected.get_idols().size() == actual.get_idols().size())
            for (int i = 1; i <= expected.get_idols().size(); i++)
                assertEquals(expected.get_idols().get(i).toString(),actual.get_idols().get(i).toString());
        else fail();
    }

    private void assertVenues(Venue expected, Venue actual) {
        assertUsers(expected, actual);
        assertEquals(expected.get_location(),actual.get_location());
        assertEquals(expected.get_gearDescription(),actual.get_gearDescription());
        assertEquals(expected.get_size(),actual.get_size());
        assertRequests(expected.get_requests(),actual.get_requests());
    }

    private void assertUsers(User expected, User actual) {
        if (expected.get_primaryId() > 0)
            assertEquals(expected.get_primaryId(),actual.get_primaryId());
        assertEquals(expected.get_username(),actual.get_username());
        assertEquals(expected.get_firstName(),actual.get_firstName());
        assertEquals(expected.get_lastName(),actual.get_lastName());
        assertEquals(expected.get_description(),actual.get_description());
        assertContactInfos(expected.get_contactInfo(),actual.get_contactInfo());
        assertAlbums(expected.get_albums(),actual.get_albums());
        assertRatings(expected.get_ratings(),actual.get_ratings());
        assertEvents(expected.get_events(),actual.get_events());
        assertChatRooms(expected.get_chatRooms(),actual.get_chatRooms());
        assertSubscriptions(expected.get_subscription(),actual.get_subscription());
        assertBulletins(expected.get_bulletins(),actual.get_bulletins());
        assertEquals(expected.get_timestamp(),actual.get_timestamp());
    }

    private void assertContactInfos(ContactInfo expected, ContactInfo actual) {
        assertEquals(expected.get_email(),actual.get_email());
        assertEquals(expected.get_phone().get_country().get_title(),actual.get_phone().get_country().get_title());
        assertEquals(expected.get_phone().get_country().get_indexes(),actual.get_phone().get_country().get_indexes());
        assertEquals(expected.get_phone().get_country().get_firstPhoneNumberDigits(),actual.get_phone().get_country().get_firstPhoneNumberDigits());
        assertEquals(expected.get_phone().get_numbers(),actual.get_phone().get_numbers());
        assertEquals(expected.get_phone().is_mobile(),actual.get_phone().is_mobile());
        assertEquals(expected.getAddressInfo(),actual.getAddressInfo());
        assertEquals(expected.get_country().get_title(),actual.get_country().get_title());
        assertEquals(expected.get_country().get_indexes(),actual.get_country().get_indexes());
        assertEquals(expected.get_country().get_firstPhoneNumberDigits(),actual.get_country().get_firstPhoneNumberDigits());
    }
    private void assertAlbums(Liszt<Album> expectations, Liszt<Album> actuals) {
        if (expectations.size() == actuals.size()) {
            for (int i = 1; i <= expectations.size(); i++) {
                Album expected = expectations.get(i),
                        actual = actuals.get(i);
                assertEquals(expected.toString(),actual.toString());
                for (int j = 1; j <= expected.get_items().size(); j++) {
                    for (int k = 1; k <= expected.get_items().get(j).get_tags().size(); k++) {
                        assertEquals(expected.get_items().get(j).get_tags().get(k).get_primaryId(),
                                actual.get_items().get(j).get_tags().get(k).get_primaryId());
                    }
                    assertEquals(expected.get_items().get(j).get_endpoint(),actual.get_items().get(j).get_endpoint());
                    if (expected.get_items().get(j).get_event() != null)
                        assertEquals(expected.get_items().get(j).get_event().get_primaryId(),actual.get_items().get(j).get_primaryId());
                    assertEquals(expected.get_items().get(j).get_kind(),actual.get_items().get(j).get_kind());
                }
            }
        } else fail();
    }
    private void assertRatings(Liszt<Rating> expectations, Liszt<Rating> actuals) {
        if (expectations.size() == actuals.size()) {
            for (int i = 1; i <= expectations.size(); i++) {
                Rating expected = expectations.get(i),
                        actual = actuals.get(i);
                assertEquals(expected.get_appointed().get_primaryId(),actual.get_appointed().get_primaryId());
                assertEquals(expected.get_judge().get_primaryId(),actual.get_judge().get_primaryId());
                assertEquals(expected.get_value(),actual.get_value());
                assertTrue((expected.get_value() <= 5 && expected.get_value() > 0)
                    && (actual.get_value() <= 5 && actual.get_value() > 0));
                assertEquals(expected.get_comment(),actual.get_comment());
            }
        } else fail();
    }
    private void assertEvents(Liszt<Event> expected, Liszt<Event> actual) {
        if (expected.size() == actual.size())
            for (int i = 1; i <= expected.size(); i++)
                assertEquals(expected.get(i).toString(),actual.get(i).toString());
        else fail();
    }
    private void assertChatRooms(Liszt<ChatRoom> expectations, Liszt<ChatRoom> actuals) {
        if (expectations.size() == actuals.size()) {
            for (int i = 1; i <= expectations.size(); i++) {
                ChatRoom expected = expectations.get(i),
                        actual = actuals.get(i);
                assertMails(expected.get_mails(),actual.get_mails());
                for (int j = 1; j <= expected.get_chatters().size(); j++)
                    assertEquals(expected.get_chatters().get(j).toString(),actual.get_chatters().get(j).toString());
                assertEquals(expected.get_responsible().toString(),actual.get_responsible().toString());
                assertEquals(expected.get_answeringTime(),actual.get_answeringTime());
                assertEquals(expected.is_answered(),actual.is_answered());
            }
        } else fail();
    }
    private void assertMails(Liszt<Mail> expectations, Liszt<Mail> actuals) {
        if (expectations.size() == actuals.size()) {
            for (int i = 1; i <= expectations.size(); i++) {
                Mail expected = expectations.get(i),
                        actual = actuals.get(i);
                assertEquals(expected.get_primaryId(),actual.get_primaryId());
                assertEquals(expected.get_chatRoom().toString(),actual.toString());
                assertEquals(expected.get_author().toString(),actual.toString());
                assertEquals(expected.get_content(),actual.get_content());
                assertEquals(expected.is_sent(),actual.is_sent());
                assertEquals(expected.get_edited().toString(),actual.get_edited().toString());
                assertEquals(expected.is_public(),actual.is_public());
                assertEquals(expected.get_timestamp(),actual.get_timestamp());
            }
        } else fail();
    }
    private void assertSubscriptions(Subscription expected, Subscription actual) {
        assertEquals(expected.get_user().toString(),actual.get_user().toString());
        assertEquals(expected.get_type(),actual.get_type());
        assertEquals(expected.get_status(),actual.get_status());
        assertEquals(expected.get_price(),actual.get_price());
        assertEquals(expected.get_offer().get_expires(),actual.get_offer().get_expires());
        assertEquals(expected.get_offer().get_type(),actual.get_offer().get_type());
        assertEquals(expected.get_offer().get_effect(),actual.get_offer().get_effect());
        assertEquals(expected.get_cardId(),actual.get_cardId());
    }
    private void assertBulletins(Liszt<Bulletin> expectations, Liszt<Bulletin> actuals) {
        if (expectations.size() == actuals.size()) {
            for (int i = 1; i <= expectations.size(); i++) {
                Bulletin expected = expectations.get(i),
                    actual = actuals.get(i);
                assertEquals(expected.get_primaryId(),actual.get_primaryId());
                assertEquals(expected.get_author().toString(),actual.get_author().toString());
                assertEquals(expected.get_receiver().toString(),actual.get_receiver().toString());
                assertEquals(expected.get_content(),actual.get_content());
                assertEquals(expected.is_sent(),actual.is_sent());
                assertEquals(expected.get_edited().toString(),actual.get_edited().toString());
                assertEquals(expected.is_public(),actual.is_public());
                assertEquals(expected.get_timestamp(),actual.get_timestamp());
            }
        } else fail();
    }
    private void assertRequests(Liszt<Request> expectations, Liszt<Request> actuals) {
        if (expectations.size() == actuals.size()) {
            for (int i = 1; i <= expectations.size(); i++) {
                Request expected = expectations.get(i),
                        actual = actuals.get(i);
                assertEquals(expected.get_user().toString(),actual.get_user().toString());
                assertEquals(expected.get_event().toString(),actual.get_event().toString());
                assertEquals(expected.get_approved().get_truth(),actual.get_approved().get_truth());
                assertEquals(expected.get_message(),actual.get_message());
            }
        } else fail();
    }
}