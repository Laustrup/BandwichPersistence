package laustrup.bandwichpersistence.models;

import laustrup.bandwichpersistence.JTest;

import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.services.TimeService;
import laustrup.bandwichpersistence.utilities.Liszt;

import laustrup.bandwichpersistence.utilities.Printer;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest extends JTest {

    private Event _event;

    @Test
    public void canAddGigs() {
        for (int i = 0; i < 10; i++) {
            // ARRANGE
            _event = _items.get_events()[_random.nextInt(_items.get_eventAmount())];

            Liszt<Gig> originals = _event.get_gigs();
            Liszt<Gig> generatedGigs = _items.generateGigs(TimeService.get_instance().generateRandom(),
                    _random.nextInt(11), _random.nextInt(45));

            boolean bothGigCollectionSharesGigs = false;
            for (Gig original : originals) {
                for (Gig generated : generatedGigs) {
                    for (Performer originalAct : original.get_act()) {
                        for (Performer generatedAct : generated.get_act()) {
                            if (originalAct.get_id() == generatedAct.get_id() &&
                                original.get_start().isEqual(generated.get_start()) &&
                                original.get_end().isEqual(generated.get_end())) {
                                bothGigCollectionSharesGigs = true;
                                break;
                            }
                        }
                        if (bothGigCollectionSharesGigs) break;
                    }
                    if (bothGigCollectionSharesGigs) break;
                }
                if (bothGigCollectionSharesGigs) break;
            }

            Gig[] gigs = new Gig[generatedGigs.size()];
            for (int j = 0; j < gigs.length; j++) gigs[j] = generatedGigs.get(j+1);

            // ACT
            begin();
            _event.add(gigs);
            calculatePerformance();

            // ASSERT
            if (bothGigCollectionSharesGigs)
                assertFalse(_event.get_gigs().size() != originals.size() + generatedGigs.size());

            for (Gig gig : _event.get_gigs())
                assertTrue(originals.contains(gig) || generatedGigs.contains(gig));

            assertEquals(calculateEventLength(), _event.get_length());
            assertTrue(requestsFitsGigs());
        }
    }
    private long calculateEventLength() {
        long length = 0;

        Liszt<Gig> gigs = _event.get_gigs();

        LocalDateTime start = _event.get_gigs().get(1).get_start();
        LocalDateTime end = _event.get_gigs().get(1).get_end();

        for (Gig gig : gigs) {
            if (gig.get_start().isBefore(start)) start = gig.get_start();
            if (gig.get_end().isAfter(end)) end = gig.get_end();
        }

        length = Duration.between(start, end).toMillis();

        return length;
    }
    private boolean requestsFitsGigs() {
        Liszt<Request> requests = _event.get_requests();
        Liszt<Gig> gigs = _event.get_gigs();
        boolean performerHasRequest = false;

        for (Gig gig : gigs) {
            for (Request request : requests) {
                for (Performer performer : gig.get_act()) {
                    if (request.get_user().get_id() == performer.get_id()) {
                        performerHasRequest = true;
                        break;
                    }
                }
                if (!performerHasRequest) return false;
                performerHasRequest = false;
            }
        }

        return true;
    }

    @Test
    public void canRemoveGigs() {

    }
}
