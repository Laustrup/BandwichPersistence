package laustrup.bandwichpersistence.models;

import laustrup.bandwichpersistence.JTest;

import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.services.TimeService;
import laustrup.bandwichpersistence.utilities.Liszt;

import laustrup.bandwichpersistence.utilities.Printer;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

            while (generatedGigs==null) generatedGigs = _items.generateGigs(TimeService.get_instance().generateRandom(),
                    _random.nextInt(11), _random.nextInt(45));

            boolean bothGigCollectionSharesGigs = false;
            for (Gig original : originals) {
                for (Gig generated : generatedGigs) {
                    for (Performer originalAct : original.get_act()) {
                        for (Performer generatedAct : generated.get_act()) {
                            if (originalAct.get_primaryId() == generatedAct.get_primaryId() &&
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
    @Test
    public void canRemoveGigs() {
        // ARRANGE
        _event = _items.get_events()[_random.nextInt(_items.get_eventAmount())];
        Gig[] gigsToRemove = new Gig[_event.get_gigs().size()-1];
        Set<Gig> gigSet = new HashSet<>();
        for (int i = 0; i < gigsToRemove.length; i++) {
            Gig gig = _event.get_gigs().get(_random.nextInt(_event.get_gigs().size()));
            while (gigSet.contains(gig)) gig = _event.get_gigs().get(_random.nextInt(_event.get_gigs().size()));
            gigSet.add(gig);

            gigsToRemove[i] = gig;
        }

        // ACT
        begin();
        _event.remove(gigsToRemove);
        calculatePerformance();

        // ASSERT
        assertEquals(calculateEventLength(), _event.get_length());
        assertTrue(requestsFitsGigs());
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
                    if (request.get_user().get_primaryId() == performer.get_primaryId()) {
                        performerHasRequest = true;
                        break;
                    }
                }
                if (!performerHasRequest) {
                    Printer.get_instance().print(Arrays.toString(requests.toArray()));
                    return false;
                }
                performerHasRequest = false;
            }
        }

        return true;
    }
}
