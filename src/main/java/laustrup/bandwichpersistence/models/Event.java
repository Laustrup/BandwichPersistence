package laustrup.bandwichpersistence.models;

import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Printer;
import lombok.*;

import javax.management.relation.InvalidRelationIdException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * An Event is placed a gig, where a venue is having bands playing at specific times.
 */
@NoArgsConstructor @ToString
public class Event extends Model {

    @Getter
    private LocalDateTime _beginning, _end;
    @Getter
    private double _length;
    @Getter @Setter
    private boolean _isVoluntary, _isPublic;

    @Getter @Setter
    private String _location;
    @Getter @Setter
    private Liszt<Gig> _gigs;
    @Getter @Setter
    private Venue _venue;

    @Getter
    private Liszt<Bulletin> _bulletins;

    public Event(long id, String title, boolean isVoluntary) {
        super(id, title);
        _isVoluntary = isVoluntary;
    }

    public Event(long id, String title, boolean isVoluntary, LocalDateTime beginning, LocalDateTime end, String location,
                 Venue venue) {
        super(id, title);
        _isVoluntary = isVoluntary;
        _beginning = beginning;
        _end = end;
        _location = location;
    }

    public Event(long id, String title, LocalDateTime beginning, LocalDateTime end, String location, LocalDateTime timeStamp) {
        super(id, title, timeStamp);
        _beginning = beginning;
        _end = end;
        _location = location;
    }

    /**
     * Adds the given gig to gigs of current event.
     * @param gig Determines a specific gig of one band for a specific time.
     * @return All the gigs of the current event.
     */
    public Liszt<Gig> addGig(Gig gig) {
        _gigs.add(gig);
        try {
            calculateTime();
        } catch (InputMismatchException e) {
            Printer.get_instance().print("End date is before beginning date of " + _title + "...", e);
            _gigs.remove(gig);
        }
        return _gigs;
    }

    //TODO Check if it formats time correctly
    /**
     * Sets the beginning and end of the event to match all the current gigs.
     * Important to use after each change of gigs.
     * @return The calculated length between first beginning and latest end.
     * @throws InputMismatchException In case that the end is before the beginning.
     */
    private double calculateTime() throws InputMismatchException {
        if (_end.isAfter(_beginning)) {
            _beginning = _gigs.get(1).get_beginning();
            _end = _gigs.get(1).get_end();

            if (_gigs.size() > 1)
                for (Gig gig : _gigs) {
                    if (gig._beginning.isBefore(_beginning)) _beginning = gig.get_beginning();
                    if (gig._end.isAfter(_end)) _end = gig.get_end();
                }

            return calculateLength();
        }
        throw new InputMismatchException();
    }

    /**
     * Is meant to be used after calculateTime().
     * Will determine the _length of the event.
     * Tries to do a simpleDateFormat, otherwise it will catch the ParseException
     * and _length will not change its value.
     * @return The length of the event as a Object typed of Double.
     * If the try is not caught, it will have the new length calculated, set and returned,
     * otherwise it will just return the standard _length from earlier.
     */
    private Double calculateLength() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        try {
            Date first = simpleDateFormat.parse(_beginning.toString());
            Date last = simpleDateFormat.parse(_end.toString());

            _length = ((last.getTime() - first.getTime()) / (1000 * 60 * 60)) % 24;
        } catch (ParseException e) { Printer.get_instance().print("Couldn't parse datetimes in event " + _title + "...",e); }

        return _length;
    }

    /**
     * Determines a specific gig of one band for a specific time.
     */
    @Data @ToString
    public class Gig {
        private Band _band;
        private LocalDateTime _beginning, _end;

        public Gig(Band band, LocalDateTime beginning, LocalDateTime end) {
            _band = band;
            _beginning = beginning;
            _end = end;
        }
    }
}
