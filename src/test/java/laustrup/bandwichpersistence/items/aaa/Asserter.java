package laustrup.bandwichpersistence.items.aaa;

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
import laustrup.bandwichpersistence.utilities.collections.Liszt;
import laustrup.bandwichpersistence.utilities.console.Printer;

import javax.lang.model.element.UnknownElementException;

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
     * Will generate a message of the assertion fail of two collections having different sizes.
     * @param elements The two collections, must be of no more than two.
     * @return The generated message.
     */
    protected String notSameSizeMessage(Object[][] elements) {
        String description = new String();
        if (elements.length == 0)
            description = "Collections not specified";
        else if (elements.length >= 2)
            description = elements[0][0].getClass() + " of " + Printer.get_instance().toString(elements[0]) + " and " +
                    Printer.get_instance().toString(elements[1]);
        else
            description = elements[0][0].getClass() + " of " + Printer.get_instance().toString(elements[0]);
        return description + " are not the same size...";
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
            case BAND -> assertBands((Band) expected,(Band) actual, true);
            case ARTIST -> assertArtists((Artist) expected,(Artist) actual, true);
            case VENUE -> assertVenues((Venue) expected,(Venue) actual, true);
            default -> fail(switchElementUnknown(authority), new UnknownElementException(null,authority));
        }
    }

    /**
     * Asserts two Participants to check they are the same.
     * @param expected The Participant that is arranged and defined.
     * @param actual The Participant that is the result of an action.
     */
    protected void assertParticipants(Participant expected, Participant actual, boolean assertNotNull) {
        if ((expected != null && actual != null) || assertNotNull) {
            assertUsers(expected, actual);
            assert expected != null && actual != null;
            if (expected.get_idols().size() == actual.get_idols().size())
                for (int i = 1; i <= expected.get_idols().size(); i++)
                    assertEquals(expected.get_idols().get(i).toString(),actual.get_idols().get(i).toString());
            else
                fail(notSameSizeMessage(new Object[][]{expected.get_idols().get_data(),actual.get_idols().get_data()}));
        }
    }

    /**
     * Asserts two Bands to check they are the same.
     * @param expected The Band that is arranged and defined.
     * @param actual The Band that is the result of an action.
     */
    protected void assertBands(Band expected, Band actual, boolean assertNotNull) {
        if ((expected != null && actual != null) || assertNotNull) {
            assertPerformers(expected, actual);
            assert expected != null && actual != null;
            if (expected.get_members().size() == actual.get_members().size())
                for (int i = 1; i <= expected.get_members().size(); i++)
                    assertEquals(expected.get_members().get(i).toString(),actual.get_members().get(i).toString());
            else fail("Bands are not the same size");
            assertEquals(expected.get_runner(),actual.get_runner());
        }
    }

    /**
     * Asserts two Artists to check they are the same.
     * @param expected The Artist that is arranged and defined.
     * @param actual The Artist that is the result of an action.
     */
    protected void assertArtists(Artist expected, Artist actual, boolean assertNotNull) {
        if ((expected != null && actual != null) || assertNotNull)
            assertPerformers(expected,actual);
        assert expected != null && actual != null;
        if (expected.get_bands().size() == actual.get_bands().size())
            for (int i = 1; i <= expected.get_bands().size(); i++)
                assertEquals(expected.get_bands().get(i).toString(),actual.get_bands().get(i).toString());
        else
            fail(notSameSizeMessage(new Object[][]{expected.get_bands().get_data(),actual.get_bands().get_data()}));

        assertEquals(expected.get_runner(),actual.get_runner());
        assertRequests(expected.get_requests(),actual.get_requests());
    }

    /**
     * Asserts two Performers to check they are the same.
     * @param expected The Performer that is arranged and defined.
     * @param actual The Performer that is the result of an action.
     */
    private void assertPerformers(Performer expected, Performer actual) {
        assertUsers(expected, actual);
        if (expected.get_fans().size() == actual.get_fans().size())
            for (int i = 1; i <= expected.get_fans().size(); i++)
                assertEquals(expected.get_fans().get(i).toString(),actual.get_fans().get(i).toString());
        else
            fail(notSameSizeMessage(new Object[][]{expected.get_fans().get_data(),actual.get_fans().get_data()}));
        if (expected.get_idols().size() == actual.get_idols().size())
            for (int i = 1; i <= expected.get_idols().size(); i++)
                assertEquals(expected.get_idols().get(i).toString(),actual.get_idols().get(i).toString());
        else
            fail(notSameSizeMessage(new Object[][]{expected.get_idols().get_data(),actual.get_idols().get_data()}));
    }

    /**
     * Asserts two Venues to check they are the same.
     * @param expected The Venue that is arranged and defined.
     * @param actual The Venue that is the result of an action.
     */
    protected void assertVenues(Venue expected, Venue actual, boolean assertNotNull) {
        if ((expected != null && actual != null) || assertNotNull) {
            assertUsers(expected, actual);
            assert expected != null && actual != null;
            assertEquals(expected.get_location(),actual.get_location());
            assertEquals(expected.get_gearDescription(),actual.get_gearDescription());
            assertEquals(expected.get_size(),actual.get_size());
            assertRequests(expected.get_requests(),actual.get_requests());
        }
    }

    /**
     * Asserts two Users to check they are the same.
     * @param expected The User that is arranged and defined.
     * @param actual The User that is the result of an action.
     */
    private void assertUsers(User expected, User actual) {
        if (expected != null && actual != null) {
            if (expected.get_primaryId() > 0)
                assertEquals(expected.get_primaryId(),actual.get_primaryId());
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
            //assertEquals(expected.get_timestamp(),actual.get_timestamp());
        }
    }

    /**
     * Asserts Albums to check they are the same.
     * @param expectations The Albums that are arranged and defined.
     * @param actuals The Albums that are the result of an action.
     */
    protected void assertAlbums(Liszt<Album> expectations, Liszt<Album> actuals) {
        if (expectations != null && actuals != null) {
            if (expectations.size() == actuals.size()) {
                for (int i = 1; i <= expectations.size(); i++) {
                    Album expected = expectations.get(i),
                            actual = actuals.get(i);
                    if (!expected.get_items().isEmpty() && !actual.get_items().isEmpty())
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
            } else fail(notSameSizeMessage(new Object[][]{expectations.get_data(),actuals.get_data()}));
        }
    }

    /**
     * Asserts Ratings to check they are the same.
     * @param expectations The Ratings that are arranged and defined.
     * @param actuals The Ratings that are the result of an action.
     */
    protected void assertRatings(Liszt<Rating> expectations, Liszt<Rating> actuals) {
        if (expectations != null && actuals != null) {
            if (expectations.size() == actuals.size()) {
                for (int i = 1; i <= expectations.size(); i++) {
                    Rating expected = expectations.get(i),
                            actual = actuals.get(i);
                    if (expected.get_appointed() != null && actual.get_appointed() != null)
                        assertEquals(expected.get_appointed().get_primaryId(),actual.get_appointed().get_primaryId());
                    if (expected.get_judge() != null && actual.get_judge() != null)
                        assertEquals(expected.get_judge().get_primaryId(),actual.get_judge().get_primaryId());
                    assertEquals(expected.get_value(),actual.get_value());
                    assertTrue((expected.get_value() <= 5 && expected.get_value() > 0)
                            && (actual.get_value() <= 5 && actual.get_value() > 0));
                    assertEquals(expected.get_comment(),actual.get_comment());
                }
            } else fail(notSameSizeMessage(new Object[][]{expectations.get_data(),actuals.get_data()}));
        }
    }

    /**
     * Asserts ChatRooms to check they are the same.
     * @param expectations The ChatRooms that are arranged and defined.
     * @param actuals The ChatRooms that are the result of an action.
     */
    public void assertChatRooms(Liszt<ChatRoom> expectations, Liszt<ChatRoom> actuals) {
        if (expectations != null && actuals != null) {
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
            } else fail(notSameSizeMessage(new Object[][]{expectations.get_data(),actuals.get_data()}));
        }
    }

    /**
     * Asserts Mails to check they are the same.
     * @param expectations The Mails that are arranged and defined.
     * @param actuals The Mails that are the result of an action.
     */
    protected void assertMails(Liszt<Mail> expectations, Liszt<Mail> actuals) {
        if (expectations.size() == actuals.size()) {
            for (int i = 1; i <= expectations.size(); i++) {
                Mail expected = expectations.get(i),
                        actual = actuals.get(i);
                assertEquals(expected.get_primaryId(),actual.get_primaryId());
                assertEquals(expected.get_chatRoom().toString(),actual.get_chatRoom().toString());
                assertEquals(expected.get_author().toString(),actual.get_author().toString());
                assertEquals(expected.get_content(),actual.get_content());
                assertEquals(expected.is_sent(),actual.is_sent());
                assertEquals(expected.get_edited().toString(),actual.get_edited().toString());
                assertEquals(expected.is_public(),actual.is_public());
                assertEquals(expected.get_timestamp(),actual.get_timestamp());
            }
        } else fail(notSameSizeMessage(new Object[][]{expectations.get_data(),actuals.get_data()}));
    }

    /**
     * Asserts two Subscriptions to check they are the same.
     * @param expected The Subscription that is arranged and defined.
     * @param actual The Subscription that is the result of an action.
     */
    public void asserting(Subscription expected, Subscription actual) {
        if (expected != null && actual != null) {
            assertEquals(expected.get_user().toString(),actual.get_user().toString());
            assertEquals(expected.get_type(),actual.get_type());
            assertEquals(expected.get_status(),actual.get_status());
            assertEquals(expected.get_price(),actual.get_price());
            if (expected.get_offer() != null) {
                if (actual.get_offer().get_expires() != null)
                    assertEquals(expected.get_offer().get_expires(),actual.get_offer().get_expires());
                if (actual.get_offer().get_type() != null)
                    assertEquals(expected.get_offer().get_type(),actual.get_offer().get_type());
                if (actual.get_offer().get_effect() > 0)
                    assertEquals(expected.get_offer().get_effect(),actual.get_offer().get_effect());
            }
            assertEquals(expected.get_cardId(),actual.get_cardId());
        }
    }

    /**
     * Asserts Requests to check they are the same.
     * @param expectations The Requests that are arranged and defined.
     * @param actuals The Requests that are the result of an action.
     */
    public void assertRequests(Liszt<Request> expectations, Liszt<Request> actuals) {
        if (expectations != null && actuals != null) {
            if (expectations.size() == actuals.size()) {
                for (int i = 1; i <= expectations.size(); i++) {
                    Request expected = expectations.get(i),
                            actual = actuals.get(i);
                    assertUsers(expected.get_user(),actual.get_user());
                    asserting(expected.get_event(),actual.get_event());
                    assertEquals(expected.get_approved().get_truth(),actual.get_approved().get_truth());
                    assertEquals(expected.get_message(),actual.get_message());
                }
            } else fail(notSameSizeMessage(new Object[][]{expectations.get_data(),actuals.get_data()}));
        }
    }
    /**
     * Asserts two ContactInfos to check they are the same.
     * @param expected The information that is arranged and defined.
     * @param actual The information that is the result of an action.
     */
    protected void asserting(ContactInfo expected, ContactInfo actual) {
        if (expected != null && actual != null) {
            assertEquals(expected.get_email(),actual.get_email());
            assertEquals(expected.get_country().get_title(),actual.get_country().get_title());
            assertEquals(expected.get_country().get_indexes(),actual.get_country().get_indexes());
            assertEquals(expected.get_phone().get_country().get_firstPhoneNumberDigits(),actual.get_phone().get_country().get_firstPhoneNumberDigits());
            assertEquals(expected.get_phone().get_numbers(),actual.get_phone().get_numbers());
            assertEquals(expected.get_phone().is_mobile(),actual.get_phone().is_mobile());
            assertEquals(expected.getAddressInfo(),actual.getAddressInfo());
        }
    }

    /**
     * Asserts Bulletins to check they are the same.
     * @param expectations The Bulletins that are arranged and defined.
     * @param actuals The Bulletins that are the result of an action.
     */
    protected void assertBulletins(Liszt<Bulletin> expectations, Liszt<Bulletin> actuals) {
        if (expectations != null && actuals != null) {
            if (expectations.size() == actuals.size()) {
                for (int i = 1; i <= expectations.size(); i++) {
                    Bulletin expected = expectations.get(i),
                            actual = actuals.get(i);
                    assertEquals(expected.get_primaryId(),actual.get_primaryId());
                    assertUsers(expected.get_author(), actual.get_author());
                    if (expected.get_author() != null && actual.get_author() != null)
                        assertUsers((User) expected.get_receiver(),(User) actual.get_receiver());
                    else if (expected.get_receiver() != null && actual.get_receiver() != null)
                        if (expected.get_receiver().getClass() == Event.class &&
                            actual.get_receiver().getClass() == Event.class)
                            asserting((Event) expected.get_receiver(),(Event) actual.get_receiver());
                    assertEquals(expected.get_content(),actual.get_content());
                    assertEquals(expected.is_sent(),actual.is_sent());
                    assertEquals(expected.get_edited().toString(),actual.get_edited().toString());
                    assertEquals(expected.is_public(),actual.is_public());
                }
            } else fail(notSameSizeMessage(new Object[][]{expectations.get_data(),actuals.get_data()}));
        }
    }

    /**
     * Asserts Events to check they are the same.
     * @param expectations The Events that are arranged and defined.
     * @param actuals The Events that are the result of an action.
     */
    private void assertEvents(Liszt<Event> expectations, Liszt<Event> actuals) {
        if (expectations != null && actuals != null)
            if (expectations.size() == actuals.size())
                for (int i = 1; i <= expectations.size(); i++)
                    asserting(expectations.get(i),actuals.get(i));
            else
                fail(notSameSizeMessage(new Object[][]{expectations.get_data(),actuals.get_data()}));
    }

    /**
     * Asserts two Events to check they are the same.
     * @param expected The Event that is arranged and defined.
     * @param actual The Event that is the result of an action.
     */
    protected void asserting(Event expected, Event actual) {
        if (expected.get_openDoors() != null && actual.get_openDoors() != null)
            assertTrue(expected.get_openDoors().isEqual(actual.get_openDoors()));
        if (expected.get_start() != null && actual.get_start() != null)
            assertTrue(expected.get_start().isEqual(actual.get_start()));
        if (expected.get_end() != null && actual.get_end() != null)
            assertTrue(expected.get_end().isEqual(actual.get_end()));
        assertEquals(expected.get_length(),expected.get_length());
        if (expected.get_voluntary() != null && actual.get_voluntary() != null)
            assertEquals(expected.get_voluntary().get_argument(),actual.get_voluntary().get_argument());
        if (expected.get_public() != null && actual.get_public() != null)
            assertEquals(expected.get_public().get_argument(),actual.get_public().get_argument());
        if (expected.get_cancelled() != null && actual.get_cancelled() != null)
            assertEquals(expected.get_cancelled().get_argument(),actual.get_cancelled().get_argument());
        if (expected.get_soldOut() != null && actual.get_soldOut() != null)
            assertEquals(expected.get_soldOut().get_argument(),actual.get_soldOut().get_argument());
        assertEquals(expected.get_location(), actual.get_location());
        assertEquals(expected.get_ticketsURL(), actual.get_ticketsURL());
        if (expected.get_contactInfo() != null && actual.get_contactInfo() != null)
            asserting(expected.get_contactInfo(), actual.get_contactInfo());
        assertGigs(expected.get_gigs(),actual.get_gigs());
        assertVenues(expected.get_venue(),actual.get_venue(),false);
        assertRequests(expected.get_requests(),actual.get_requests());
        assertParticipations(expected.get_participations(),actual.get_participations());
        assertAlbums(expected.get_albums(),actual.get_albums());
    }

    /**
     * Asserts Participations to check they are the same.
     * @param expectations The Participations that are arranged and defined.
     * @param actuals The Participations that are the result of an action.
     */
    protected void assertParticipations(Liszt<Participation> expectations, Liszt<Participation> actuals) {
        if (expectations != null && actuals != null) {
            if (expectations.size() == actuals.size())
                for (int i = 1; i <= expectations.size(); i++)
                    asserting(expectations.get(i),actuals.get(i));
            else
                fail(notSameSizeMessage(new Object[][]{expectations.get_data(),actuals.get_data()}));
        }
    }

    /**
     * Asserts two Participations to check they are the same.
     * @param expected The Participation that is arranged and defined.
     * @param actual The Participation that is the result of an action.
     */
    protected void asserting(Participation expected, Participation actual) {
        assertEquals(expected.toString(),actual.toString());
    }

    /**
     * Asserts Gigs to check they are the same.
     * @param expectations The Gigs that are arranged and defined.
     * @param actuals The Gigs that are the result of an action.
     */
    protected void assertGigs(Liszt<Gig> expectations, Liszt<Gig> actuals) {
        if (expectations != null && actuals != null) {
            if (expectations.size() == actuals.size())
                for (int i = 1; i <= expectations.size(); i++)
                    asserting(expectations.get(i),actuals.get(i));
            else
                fail(notSameSizeMessage(new Object[][]{expectations.get_data(),actuals.get_data()}));
        }
    }



    /**
     * Asserts two Gigs to check they are the same.
     * @param expected The Gig that is arranged and defined.
     * @param actual The Gig that is the result of an action.
     */
    protected void asserting(Gig expected, Gig actual) {
        asserting(expected.get_event(),actual.get_event());
        asserting(expected.get_act(),actual.get_act());
        assertEquals(expected.get_start(),actual.get_start());
        assertEquals(expected.get_end(),actual.get_end());
    }

    /**
     * Asserts two acts to check they are the same.
     * @param expectations The acts that is arranged and defined.
     * @param actuals The acts that is the result of an action.
     */
    protected void asserting(Performer[] expectations, Performer[] actuals) {
        if (expectations != null && actuals != null && expectations.length == actuals.length)
            for (int i = 0; i < expectations.length; i++)
                assertPerformers(expectations[i], actuals[i]);
    }

    /**
     * Is used to determine whether a statement is true.
     * Uses the assertTrue method.
     * @param isTrue A statement or value that must be true.
     */
    protected void asserting(boolean isTrue) {
        assertTrue(isTrue);
    }

    /**
     * A simple comparing assertEqual of two booleans.
     * @param expected The boolean that is arranged and defined.
     * @param actual The boolean that is the result of an action
     */
    protected void asserting(boolean expected, boolean actual) { assertEquals(expected,actual);}

    /**
     * A simple comparing assertEqual of two Objects.
     * @param expected The Object that is arranged and defined.
     * @param actual The Object that is the result of an action
     */
    protected void asserting(Object expected, Object actual) { assertEquals(expected,actual);}

    /**
     * A simple comparing assertEqual of two Strings.
     * @param expected The String that is arranged and defined.
     * @param actual The String that is the result of an action
     */
    protected void asserting(String expected, String actual) {
        assertEquals(expected,actual);
    }

    /**
     * Will assert the condition of whether this is null or not.
     * @param object The Object that will be asserted.
     */
    protected void notNull(Object object) {
        assertNotNull(object);
    }

    /**
     * Will make the assertion fail.
     * @param message The reason of the fail described.
     */
    protected void fail(String message) {
        Printer.get_instance().print(message, new Exception());
        fail(message);
    }

    /**
     * Will make the assertion fail.
     * @param message The reason of the fail described.
     * @param exception The exception caused.
     */
    protected void fail(String message, Exception exception) {
        Printer.get_instance().print(message, exception);
        fail(message);
    }

    /**
     * Will add the message to the print and assertion status will be a success.
     * @param message A description of the assertion.
     */
    protected void success(String message) {
        _print += "\n\n\t" + message + "\n";
        assertTrue(true);
    }
}
