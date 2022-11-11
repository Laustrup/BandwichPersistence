package laustrup.bandwichpersistence.models;

import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.MusicalUser;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Plato;
import laustrup.bandwichpersistence.utilities.Printer;

import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * An Event is placed a gig, where a venue is having bands playing at specific times.
 */
@NoArgsConstructor @ToString
public class Event extends Model {

    /**
     * Is when the participants can enter the event,
     * not necessarily the same time as when the gigs start.
     * It must be before or same time as the gigs start.
     */
    @Getter
    private LocalDateTime _openDoors;

    /**
     * This DateTime is determining when the first gig will start.
     * Is being calculated automatically.
     */
    @Getter
    private LocalDateTime _start;

    /**
     * This DateTime is determining when the last gig will end.
     * Is being calculated automatically.
     */
    @Getter
    private LocalDateTime _end;

    /**
     * The amount of time it will take the gigs in total.
     * Is being calculated automatically.
     */
    @Getter
    private long _length;

    /**
     * This Event is paid or voluntary.
     */
    @Getter @Setter
    private Plato _voluntary;

    /**
     * If this is a public Event, other Users can view and interact with it.
     */
    @Getter
    private Plato _public;

    /**
     * This is marked if there is no more tickets to sell.
     */
    @Getter @Setter
    private Plato _soldOut;

    /**
     * This is the address or place, whether the Event will be held.
     * Will be used to be search at in Google Maps.
     */
    @Getter @Setter
    private String _location;

    /**
     * The cost of a ticket.
     * Can be multiple prices for tickets, but this is just meant as the cheapest.
     */
    @Getter @Setter
    private double _price;

    /**
     * A link for where the tickets can be sold.
     * This might be altered later on,
     * since it is wished to be sold through Bandwich.
     * If the field isn't touched, it will use the Venue's location.
     */
    @Getter @Setter
    private String _ticketsURL;

    /**
     * Different information of contacting.
     */
    @Getter
    private ContactInfo _contactInfo;

    /**
     * The gigs with times and acts of the Event.
     */
    @Getter
    private Liszt<Gig> _gigs;

    /**
     * This venue is the ones responsible for the Event,
     * perhaps even the place it is held, but not necessarily.
     */
    @Getter
    private Venue _venue;

    /**
     * These requests are needed to make sure, everyone wants to be a part of the Event.
     */
    @Getter
    private Liszt<Request> _requests;

    /**
     * The people that will participate in the Event,
     * not including venues or acts.
     */
    @Getter
    private Liszt<Participation> _participations;

    /**
     * Post from different people, that will mention contents.
     */
    @Getter
    private Liszt<Bulletin> _bulletins;

    public Event(String title, User user) {
        super(title);

        _gigs = new Liszt<>();
        if (user.getClass() == MusicalUser.class)
            _gigs.add(new Gig(new MusicalUser[]{(MusicalUser) user}, null, null));
        else _venue = (Venue) user;

        _participations = new Liszt<>();
        _bulletins = new Liszt<>();

        _length = 0;
    }

    public Event(long id, String title, LocalDateTime timestamp, LocalDateTime openDoors,
                 Plato isVoluntary, Plato isPublic, Plato isSoldOut, String location, double price,
                 String ticketsURL, ContactInfo contactInfo, Liszt<Gig> gigs, Venue venue, Liszt<Request> requests,
                 Liszt<Participation> participations, Liszt<Bulletin> bulletins) throws InputMismatchException {
        super(id, title, timestamp);

        _gigs = gigs;

        try {
            calculateTime();
        } catch (InputMismatchException e) {
            Printer.get_instance().print("End date is before beginning date of " + _title + "...", e);
        }

        if (Duration.between(openDoors, _start).toMinutes() >= 0)
            _openDoors = openDoors;
        else
            throw new InputMismatchException();

        _voluntary = isVoluntary;
        _public = isPublic;
        _soldOut = isSoldOut;
        _price = price;
        _ticketsURL = ticketsURL;
        _contactInfo = contactInfo;
        _venue = venue;

        if (location == null || location.isEmpty())
            _location = venue.get_location();
        else
            _location = location;

        _requests = requests;
        _participations = participations;
        _bulletins = bulletins;
    }

    /**
     * Adds the given Gig to gigs of current Event.
     * @param gig Determines a specific Gig of one MusicalUser for a specific time.
     * @return All the Gigs of the current Event.
     */
    public Liszt<Gig> addGig(Gig gig) { return addGigs(new Gig[]{gig}); }

    /**
     * Adds multiple given Gigs to the Liszt of Gigs in the current Event.
     * @param gigs Determines some specific Gigs of one MusicalUser for a specific time.
     * @return All the Gigs of the current Event.
     */
    public Liszt<Gig> addGigs(Gig[] gigs) {
        _gigs.add(gigs);
        addRequests(createRequests(gigs));

        try {
            calculateTime();
        } catch (InputMismatchException e) {
            Printer.get_instance().print("End date is before beginning date of " + _title + "...", e);
            _gigs.remove(gigs);
        }
        return _gigs;
    }

    /**
     * Removes the given Gig from gigs of current Event.
     * @param gig Determines a specific gig, that is wished to be removed.
     * @return All the gigs of the current Event.
     */
    public Liszt<Gig> removeGig(Gig gig) { return removeGigs(new Gig[]{gig}); }

    /**
     * Removes some given Gigs from the Liszt of gigs from current Event.
     * Also removes the Requests of the given Gigs.
     * @param gigs Determines some specific gigs, that is wished to be removed.
     * @return All the Gigs of the current Event.
     */
    public Liszt<Gig> removeGigs(Gig[] gigs) {
        _gigs.remove(gigs);
        for (Gig gig : gigs) { _requests.remove(gig); }

        return _gigs;
    }

    /**
     * Adds the given Request to requests of current Event.
     * @param request Determines a specific Request, that is wished to be added.
     * @return All the Requests of the current Event.
     */
    private Liszt<Request> addRequest(Request request) { return addRequests(new Request[]{request}); }

    /**
     * Adds some given Requests to the Liszt of requests from current Event.
     * @param requests Determines some specific requests, that is wished to be added.
     * @return All the requests of the current Event.
     */
    private Liszt<Request> addRequests(Request[] requests) {
        _requests.add(requests);
        return _requests;
    }

    /**
     * Creates some requests for the gigs that are about to be created to this Event.
     * Must be done after add of gigs.
     * @param gigs The gigs that are about to be created a Request for the current gig.
     * @return All the requests of this Event.
     */
    private Request[] createRequests(Gig[] gigs) {
        Request[] requests = new Request[gigs.length];
        boolean shouldBeApproved = _public.get_truth() && venueHasApproved();

        for (int i = 0; i < requests.length; i++) {
            if (shouldBeApproved) {
                for (User user : gigs[i]._act)
                    requests[i] = new Request(user,this, new Plato(true));
            } else {
                for (User user : gigs[i]._act)
                    requests[i] = new Request(user,this, new Plato(Plato.Argument.UNDEFINED));
            }
        }

        return requests;
    }

    public Request acceptRequest(Request request) {
        _requests.get(request.toString()).approve();
        return _requests.get(request.toString());
    }

    /**
     * Checks if the Venue has approved the Request.
     * @return True if the venue has approved.
     */
    public boolean venueHasApproved() {
        for (Request request : _requests) {
            if (request.get_user().getClass() == Venue.class && request.get_approved().get_truth())
                return true;
        }
        return false;
    }

    /**
     * Sets the Venue and also replaces the request of former Venue to a new request.
     * This means that the Event will become private instead of public, since the
     * Venue needs to approve the Event, in order to host it.
     * @param venue The Venue, that is wished to be set, as the Events new Venue.
     * @return The Venue that is set of the Event.
     */
    public Venue set_venue(Venue venue) {
        for (Request request : _requests) {
            if (request.get_user().get_id() == _venue.get_id())
                _requests.remove(request);
        }

        _requests.add(new Request(venue, this, new Plato(Plato.Argument.UNDEFINED)));
        _public.set_argument(true);
        _venue = venue;

        return _venue;
    }

    /**
     * Adds the given Participation to participations of current Event.
     * @param participation Determines a specific participant, that is wished to be added.
     * @return All the Participations of the current Event.
     */
    public Liszt<Participation> addParticipation(Participation participation) {
        return addParticpations(new Participation[]{participation});
    }

    /**
     * Adds some given Participations to the Liszt of participations from current Event.
     * @param participations Determines some specific participants, that is wished to be added.
     * @return All the Participations of the current Event.
     */
    public Liszt<Participation> addParticpations(Participation[] participations) {
        _participations.add(participations);
        return _participations;
    }

    /**
     * Removes the given Participation from participations of current Event.
     * @param participation Determines a specific participant, that is wished to be removed.
     * @return All the Participations of the current Event.
     */
    public Liszt<Participation> removeParticipation(Participation participation) {
        return removeParticpations(new Participation[]{participation});
    }

    /**
     * Removes some given Participations from the Liszt of participations from current Event.
     * @param participations Determines some specific participants, that is wished to be removed.
     * @return All the Participations of the current Event.
     */
    public Liszt<Participation> removeParticpations(Participation[] participations) {
        _participations.remove(participations);
        return _participations;
    }

    /**
     * Adds the given Bulletin to bulletins of current Event.
     * @param bulletin Determines a specific bulletin, that is wished to be added.
     * @return All the bulletins of the current Event.
     */
    public Liszt<Bulletin> addBulletin(Bulletin bulletin) {
        return addBulletins(new Bulletin[]{bulletin});
    }

    /**
     * Adds some given Bulletins to the Liszt of bulletins from current Event.
     * @param bulletins Determines some specific bulletins, that is wished to be added.
     * @return All the bulletins of the current Event.
     */
    public Liszt<Bulletin> addBulletins(Bulletin[] bulletins) {
        _bulletins.add(bulletins);
        return _bulletins;
    }

    /**
     * Removes the given Bulletin from bulletins of current Event.
     * @param bulletin Determines a specific bulletin, that is wished to be removed.
     * @return All the bulletins of the current Event.
     */
    public Liszt<Bulletin> removeBulletin(Bulletin bulletin) {
        return removeBulletins(new Bulletin[]{bulletin});
    }

    /**
     * Removes some given Bulletins from the Liszt of bulletins from current Event.
     * @param bulletins Determines some specific bulletins, that is wished to be removed.
     * @return All the bulletins of the current Event.
     */
    public Liszt<Bulletin> removeBulletins(Bulletin[] bulletins) {
        _bulletins.remove(bulletins);
        return _bulletins;
    }

    /**
     * Sets the beginning and end of the event to match all the current gigs.
     * Important to use after each change of gigs.
     * @return The calculated length between first beginning and latest end.
     * @throws InputMismatchException In case that the end is before the beginning.
     */
    private long calculateTime() throws InputMismatchException {
        if (_end.isAfter(_start)) {
            _start = _gigs.get(1).get_start();
            _end = _gigs.get(1).get_end();

            if (_gigs.size() > 1)
                for (Gig gig : _gigs) {
                    if (gig._start.isBefore(_start)) _start = gig.get_start();
                    if (gig._end.isAfter(_end)) _end = gig.get_end();
                }

            _length = Duration.between(_start, _end).toMillis();
            return _length;
        }
        throw new InputMismatchException();
    }

    /**
     * Determines a specific gig of one band for a specific time.
     */
    @Data @ToString
    public class Gig {
        /**
         * This act is of a Gig and can both be assigned as artists or bands.
         */
        private MusicalUser[] _act;

        /**
         * The start of the Gig, where the act will begin.
         */
        private LocalDateTime _start;

        /**
         * The end of the Gig, where the act will end.
         */
        private LocalDateTime _end;

        public Gig(MusicalUser[] act, LocalDateTime start, LocalDateTime end) {
            _act = act;
            _start = start;
            _end = end;
        }
    }

    /**
     * Determines type of which a Participant is participating in an Event.
     */
    @Data @ToString
    public class Participation {
        /**
         * The Participant of the participation.
         */
        private Participant _participant;
        /**
         * The type of which participant is participating in the participation.
         */
        private ParticipationType _type;

        public Participation(Participant participant, ParticipationType type) {
            _participant = participant;
            _type = type;
        }

        /**
         * Each Participation have four different choices of participating.
         */
        public enum ParticipationType { ACCEPTED, IN_DOUBT, CANCELED, INVITED }
    }
}
