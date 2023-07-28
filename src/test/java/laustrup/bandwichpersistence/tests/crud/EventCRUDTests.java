package laustrup.bandwichpersistence.tests.crud;

import laustrup.bandwichpersistence.tests.PersistenceTester;
import laustrup.models.chats.messages.Bulletin;
import laustrup.models.events.Event;
import laustrup.models.events.Participation;
import laustrup.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services.EventPersistenceService;
import laustrup.utilities.collections.lists.Liszt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static laustrup.quality_assurance.inheritances.aaa.assertions.AssertionFailer.failing;

public class EventCRUDTests extends PersistenceTester<Object> {

    @Test
    void canCRUDEvent() {
        test(() -> {
            Event expected = (Event) arrange(() -> _items.get_events()[_random.nextInt(_items.get_eventAmount())]);
            Event actual = (Event) act(expected, e -> EventPersistenceService.get_instance().create((Event) e),"creating event");
            asserting(expected,actual);
            readUpdateDelete(expected,actual);
        });

    }

    @Test
    void canUpsertBulletin() {
        test(() -> {
            Bulletin expected = (Bulletin) arrange(() -> _items.generateBulletins(Assembly.get_instance().getEvent(0))[0]);
            Event actual = (Event) act(expected, e -> EventPersistenceService.get_instance().upsert((Bulletin) e),"upsert bulletin");
            assertBulletins(new Liszt<>(new Bulletin[]{expected}), new Liszt<>(new Bulletin[]{actual.get_bulletins().getLast()}));
        });

    }

    @ParameterizedTest
    @CsvSource(value = {"ACCEPTED","CANCELLED","IN_DOUBT","INVITED"})
    void canUpsertParticipations(Participation.ParticipationType type) {
        test(() -> {
            Participation expected = (Participation) arrange(() -> new Participation(
                    (Participant) Assembly.get_instance().getUser(1),
                    Assembly.get_instance().getEvent(1),
                    type));

            Event event = (Event) act(() -> EventPersistenceService.get_instance().upsert(new Liszt<>(new Participation[]{expected})),
                    "upsert participation");

            asserting(expected, event.get_participations().getLast());
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
        test(() -> {
            arrange();

            notNull(act(() -> Assembly.get_instance().getEvent(id)));
        });
    }

    private Event update(Event event) {
        return (Event) act(event, e -> EventPersistenceService.get_instance().update((Event) e),"update event");
    }

    private Event read(Event event) {
        return (Event) act(event, e -> Assembly.get_instance().getEvent(((Event) e).get_primaryId()),"read event");
    }

    private boolean delete(Event event) {
        return (boolean) act(() -> EventPersistenceService.get_instance().delete(event).get_truth(), "update event");
    }
}
