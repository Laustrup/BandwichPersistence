package laustrup.bandwichpersistence.tests.crud;

import laustrup.models.Rating;
import laustrup.models.albums.Album;
import laustrup.models.users.Login;
import laustrup.models.users.User;
import laustrup.models.users.sub_users.bands.Artist;
import laustrup.models.users.sub_users.bands.Band;
import laustrup.models.users.sub_users.participants.Participant;
import laustrup.models.users.sub_users.venues.Venue;
import laustrup.models.users.subscriptions.Subscription;
import laustrup.services.RandomCreatorService;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services.*;
import laustrup.bandwichpersistence.tests.PersistenceTester;
import laustrup.utilities.collections.lists.Liszt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static laustrup.quality_assurance.inheritances.aaa.assertions.AssertionFailer.failing;
import static org.junit.jupiter.api.Assertions.*;

class UserCRUDTests extends PersistenceTester<Object> {

    private String _prevDescription, _postDescription, _carlosPassword;

    private Subscription.Status _prevStatus, _postStatus;

    @Test
    @SuppressWarnings("unchecked")
    void canReadAllUsers() {
        test(() -> {
            arrange();
            Liszt<User> users = (Liszt<User>) act(() -> Assembly.get_instance().getUsers(), "read all users");
            assertTrue(users != null && !users.isEmpty());
        });
    }

    @Test
    void canUpsertRating() {
        test(() -> {
            Rating expected = (Rating) arrange(() -> new Rating(
                    _random.nextInt(5)+1,
                    Assembly.get_instance().getUser(1),
                    Assembly.get_instance().getUser(2),
                    LocalDateTime.now()
                )
            );

            User user = (User) act(() -> UserPersistenceService.get_instance().upsert(expected), "upsert insert rating");

            assertRatings(new Liszt<>(new Rating[]{expected}), new Liszt<>(new Rating[]{user.get_ratings().getLast()}));

            expected.set_value(RandomCreatorService.get_instance().generateDifferent(expected.get_value(),5+1));

            user = (User) act(() -> UserPersistenceService.get_instance().upsert(expected), "upsert update rating");

            assertRatings(new Liszt<>(new Rating[]{expected}), new Liszt<>(new Rating[]{user.get_ratings().getLast()}));
        });
    }

    @Test
    void canUpsertAlbum() {
        test(() -> {
            Album expected = (Album) arrange(() -> new Album(
                    "Test album", _items.generateAlbumItems(), Assembly.get_instance().getUser(1)
                )
            );

            User user = (User) act(() -> UserPersistenceService.get_instance().upsert(expected));

            assertAlbums(new Liszt<>(new Album[]{expected}), new Liszt<>(new Album[]{user.get_albums().getLast()}));
        });

    }

    @Test
    void canFollowAndUnfollow() {
        test(() -> {
            User[] arrangements = (User[]) arrange(() ->
                new User[]{Assembly.get_instance().getUser(1),Assembly.get_instance().getUser(2)}
            );
            User fan = arrangements[0],
                idol = arrangements[1];

            User[] acts = (User[]) act(() -> UserPersistenceService.get_instance().follow(fan, idol), "following");

            assertTrue(acts != null && acts.length == 2);

            acts = (User[]) act(() -> UserPersistenceService.get_instance().unfollow(fan, idol), "unfollowing");

            assertTrue(acts != null && acts.length == 2);
        });
    }

    @Test
    void canUpdateUser() {
        test(() -> {
            User expected = (User) arrange(() -> {
                    User user = get_carlos();
                    _prevDescription = user.get_description();
                    _postDescription = "This is a new description";
                    _carlosPassword = "laust!_er_sej1";
                    return user;
                }
            );
            expected.set_description(_postDescription);

            User actual = (User) act(() ->
                    UserPersistenceService.get_instance().update(
                    expected,
                    new Login(expected.get_username(), _carlosPassword),
                    _password
                )
            );

            expected.set_description(_prevDescription);
            UserPersistenceService.get_instance().update(
                    expected,
                    new Login(expected.get_username(), _carlosPassword),
                    _password
            );

            expected.set_description(_postDescription);
            asserting(expected,actual,expected.get_authority());
        });

    }

    // TODO Fix card function in upsert
    @Test
    void canUpsertSubscription() {
        test(() -> {
            User expected = (User) arrange(() -> {
                User user = get_carlos();
                _carlosPassword = "laust!_er_sej1";
                _prevStatus = user.get_subscription().get_status();
                _postStatus = Subscription.Status.BLOCKED;
                user.get_subscription().set_status(_postStatus);
                return user;
            });

            User actual = (User) act(() -> UserPersistenceService.get_instance().upsert(
                    expected,
                    new Login(expected.get_username(),_carlosPassword),
                    null
                )
            );

            asserting(expected,actual,expected.get_authority());

            expected.get_subscription().set_status(_prevStatus);
            UserPersistenceService.get_instance().upsert(expected, new Login(expected.get_username(),_carlosPassword),null);
        });
    }

    @ParameterizedTest
    @CsvSource(value = {"1","5","6","8"})
    void canAssembleUser(long id) {
        test(() -> {
            arrange();
            notNull(act(() -> Assembly.get_instance().getUser(id)));
        });
    }

    @Test
    void canCRUDArtist() {
        test(() -> {
            Object[] arrangement = (Object[]) arrange(() ->
                new Object[] {
                    _items.get_artists()[_random.nextInt(_items.get_artistAmount())],
                    User.Authority.ARTIST
                }
            );
            Artist expected = (Artist) arrangement[0];
            User.Authority authority = (User.Authority) arrangement[1];

            Artist actual = (Artist) act(expected, e ->
                    ArtistPersistenceService.get_instance().create((Artist) e,_password),"creating " + authority
            );

            // Adding id
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

            asserting(expected,actual,authority);
            readUpdateDelete(expected,actual,authority);
        });

    }

    @Test
    void canCRUDVenue() {
        test(() -> {
            Object[] arrangement = (Object[]) arrange(() ->
                new Object[]{_items.get_venues()[_random.nextInt(_items.get_venueAmount())],User.Authority.VENUE}
            );
            Venue expected = (Venue) arrangement[0];
            User.Authority authority = (User.Authority) arrangement[1];

            Venue actual = (Venue) act(() ->
                VenuePersistenceService.get_instance().create(expected,_password),"creating " + authority
            );

            asserting(expected,actual,authority);
            readUpdateDelete(expected,actual,authority);
        });
    }

    @Test
    void canCRUDParticipant() {
        test(() -> {
            Object[] arrangement = (Object[]) arrange(() ->
                new Object[]{_items.get_participants()[_random.nextInt(_items.get_participantAmount())],User.Authority.PARTICIPANT}
            );
            Participant expected = (Participant) arrangement[0];
            User.Authority authority = (User.Authority) arrangement[1];

            Participant actual = (Participant) act(() ->
                ParticipantPersistenceService.get_instance().create(expected,_password),"creating " + authority
            );

            asserting(expected,actual,authority);
            readUpdateDelete(expected,actual,authority);
        });
    }

    @Test
    void canCRUDBand() {
        test(() -> {
            Object[] arrangement = (Object[]) arrange(() ->
                new Object[]{_items.get_bands()[_random.nextInt(_items.get_bandAmount())],User.Authority.BAND}
            );
            Band expected = (Band) arrangement[0];
            User.Authority authority = (User.Authority) arrangement[1];

            Band actual = (Band) act(() ->
                BandPersistenceService.get_instance().create(expected,_password),"creating " + authority
            );

            asserting(expected,actual,authority);

            readUpdateDelete(expected,actual,authority);
        });
    }

    private void readUpdateDelete(User expected, User actual, User.Authority authority) {
        if (actual != null) {
            expected = actual;
            actual = read(expected, authority);
            asserting(expected, actual, authority);

            if (actual != null) {
                actual = logIn(expected, authority);

                asserting(expected, actual, authority);

                if (actual != null) {
                    expected.set_description("This is a new description");
                    actual = update(expected,_password,authority);

                    asserting(expected, actual, authority);
                } else failing(isNullMessage(actual));
            } else failing(isNullMessage(actual));
        } else failing(isNullMessage(actual));

        assertTrue(delete(actual == null ? expected : actual, authority));
    }
    private User logIn(User user, User.Authority authority) {
        return (User) act(() ->
            Assembly.get_instance().getUser(new Login(user.get_username(),_password)),
        "login " + authority
        );
    }
    private User read(User user, User.Authority authority) {
        return (User) act(() ->
            Assembly.get_instance().getUser(user.get_primaryId()),
            "read " + authority
        );
    }
    private User update(User user, String password, User.Authority authority) {
        return (User) act(() ->
            UserPersistenceService.get_instance().update(user,
                    new Login(user.get_username(), password), "%&123456789"
            ),
            "read " + authority
        );
    }
    private boolean delete(User user, User.Authority authority) {
        return (boolean) act(() ->
            UserPersistenceService.get_instance().delete(user).get_truth(),
            "delete " + authority
        );
    }
}