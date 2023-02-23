package laustrup.bandwichpersistence.crud;

import laustrup.bandwichpersistence.Tester;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.events.Participation;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services.EventPersistenceService;
import laustrup.bandwichpersistence.utilities.Liszt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class EventCRUDTests extends Tester {

    @Test
    void canCRUDEvent() {
        //ARRANGE
        Event expected = _items.get_events()[_random.nextInt(_items.get_eventAmount())];

        //ACT
        begin();
        Event actual = EventPersistenceService.get_instance().create(expected);
        calculatePerformance("creating event");

        //ASSERT
        asserting(expected,actual);

        readUpdateDelete(expected,actual);
    }

    @Test
    void canUpsertBulletin() {
        //ARRANGE
        Bulletin expected = _items.generateBulletins(Assembly.get_instance().getEvent(0))[0];

        //ACT
        begin();
        Event event = EventPersistenceService.get_instance().upsert(expected);
        calculatePerformance("upsert bulletin");

        //ASSERT
        assertBulletins(new Liszt<>(new Bulletin[]{expected}), new Liszt<>(new Bulletin[]{event.get_bulletins().getLast()}));
    }

    @ParameterizedTest
    @CsvSource(value = {"ACCEPTED","CANCELLED","IN_DOUBT","INVITED"})
    void canUpsertParticipations(Participation.ParticipationType type) {
        //ARRANGE
        Participation expected = new Participation(
                (Participant) Assembly.get_instance().getUser(1),
                Assembly.get_instance().getEvent(1),
                type);

        //ACT
        begin();
        Event event = EventPersistenceService.get_instance().upsert(new Liszt<>(new Participation[]{expected}));
        calculatePerformance("upsert participation");

        //ASSERT
        asserting(expected, event.get_participations().getLast());
    }

    private void readUpdateDelete(Event expected, Event actual) {
        if (actual != null) {
            expected = actual;

            //ACT
            actual = read(expected);

            //ASSERT
            asserting(expected, actual);

            if (actual != null) {

                    // ACT
                    expected.set_description("This is a new description");
                    actual = update(expected);

                    // ASSERT
                    asserting(expected, actual);
            } else fail();
        } else fail();

        // ACT and ASSERT
        assertTrue(delete(actual == null ? expected : actual));
    }

    @ParameterizedTest
    @CsvSource(value = {"1","2"})
    void canAssembleEvent(long id) {
        //ACT
        begin();
        Event actual = Assembly.get_instance().getEvent(id);
        calculatePerformance();

        //ASSERT
        assertNotNull(actual);
    }

    private Event update(Event event) {
        begin();
        event = EventPersistenceService.get_instance().update(event);
        calculatePerformance("update event");
        return event;
    }

    private Event read(Event event) {
        begin();
        event = Assembly.get_instance().getEvent(event.get_primaryId());
        calculatePerformance("read event");
        return event;
    }

    private boolean delete(Event event) {
        begin();
        boolean success = EventPersistenceService.get_instance().delete(event).get_truth();
        calculatePerformance("update event");
        return success;
    }
}
