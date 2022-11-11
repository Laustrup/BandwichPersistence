package laustrup.bandwichpersistence.models;

import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.MusicalUser;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.utilities.Liszt;
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
    private boolean _isVoluntary;

    /**
     * If this is a public Event, other Users can view and interact with it.
     */
    @Getter @Setter
    private boolean _isPublic;

    /**
     * This is marked if there is no more tickets to sell.
     */
    @Getter @Setter
    private boolean _isSoldOut;

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
    @Getter @Setter
    private Liszt<Gig> _gigs;

    /**
     * This venue is the ones responsible for the Event,
     * perhaps even the place it is held, but not necessarily.
     */
    @Getter @Setter
    private Venue _venue;

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
            _gigs.add(new Gig((MusicalUser) user, null, null));
        else _venue = (Venue) user;

        _participations = new Liszt<>();
        _bulletins = new Liszt<>();

        _length = 0;
    }

    public Event(long id, String title, LocalDateTime timestamp, LocalDateTime openDoors,
                 boolean isVoluntary, boolean isPublic, boolean isSoldOut, String location, double price,
                 String ticketsURL, ContactInfo contactInfo, Liszt<Gig> gigs, Venue venue,
                 Liszt<Participation> participations, Liszt<Bulletin> bulletins) throws InputMismatchException {
        super(id, title, timestamp);

        try {
            calculateTime();
        } catch (InputMismatchException e) {
            Printer.get_instance().print("End date is before beginning date of " + _title + "...", e);
        }

        if (Duration.between(openDoors, _start).toMinutes() >= 0)
            _openDoors = openDoors;
        else
            throw new InputMismatchException();

        _isVoluntary = isVoluntary;
        _isPublic = isPublic;
        _isSoldOut = isSoldOut;
        _price = price;
        _ticketsURL = ticketsURL;
        _contactInfo = contactInfo;
        _gigs = gigs;
        _venue = venue;

        if (location == null || location.isEmpty())
            _location = venue.get_location();
        else
            _location = location;

        _participations = participations;
        _bulletins = bulletins;
    }

    /**
     * Adds the given Gig to gigs of current Event.
     * @param gig Determines a specific Gig of one MusicalUser for a specific time.
     * @return All the Gigs of the current event.
     */
    public Liszt<Gig> addGig(Gig gig) { return addGigs(new Gig[]{gig}); }

    /**
     * Adds multiple given Gigs to the Liszt of Gigs in the current Event.
     * @param gigs Determines some specific Gigs of one MusicalUser for a specific time.
     * @return All the Gigs of the current event.
     */
    public Liszt<Gig> addGigs(Gig[] gigs) {
        _gigs.add(gigs);
        try {
            calculateTime();
        } catch (InputMismatchException e) {
            Printer.get_instance().print("End date is before beginning date of " + _title + "...", e);
            _gigs.remove(gigs);
        }
        return _gigs;
    }

    /**
     * Adds the given Participation to participations of current event.
     * @param participation Determines a specific participant, that is wished to be added.
     * @return All the Participations of the current event.
     */
    public Liszt<Participation> addParticipation(Participation participation) {
        return addParticpations(new Participation[]{participation});
    }

    /**
     * Adds some given Participations to the Liszt of participations from current event.
     * @param participations Determines some specific participants, that is wished to be added.
     * @return All the Participations of the current event.
     */
    public Liszt<Participation> addParticpations(Participation[] participations) {
        _participations.add(participations);
        return _participations;
    }

    /**
     * Removes the given Participation from participations of current event.
     * @param participation Determines a specific participant, that is wished to be removed.
     * @return All the Participations of the current event.
     */
    public Liszt<Participation> removeParticipation(Participation participation) {
        return removeParticpations(new Participation[]{participation});
    }

    /**
     * Removes some given Participations from the Liszt of participations from current event.
     * @param participations Determines some specific participants, that is wished to be removed.
     * @return All the Participations of the current event.
     */
    public Liszt<Participation> removeParticpations(Participation[] participations) {
        _participations.remove(participations);
        return _participations;
    }

    /**
     * Adds the given Bulletin to bulletins of current event.
     * @param bulletin Determines a specific bulletin, that is wished to be added.
     * @return All the bulletins of the current event.
     */
    public Liszt<Bulletin> addBulletin(Bulletin bulletin) {
        return addBulletins(new Bulletin[]{bulletin});
    }

    /**
     * Adds some given Bulletins to the Liszt of bulletins from current event.
     * @param bulletins Determines some specific bulletins, that is wished to be added.
     * @return All the bulletins of the current event.
     */
    public Liszt<Bulletin> addBulletins(Bulletin[] bulletins) {
        _bulletins.add(bulletins);
        return _bulletins;
    }

    /**
     * Removes the given Bulletin from bulletins of current event.
     * @param bulletin Determines a specific bulletin, that is wished to be removed.
     * @return All the bulletins of the current event.
     */
    public Liszt<Bulletin> removeBulletin(Bulletin bulletin) {
        return removeBulletins(new Bulletin[]{bulletin});
    }

    /**
     * Removes some given Bulletins from the Liszt of bulletins from current event.
     * @param bulletins Determines some specific bulletins, that is wished to be removed.
     * @return All the bulletins of the current event.
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
         * This act is of a Gig and can both be assigned as an artist or a band.
         */
        private MusicalUser _act;

        /**
         * The start of the Gig, where the act will begin.
         */
        private LocalDateTime _start;

        /**
         * The end of the Gig, where the act will end.
         */
        private LocalDateTime _end;

        public Gig(MusicalUser act, LocalDateTime start, LocalDateTime end) {
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
