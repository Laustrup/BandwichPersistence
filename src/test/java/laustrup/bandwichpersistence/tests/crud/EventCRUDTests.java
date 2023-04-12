package laustrup.bandwichpersistence.tests.crud;

import laustrup.bandwichpersistence.tests.Tester;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.events.Participation;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services.EventPersistenceService;
import laustrup.bandwichpersistence.utilities.collections.lists.Liszt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static laustrup.bandwichpersistence.items.aaa.assertions.AssertionFailer.failing;

public class EventCRUDTests extends Tester<Object, Object> {

    @Test
    void canCRUDEvent() {
        test(t -> {
            Event expected = (Event) arrange(e -> _items.get_events()[_random.nextInt(_items.get_eventAmount())]);
            Event actual = (Event) act(expected, e -> EventPersistenceService.get_instance().create((Event) e),"creating event");
            asserting(expected,actual);
            readUpdateDelete(expected,actual);

            return true;
        });

    }

    @Test
    void canUpsertBulletin() {
        test(t -> {
            Bulletin expected = (Bulletin) arrange(e -> _items.generateBulletins(Assembly.get_instance().getEvent(0))[0]);
            Event actual = (Event) act(expected, e -> EventPersistenceService.get_instance().upsert((Bulletin) e),"upsert bulletin");
            assertBulletins(new Liszt<>(new Bulletin[]{expected}), new Liszt<>(new Bulletin[]{actual.get_bulletins().getLast()}));

            return true;
        });

    }

    @ParameterizedTest
    @CsvSource(value = {"ACCEPTED","CANCELLED","IN_DOUBT","INVITED"})
    void canUpsertParticipations(Participation.ParticipationType type) {
        test(t -> {
            Participation expected = (Participation) arrange(e -> new Participation(
                    (Participant) Assembly.get_instance().getUser(1),
                    Assembly.get_instance().getEvent(1),
                    type));

            Event event = (Event) act(e -> EventPersistenceService.get_instance().upsert(new Liszt<>(new Participation[]{expected})),
                    "upsert participation");

            asserting(expected, event.get_participations().getLast());

            return true;
        });

    }

    private void readUpdateDelete(Event expected, Event actual) {
        if (actual != null) {
            expected = actual;
            actual = read(expected);
            asserting(expected, actual);

            if (actual != null) {
                expected.set_description("This is a new description");
                actual = update(expected);
                asserting(expected, actual);
            } else failing(isNullMessage(actual));
        } else failing(isNullMessage(actual));

        asserting(delete(actual == null ? expected : actual));
    }

    @ParameterizedTest
    @CsvSource(value = {"1","2"})
    void canAssembleEvent(long id) {
        test(t -> {
            arrange();

            notNull(act(e -> Assembly.get_instance().getEvent(id)));

            return true;
        });
    }

    private Event update(Event event) {
        return (Event) act(event, e -> EventPersistenceService.get_instance().update((Event) e),"update event");
    }

    private Event read(Event event) {
        return (Event) act(event, e -> Assembly.get_instance().getEvent(((Event) e).get_primaryId()),"read event");
    }

    private boolean delete(Event event) {
        return (boolean) act(e -> EventPersistenceService.get_instance().delete(event).get_truth(), "update event");
    }
}
