package laustrup.bandwichpersistence.crud;

import laustrup.bandwichpersistence.JTest;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistence.services.RandomCreatorService;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services.*;
import laustrup.bandwichpersistence.utilities.Liszt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserCRUDTests extends JTest {

    @Test
    void canReadAllUsers() {
        //ACT
        Liszt<User> users = Assembly.get_instance().getUsers();
        calculatePerformance("read all users");

        //ASSERT
        assertTrue(users != null && !users.isEmpty());
    }

    @Test
    void canUpsertBulletin() {
        //ARRANGE
        Bulletin expected = _items.generateBulletins(Assembly.get_instance().getEvent(1))[0];

        //ACT
        begin();
        User user = UserPersistenceService.get_instance().upsert(expected);
        calculatePerformance("upsert insert bulletin");

        //ASSERT
        assertBulletins(new Liszt<>(new Bulletin[]{expected}), new Liszt<>(new Bulletin[]{user.get_bulletins().getLast()}));

        //ARRANGE
        expected.set_content("This is new content");

        //ACT
        begin();
        user = UserPersistenceService.get_instance().upsert(expected);
        calculatePerformance("upsert update bulletin");

        //ASSERT
        assertBulletins(new Liszt<>(new Bulletin[]{expected}), new Liszt<>(new Bulletin[]{user.get_bulletins().getLast()}));
    }

    @Test
    void canUpsertRating() {
        //ARRANGE
        Rating expected = new Rating(
                _random.nextInt(5)+1,
                Assembly.get_instance().getUser(1),
                Assembly.get_instance().getUser(2),
                LocalDateTime.now()
        );

        //ACT
        begin();
        User user = UserPersistenceService.get_instance().upsert(expected);
        calculatePerformance("upsert insert rating");

        //ASSERT
        assertRatings(new Liszt<>(new Rating[]{expected}), new Liszt<>(new Rating[]{user.get_ratings().getLast()}));

        //ARRANGE
        expected.set_value(RandomCreatorService.get_instance().generateDifferent(expected.get_value(),5+1));

        //ACT
        begin();
        user = UserPersistenceService.get_instance().upsert(expected);
        calculatePerformance("upsert update rating");

        //ASSERT
        assertRatings(new Liszt<>(new Rating[]{expected}), new Liszt<>(new Rating[]{user.get_ratings().getLast()}));
    }

    @Test
    void canUpsertAlbum() {
        //ARRANGE
        Album expected = new Album("Test album", _items.generateAlbumItems(), Assembly.get_instance().getUser(1));

        //ACT
        begin();
        User user = UserPersistenceService.get_instance().upsert(expected);
        calculatePerformance();

        //ARRANGE
        assertAlbums(new Liszt<>(new Album[]{expected}), new Liszt<>(new Album[]{user.get_albums().getLast()}));
    }

    @Test
    void canFollowAndUnfollow() {
        //ARRANGE
        User fan = Assembly.get_instance().getUser(1),
            idol = Assembly.get_instance().getUser(2);

        //ACT
        begin();
        User[] acts = UserPersistenceService.get_instance().follow(fan, idol);
        calculatePerformance("following");

        //ASSERT
        assertTrue(acts != null && acts.length == 2);

        //ACT
        begin();
        acts = UserPersistenceService.get_instance().unfollow(fan, idol);
        calculatePerformance("unfollowing");

        //ASSERT
        assertTrue(acts != null && acts.length == 2);
    }

    @Test
    void canUpdateUser() {
        //ARRANGE
        User expected = Assembly.get_instance().getUser(1);
        String prevDescription = expected.get_description(),
            postDescription = "This is a new description",
            password = "laust!_er_sej1";

        //ACT
        expected.set_description(postDescription);
        begin();
        User actual = UserPersistenceService.get_instance().update(
                expected,
                new Login(expected.get_username(),password),
                _password
        );
        calculatePerformance();

        expected.set_description(prevDescription);
        UserPersistenceService.get_instance().update(
                expected,
                new Login(expected.get_username(),_password),
                password
        );

        //ASSERT
        expected.set_description(postDescription);
        asserting(expected,actual,expected.get_authority());
    }

    //TODO Fix test of card
    @Test
    void canUpsertSubscription() {
        //ARRANGE
        User expected = Assembly.get_instance().getUser(1);
        String password = RandomCreatorService.get_instance().generatePassword();
        Subscription.Status prevStatus = expected.get_subscription().get_status(),
            postStatus = Subscription.Status.BLOCKED;
        expected.get_subscription().set_status(postStatus);

        //ACT
        begin();
        User actual = UserPersistenceService.get_instance().upsert(expected, new Login(expected.get_username(),password),null);
        calculatePerformance();

        //ASSERT
        asserting(expected,actual,expected.get_authority());

        expected.get_subscription().set_status(prevStatus);
        UserPersistenceService.get_instance().upsert(expected, new Login(expected.get_username(),password),null);
    }

    @ParameterizedTest
    @CsvSource(value = {"1","5","6","8"})
    void canAssembleUser(long id) {
        //ACT
        begin();
        User actual = Assembly.get_instance().getUser(id);
        calculatePerformance();

        //ASSERT
        asserting(actual,actual,actual.get_authority());
    }

    @Test
    void canCRUDArtist() {
        //ARRANGE
        Artist expected = _items.get_artists()[_random.nextInt(_items.get_artistAmount())];
        User.Authority authority = User.Authority.ARTIST;

        //ACT
        begin();
        Artist actual = ArtistPersistenceService.get_instance().create(expected,_password);
        calculatePerformance("creating " + authority);
        expected = new Artist(actual.get_primaryId(), expected.get_username(), expected.get_firstName(),
                expected.get_lastName(), expected.get_description(), expected.get_contactInfo(),new Liszt<>(),
                new Liszt<>(), new Liszt<>(), new Liszt<>(), new Liszt<>(),
                new Subscription(
                        new Artist(
                                actual.get_primaryId(), expected.get_username(), expected.get_firstName(),
                                expected.get_lastName(), expected.get_description(), expected.get_contactInfo(),
                                expected.get_albums(), expected.get_ratings(), expected.get_events(),
                                expected.get_gigs(),expected.get_chatRooms(),
                                    new Subscription(expected, Subscription.Type.FREEMIUM, Subscription.Status.ACCEPTED,
                                            null, (long) 0,actual.get_subscription().get_timestamp()
                                    ),
                                new Liszt<>(), new Liszt<>(), expected.get_runner(),
                                new Liszt<>(),new Liszt<>(),new Liszt<>(),expected.get_timestamp()
                        ),
                Subscription.Type.FREEMIUM, Subscription.Status.ACCEPTED, null, (long) 0,
                actual.get_subscription().get_timestamp()),new Liszt<>(), new Liszt<>(),
                expected.get_runner(), new Liszt<>(),new Liszt<>(),new Liszt<>(),
                actual.get_timestamp()
        );

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
                actual = logIn(expected, authority);

                // ASSERT
                asserting(expected, actual, authority);

                if (actual != null) {
                    // ACT
                    expected.set_description("This is a new description");
                    actual = update(expected,_password,authority);

                    // ASSERT
                    asserting(expected, actual, authority);
                } else fail();
            } else fail();
        } else fail();

        // ACT and ASSERT
        assertTrue(delete(actual == null ? expected : actual, authority));
    }
    private User logIn(User user, User.Authority authority) {
        begin();
        user = Assembly.get_instance().getUser(new Login(user.get_username(),_password));
        calculatePerformance("login " + authority);
        return user;
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
}