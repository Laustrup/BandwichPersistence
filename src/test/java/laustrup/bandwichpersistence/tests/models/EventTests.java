package laustrup.bandwichpersistence.tests.models;

import laustrup.bandwichpersistence.tests.Tester;

import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.services.TimeService;
import laustrup.bandwichpersistence.utilities.collections.Liszt;
import laustrup.bandwichpersistence.utilities.parameters.Plato;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class EventTests extends Tester<Object, Object> {

    /** The Event that will be used in the tests for testing. */
    private Event _event;

    /** Used to arrange Gigs in canAddGigs and determines if any Gigs are shared. */
    private boolean _hasCommonGigs;

    /** Used to arrange Gigs in canAddGigs and determines if any Gigs are shared. */
    private Liszt<Gig> _originals, _generatedGigs;

    @Test
    public void canAddGigs() {
        test(t -> {
            for (int i = 0; i < 10; i++) {
                Gig[] expected = (Gig[]) arrange(e -> {
                    _event = _items.get_events()[_random.nextInt(_items.get_eventAmount())];

                    _originals = _event.get_gigs();
                    _generatedGigs = _items.generateGigs(_event, TimeService.get_instance().generateRandom(),
                            _random.nextInt(11), _random.nextInt(45));

                    while (_generatedGigs==null) _generatedGigs = _items.generateGigs(_event, TimeService.get_instance().generateRandom(),
                            _random.nextInt(11), _random.nextInt(45));

                    _hasCommonGigs = false;
                    for (Gig original : _originals) {
                        for (Gig generated : _generatedGigs) {
                            for (Performer originalAct : original.get_act()) {
                                for (Performer generatedAct : generated.get_act()) {
                                    if (originalAct.get_primaryId() == generatedAct.get_primaryId() &&
                                            original.get_start().isEqual(generated.get_start()) &&
                                            original.get_end().isEqual(generated.get_end())) {
                                        _hasCommonGigs = true;
                                        break;
                                    }
                                }
                                if (_hasCommonGigs) break;
                            }
                            if (_hasCommonGigs) break;
                        }
                        if (_hasCommonGigs) break;
                    }

                    Gig[] gigs = new Gig[_generatedGigs.size()];
                    for (int j = 0; j < gigs.length; j++)
                        gigs[j] = _generatedGigs.get(j+1);

                    return gigs;
                });

                act(expected,e -> _event.add((Gig[]) e));

                if (_hasCommonGigs)
                    asserting(_event.get_gigs().size() == _originals.size() + _generatedGigs.size());

                for (Gig gig : _event.get_gigs())
                    asserting(_originals.contains(gig) || _generatedGigs.contains(gig));

                asserting(calculateEventLength(), _event.get_length());
                asserting(requestsFitsGigs());
            }

            return true;
        });
    }

    @Test
    public void canRemoveGigs() {
        test(t -> {
            Gig[] expectations = (Gig[]) arrange(e -> {
                _event = _items.get_events()[_random.nextInt(_items.get_eventAmount())];
                Gig[] gigsToRemove = new Gig[_event.get_gigs().size()-1];
                Set<Gig> gigSet = new HashSet<>();
                for (int j = 0; j < gigsToRemove.length; j++) {
                    Gig gig = _event.get_gigs().get(_random.nextInt(_event.get_gigs().size())+1);
                    while (gigSet.contains(gig)) gig = _event.get_gigs().get(_random.nextInt(_event.get_gigs().size())+1);
                    gigSet.add(gig);

                    gigsToRemove[j] = gig;
                }
                return gigsToRemove;
            });

            act(expectations, e -> _event.remove((Gig[]) e));

            asserting(calculateEventLength(), _event.get_length());
            asserting(requestsFitsGigs());

            return true;
        });
    }

    private long calculateEventLength() {
        Liszt<Gig> gigs = _event.get_gigs();

        LocalDateTime start = _event.get_gigs().get(1).get_start();
        LocalDateTime end = _event.get_gigs().get(1).get_end();

        for (Gig gig : gigs) {
            if (gig.get_start().isBefore(start)) start = gig.get_start();
            if (gig.get_end().isAfter(end)) end = gig.get_end();
        }

        return Duration.between(start, end).toMillis();
    }
    private boolean requestsFitsGigs() {
        Liszt<Request> requests = _event.get_requests();
        Liszt<Gig> gigs = _event.get_gigs();
        boolean performerHasRequest = false;

        for (Gig gig : gigs) {
            for (Request request : requests) {
                if (gig.get_act().length>0) {
                    for (Performer performer : gig.get_act()) {
                        if (request.get_user().get_primaryId() == performer.get_primaryId()) {
                            performerHasRequest = true;
                            break;
                        }
                    }
                    if (!performerHasRequest) return false;
                    performerHasRequest = false;
                }
            }
        }

        return true;
    }

    @Test
    public void canAcceptRequest() {
        test(t -> {
            Request expected = (Request) arrange(e -> {
                _event = _items.get_events()[_random.nextInt(_items.get_eventAmount())];
                Request request = null;
                _index = 0;

                do {
                    for (int i = 1; i <= _event.get_requests().size(); i++) {
                        if (!_event.get_requests().get(i).get_approved().get_truth()) {
                            request = _event.get_requests().get(i);
                            _index = i;
                        }
                    }

                    if (request == null)
                        _event = _items.get_events()[_random.nextInt(_items.get_eventAmount())];

                } while (request == null);

                return request;
            });

            act(expected, e -> _event.acceptRequest((Request) e));

            expected.set_approved(new Plato(true));
            asserting(_event.get_requests().get(_index).toString(), expected.toString());
            asserting(_event.get_requests().get(_index).get_approved().get_truth());

            return true;
        });

    }

    @Test
    public void canSetVenue() {
        test(t -> {
            for (int i = 0; i < 10; i++) {
                Venue expected = (Venue) arrange(e -> {
                    _event = _items.get_events()[_random.nextInt(_items.get_eventAmount())];
                    Venue venue = _event.get_venue(),
                            newVenue = null;

                    do {
                        newVenue = _items.get_venues()[_random.nextInt(_items.get_venueAmount())];
                    } while (newVenue.get_primaryId() != venue.get_primaryId());

                    return newVenue;
                });

                // ACT
                act(expected, e -> _event.set_venue((Venue) e));

                // ASSERT
                asserting(expected, _event.get_venue());
                asserting(!_event.get_public().get_truth());
                asserting(eventHasRequest(new Request(expected, _event, new Plato(Plato.Argument.UNDEFINED))));
            }

            return true;
        });

    }

    private boolean eventHasRequest(Request request) {
        boolean success = false;

        success = _event.get_requests().contains(request);
        if (!success)
            success = _event.get_requests().contains(request.toString());
        if (!success)
            for (Request eventRequest : _event.get_requests())
                if(eventRequest.get_primaryId() == request.get_primaryId() &&
                        Objects.equals(eventRequest.get_secondaryId(), request.get_secondaryId()))
                    success = true;

        return success;
    }
}
