package laustrup.bandwichpersistence.items.aaa.assertions;

import laustrup.bandwichpersistence.items.aaa.Actor;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.events.Participation;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistence.utilities.collections.lists.Liszt;
import laustrup.bandwichpersistence.utilities.console.Printer;
import laustrup.bandwichpersistence.utilities.parameters.Plato;

import javax.lang.model.element.UnknownElementException;

import static laustrup.bandwichpersistence.items.aaa.assertions.AssertionActor.*;
import static laustrup.bandwichpersistence.items.aaa.assertions.AssertionChecker.*;
import static laustrup.bandwichpersistence.items.aaa.assertions.AssertionFailer.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class Asserter<T,R> extends Actor<T,R> {

    /**
     * Generates a message for cases, where an element such or should not be null.
     * @param object The Object that should either be null or not.
     * @return The generated message.
     */
    protected String isNullMessage(Object object) {
        if (object == null)
            return "An object is null, where it is not supposed to be...";
        return object.toString() + " isn't null...";
    }

    /**
     * Will generate a message of the assertion fail of an unknown switch case occurring at default case.
     * @param element The element that results in the fail.
     * @return The generated message.
     */
    protected String switchElementUnknown(Object element) {
        return element.toString() + " type unknown of " + element.getClass();
    }

    /**
     * Asserts two Users to check they are the same.
     * @param expected The User that is arranged and defined.
     * @param actual The User that is the result of an action.
     * @param authority The authority of both Users.
     */
    protected void asserting(User expected, User actual, User.Authority authority) {
        Printer.get_instance().print("Expected = " + expected + "\n\nActual = " + actual);
        switch (authority) {
            case PARTICIPANT -> assertParticipants((Participant) expected, (Participant) actual, true);
            case BAND -> assertBand((Band) expected,(Band) actual);
            case ARTIST -> assertArtists((Artist) expected,(Artist) actual, true);
            case VENUE -> assertVenues((Venue) expected,(Venue) actual);
            default -> failing(switchElementUnknown(authority), new UnknownElementException(null,authority));
        }
    }

    /**
     * Asserts two Participants to check they are the same.
     * @param expected The Participant that is arranged and defined.
     * @param actual The Participant that is the result of an action.
     */
    protected void assertParticipants(Participant expected, Participant actual, boolean assertNotNull) {
        doAssert(a -> {
            if (!allowObjects(expected,actual))
                return objectMessage(expected,actual);

            assertUsers(expected, actual);
            if (allowLiszt(expected.get_idols(),actual.get_idols()))
                assertFor(1,expected.get_idols().size(), i -> {
                    assertEquals(expected.get_idols().Get(i).toString(),actual.get_idols().Get(i).toString());
                    return null;
            });

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two Bands to check they are the same.
     * @param expected The Band that is arranged and defined.
     * @param actual The Band that is the result of an action.
     */
    protected void assertBand(Band expected, Band actual) {
        doAssert(a -> {
            if (!allowObjects(expected,actual))
                return objectMessage(expected,actual);

            assertPerformers(expected, actual);
            if (allowLiszt(expected.get_members(),actual.get_members()))
                assertFor(1,expected.get_members().size(), i -> {
                    assertEquals(expected.get_members().Get(i).toString(),actual.get_members().Get(i).toString());
                    return null;
            });
            assertEquals(expected.get_runner(),actual.get_runner());

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two Artists to check they are the same.
     * @param expected The Artist that is arranged and defined.
     * @param actual The Artist that is the result of an action.
     */
    protected void assertArtists(Artist expected, Artist actual, boolean assertNotNull) {
        doAssert(a -> {
            if (!allowObjects(expected,actual))
                return objectMessage(expected,actual);

            assertPerformers(expected,actual);
            if (allowLiszt(expected.get_bands(),actual.get_bands())) {
                assertFor(1,expected.get_bands().size(), i -> {
                    assertEquals(expected.get_bands().Get(i).toString(),actual.get_bands().Get(i).toString());
                    return null;
                });
            }

            assertEquals(expected.get_runner(),actual.get_runner());
            assertRequests(expected.get_requests(),actual.get_requests());

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two Performers to check they are the same.
     * @param expected The Performer that is arranged and defined.
     * @param actual The Performer that is the result of an action.
     */
    private void assertPerformers(Performer expected, Performer actual) {
        doAssert(a -> {
            if (!allowObjects(expected,actual))
                return objectMessage(expected,actual);

            assertUsers(expected, actual);
            if (allowLiszt(expected.get_fans(),actual.get_fans())) {
                assertFor(1,expected.get_fans().size(), i -> {
                    assertEquals(expected.get_fans().Get(i).toString(),actual.get_fans().Get(i).toString());
                    return null;
                });
            }
            if (allowLiszt(expected.get_idols(),actual.get_idols())) {
                assertFor(1,expected.get_idols().size(), i -> {
                    assertEquals(expected.get_idols().Get(i).toString(),actual.get_idols().Get(i).toString());
                    return null;
                });
            }

            return AssertionMessage.SUCCESS.get_content();
        });

    }

    /**
     * Asserts two Venues to check they are the same.
     * @param expected The Venue that is arranged and defined.
     * @param actual The Venue that is the result of an action.
     */
    protected void assertVenues(Venue expected, Venue actual) {
        doAssert(a -> {
            if (!allowObjects(expected,actual))
                return objectMessage(expected,actual);

            assertUsers(expected, actual);
            assertEquals(expected.get_location(),actual.get_location());
            assertEquals(expected.get_gearDescription(),actual.get_gearDescription());
            assertEquals(expected.get_size(),actual.get_size());
            assertRequests(expected.get_requests(),actual.get_requests());

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two Users to check they are the same.
     * @param expected The User that is arranged and defined.
     * @param actual The User that is the result of an action.
     */
    private void assertUsers(User expected, User actual) {
        doAssert(a -> {
            if (!allowObjects(expected,actual))
                return objectMessage(expected,actual);

            assertIf(allowId(expected.get_primaryId(),actual.get_primaryId()),
                expected.get_primaryId(),actual.get_primaryId());
            assertEquals(expected.get_username(),actual.get_username());
            assertEquals(expected.get_firstName(),actual.get_firstName());
            assertEquals(expected.get_lastName(),actual.get_lastName());
            assertEquals(expected.get_description(),actual.get_description());
            asserting(expected.get_contactInfo(),actual.get_contactInfo());
            assertAlbums(expected.get_albums(),actual.get_albums());
            assertRatings(expected.get_ratings(),actual.get_ratings());
            assertEvents(expected.get_events(),actual.get_events());
            assertChatRooms(expected.get_chatRooms(),actual.get_chatRooms());
            asserting(expected.get_subscription(),actual.get_subscription());
            assertBulletins(expected.get_bulletins(),actual.get_bulletins());

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts Albums to check they are the same.
     * @param expectations The Albums that are arranged and defined.
     * @param actuals The Albums that are the result of an action.
     */
    protected void assertAlbums(Liszt<Album> expectations, Liszt<Album> actuals) {
        doAssert(a -> {
            if (!allowLiszt(expectations, actuals))
                return lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++) {
                Album expected = expectations.Get(i),
                        actual = actuals.Get(i);

                if (!expected.get_items().isEmpty() && !actual.get_items().isEmpty()) {
                    for (int j = 1; j <= expected.get_items().size(); j++) {
                        assertFor(new int[]{1,j},expected.get_items().Get(j).get_tags().size(), indexes -> {
                            assertEquals(expected.get_items().Get(indexes[1]).get_tags().Get(indexes[0]).get_primaryId(),
                                    actual.get_items().Get(indexes[1]).get_tags().Get(indexes[0]).get_primaryId());
                            return null;
                        });
                        assertEquals(expected.get_items().Get(j).get_endpoint(),actual.get_items().Get(j).get_endpoint());
                        assertIf(expected.get_items().Get(j).get_event() != null,
                                expected.get_items().Get(j).get_event().get_primaryId(),actual.get_items().Get(j).get_primaryId());
                        assertEquals(expected.get_items().Get(j).get_kind(),actual.get_items().Get(j).get_kind());
                    }
                }
            }

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts Ratings to check they are the same.
     * @param expectations The Ratings that are arranged and defined.
     * @param actuals The Ratings that are the result of an action.
     */
    protected void assertRatings(Liszt<Rating> expectations, Liszt<Rating> actuals) {
        doAssert(a -> {
            if (!allowLiszt(expectations,actuals))
                return lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++) {
                Rating expected = expectations.Get(i),
                        actual = actuals.Get(i);

                assertIf(allowObjects(expected.get_appointed(),actual.get_appointed()),
                        expected.get_appointed().get_primaryId(),actual.get_appointed().get_primaryId());

                assertIf(allowObjects(expected.get_judge(),actual.get_judge()),
                        expected.get_judge().get_primaryId(),actual.get_judge().get_primaryId());
                assertEquals(expected.get_value(),actual.get_value());
                asserting((expected.get_value() <= 5 && expected.get_value() > 0)
                        && (actual.get_value() <= 5 && actual.get_value() > 0));
                assertEquals(expected.get_comment(),actual.get_comment());
            }

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts ChatRooms to check they are the same.
     * @param expectations The ChatRooms that are arranged and defined.
     * @param actuals The ChatRooms that are the result of an action.
     */
    public void assertChatRooms(Liszt<ChatRoom> expectations, Liszt<ChatRoom> actuals) {
        doAssert(a -> {
            if (!allowLiszt(expectations,actuals))
                return lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++) {
                ChatRoom expected = expectations.Get(i),
                        actual = actuals.Get(i);
                assertMails(expected.get_mails(),actual.get_mails());
                assertFor(1,expected.get_chatters().size(), index -> {
                    assertEquals(expected.get_chatters().Get(index).toString(),actual.get_chatters().Get(index).toString());
                    return null;
                });
                assertEquals(expected.get_responsible().toString(),actual.get_responsible().toString());
                assertEquals(expected.get_answeringTime(),actual.get_answeringTime());
                assertEquals(expected.is_answered(),actual.is_answered());
            }

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts Mails to check they are the same.
     * @param expectations The Mails that are arranged and defined.
     * @param actuals The Mails that are the result of an action.
     */
    protected void assertMails(Liszt<Mail> expectations, Liszt<Mail> actuals) {
        doAssert(a -> {
            if (!allowLiszt(expectations,actuals))
                return lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++) {
                Mail expected = expectations.Get(i), actual = actuals.Get(i);

                assertEquals(expected.get_primaryId(),actual.get_primaryId());
                assertEquals(expected.get_chatRoom().toString(),actual.get_chatRoom().toString());
                assertEquals(expected.get_author().toString(),actual.get_author().toString());
                assertEquals(expected.get_content(),actual.get_content());
                assertEquals(expected.is_sent(),actual.is_sent());
                assertEquals(expected.get_edited().toString(),actual.get_edited().toString());
                assertEquals(expected.is_public(),actual.is_public());
                assertEquals(expected.get_timestamp(),actual.get_timestamp());
            }

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two Subscriptions to check they are the same.
     * @param expected The Subscription that is arranged and defined.
     * @param actual The Subscription that is the result of an action.
     */
    protected void asserting(Subscription expected, Subscription actual) {
        doAssert(a -> {
            if (!allowObjects(expected,actual))
                return objectMessage(expected,actual);

            assertEquals(expected.get_user().toString(),actual.get_user().toString());
            assertEquals(expected.get_type(),actual.get_type());
            assertEquals(expected.get_status(),actual.get_status());
            assertEquals(expected.get_price(),actual.get_price());
            assertEquals(expected.get_cardId(),actual.get_cardId());

            if (allowObjects(expected.get_offer(),actual.get_offer())) {
                assertIf(actual.get_offer().get_expires() != null,
                    expected.get_offer().get_expires(),actual.get_offer().get_expires());
                assertIf(actual.get_offer().get_type() != null,
                    expected.get_offer().get_type(),actual.get_offer().get_type());
                assertIf(actual.get_offer().get_effect() > 0,
                    expected.get_offer().get_effect(),actual.get_offer().get_effect());
            }

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts Requests to check they are the same.
     * @param expectations The Requests that are arranged and defined.
     * @param actuals The Requests that are the result of an action.
     */
    protected void assertRequests(Liszt<Request> expectations, Liszt<Request> actuals) {
        doAssert(a -> {
            if (!allowLiszt(expectations,actuals))
                return lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++) {
                Request expected = expectations.Get(i),
                        actual = actuals.Get(i);
                assertUsers(expected.get_user(),actual.get_user());
                asserting(expected.get_event(),actual.get_event());
                assertEquals(expected.get_approved().get_truth(),actual.get_approved().get_truth());
                assertEquals(expected.get_message(),actual.get_message());
            }

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two ContactInfos to check they are the same.
     * @param expected The information that is arranged and defined.
     * @param actual The information that is the result of an action.
     */
    protected void asserting(ContactInfo expected, ContactInfo actual) {
        doAssert(a -> {
            if (!allowObjects(expected,actual))
                return objectMessage(expected,actual);

            assertEquals(expected.get_email(),actual.get_email());
            assertEquals(expected.get_country().get_title(),actual.get_country().get_title());
            assertEquals(expected.get_country().get_indexes(),actual.get_country().get_indexes());
            assertEquals(expected.get_phone().get_country().get_firstPhoneNumberDigits(),actual.get_phone().get_country().get_firstPhoneNumberDigits());
            assertEquals(expected.get_phone().get_numbers(),actual.get_phone().get_numbers());
            assertEquals(expected.get_phone().is_mobile(),actual.get_phone().is_mobile());
            assertEquals(expected.getAddressInfo(),actual.getAddressInfo());

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts Bulletins to check they are the same.
     * @param expectations The Bulletins that are arranged and defined.
     * @param actuals The Bulletins that are the result of an action.
     */
    protected void assertBulletins(Liszt<Bulletin> expectations, Liszt<Bulletin> actuals) {
        doAssert(a -> {
            if (!allowLiszt(expectations,actuals))
                return lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++) {
                Bulletin expected = expectations.Get(i), actual = actuals.Get(i);

                assertEquals(expected.get_primaryId(),actual.get_primaryId());
                assertUsers(expected.get_author(), actual.get_author());
                assertIf(allowObjects(expected.get_author(),actual.get_author()),expected.get_receiver(),actual.get_receiver());
                assertIf(allowObjects(expected.get_receiver(),actual.get_receiver()),expected.get_receiver(),actual.get_receiver());
                assertEquals(expected.get_content(),actual.get_content());
                assertEquals(expected.is_sent(),actual.is_sent());
                asserting(expected.get_edited(),actual.get_edited());
                assertEquals(expected.is_public(),actual.is_public());
            }

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts Events to check they are the same.
     * @param expectations The Events that are arranged and defined.
     * @param actuals The Events that are the result of an action.
     */
    protected void assertEvents(Liszt<Event> expectations, Liszt<Event> actuals) {
        doAssert(a -> {
            if (!allowLiszt(expectations,actuals))
                return lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++)
                asserting(expectations.Get(i),actuals.Get(i));

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two Events to check they are the same.
     * @param expected The Event that is arranged and defined.
     * @param actual The Event that is the result of an action.
     */
    protected void asserting(Event expected, Event actual) {
        doAssert(a -> {
            if (!allowObjects(expected,actual))
                return objectMessage(expected, actual);

            assertIf(allowObjects(expected.get_openDoors(),actual.get_openDoors()),expected.get_openDoors(),actual.get_openDoors());
            assertIf(allowObjects(expected.get_start(),actual.get_start()),expected.get_start(),actual.get_start());
            assertIf(allowObjects(expected.get_end(),actual.get_end()),expected.get_end(),actual.get_end());
            assertIf(allowObjects(expected.get_contactInfo(),actual.get_contactInfo()),expected.get_contactInfo(),actual.get_contactInfo());
            asserting(expected.get_cancelled(),actual.get_cancelled());
            asserting(expected.get_soldOut(),actual.get_soldOut());
            asserting(expected.get_voluntary(),actual.get_voluntary());
            asserting(expected.get_public(),actual.get_public());
            assertEquals(expected.get_length(),expected.get_length());
            assertEquals(expected.get_location(), actual.get_location());
            assertEquals(expected.get_ticketsURL(), actual.get_ticketsURL());
            assertGigs(expected.get_gigs(),actual.get_gigs());
            assertVenues(expected.get_venue(),actual.get_venue());
            assertRequests(expected.get_requests(),actual.get_requests());
            assertParticipations(expected.get_participations(),actual.get_participations());
            assertAlbums(expected.get_albums(),actual.get_albums());

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts Participations to check they are the same.
     * @param expectations The Participations that are arranged and defined.
     * @param actuals The Participations that are the result of an action.
     */
    protected void assertParticipations(Liszt<Participation> expectations, Liszt<Participation> actuals) {
        doAssert(a -> {
            if (!allowLiszt(expectations,actuals))
                return lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++)
                asserting(expectations.Get(i),actuals.Get(i));

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two Participations to check they are the same.
     * @param expected The Participation that is arranged and defined.
     * @param actual The Participation that is the result of an action.
     */
    protected void asserting(Participation expected, Participation actual) {
        doAssert(a -> {
            if (!allowObjects(expected,actual))
                return objectMessage(expected,actual);

            assertEquals(expected.toString(),actual.toString());

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts Gigs to check they are the same.
     * @param expectations The Gigs that are arranged and defined.
     * @param actuals The Gigs that are the result of an action.
     */
    protected void assertGigs(Liszt<Gig> expectations, Liszt<Gig> actuals) {
        doAssert(a -> {
            if (!allowLiszt(expectations,actuals))
                return lisztMessage(expectations,actuals);

            for (int i = 1; i <= expectations.size(); i++)
                asserting(expectations.Get(i),actuals.Get(i));

            return AssertionMessage.SUCCESS.get_content();
        });
    }



    /**
     * Asserts two Gigs to check they are the same.
     * @param expected The Gig that is arranged and defined.
     * @param actual The Gig that is the result of an action.
     */
    protected void asserting(Gig expected, Gig actual) {
        doAssert(a -> {
            if (!allowObjects(expected,actual))
                return objectMessage(expected,actual);

            asserting(expected.get_event(),actual.get_event());
            asserting(expected.get_act(),actual.get_act());
            assertEquals(expected.get_start(),actual.get_start());
            assertEquals(expected.get_end(),actual.get_end());

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two acts to check they are the same.
     * @param expectations The acts that is arranged and defined.
     * @param actuals The acts that is the result of an action.
     */
    protected void asserting(Performer[] expectations, Performer[] actuals) {
        doAssert(a -> {
            if (!allowCollection(expectations,actuals))
                return collectionMessage(expectations,actuals);

            for (int i = 0; i < expectations.length; i++)
                assertPerformers(expectations[i], actuals[i]);

            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Asserts two Platos to check they are the same.
     * @param expected The act that is arranged and defined.
     * @param actual The act that is the result of an action.
     */
    protected void asserting(Plato expected, Plato actual) {
        asserting(expected.toString(),expected.toString());
    }

    /**
     * Is used to determine whether a statement is true.
     * Uses the assertTrue method.
     * @param isTrue A statement or value that must be true.
     */
    protected void asserting(boolean isTrue) {
        doAssert(a -> {
            assertTrue(isTrue);
            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * A simple comparing assertEqual of two Objects.
     * @param expected The Object that is arranged and defined.
     * @param actual The Object that is the result of an action
     */
    protected void asserting(Object expected, Object actual) {
        doAssert(a -> {
                if (!allowObjects(expected,actual))
                    return objectMessage(expected,actual);

                assertEquals(expected,actual);
                return AssertionMessage.SUCCESS.get_content();
            }
        );
    }

    /**
     * A simple comparing assertEqual of two Strings.
     * @param expected The String that is arranged and defined.
     * @param actual The String that is the result of an action
     */
    protected void asserting(String expected, String actual) {
        doAssert(a -> {
            if (!allowObjects(expected,actual))
                return objectMessage(expected,actual);

            assertEquals(expected, actual);
            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Will assert the condition of whether this is null or not.
     * @param object The Object that will be asserted.
     */
    protected void notNull(Object object) {
        doAssert(a -> {
            assertNotNull(object);
            return AssertionMessage.SUCCESS.get_content();
        });
    }

    /**
     * Will add the message to the print and assertion status will be a success.
     * @param message A description of the assertion.
     */
    protected void success(String message) {
        doAssert(a -> {
            _print += "\n\n\t" + message + "\n";
            assertTrue(true);
            return AssertionMessage.SUCCESS.get_content();
        });
    }
}
