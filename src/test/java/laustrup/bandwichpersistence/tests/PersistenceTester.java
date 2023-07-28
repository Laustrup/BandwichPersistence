package laustrup.bandwichpersistence.tests;

import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.models.users.sub_users.bands.Artist;
import laustrup.models.users.sub_users.bands.Band;
import laustrup.models.users.sub_users.participants.Participant;
import laustrup.models.users.sub_users.venues.Venue;
import laustrup.quality_assurance.Tester;

import lombok.Getter;

public class PersistenceTester<T> extends Tester<T> {

    /**
     * An Artist gathered from the database of default values with all its values assigned to it.
     */
    @Getter
    private final Artist _carlos = (Artist) Assembly.get_instance().getUser(1),
            _bjarke = (Artist) Assembly.get_instance().getUser(2),
            _tir = (Artist) Assembly.get_instance().getUser(3),
            _laust = (Artist) Assembly.get_instance().getUser(4);
    /**
     * A Band gathered from the database of default values with all its values assigned to it.
     */
    @Getter
    private final Band _melanges = (Band) Assembly.get_instance().getUser(5);

    /**
     * A Venue gathered from the database of default values with all its values assigned to it.
     */
    @Getter
    private final Venue _metronomen = (Venue) Assembly.get_instance().getUser(6),
            _roskilde = (Venue) Assembly.get_instance().getUser(7);

    /**
     * A Participant gathered from the database of default values with all its values assigned to it.
     */
    @Getter
    private final Participant _rockGuy = (Participant) Assembly.get_instance().getUser(8),
            _beautiQueen = (Participant) Assembly.get_instance().getUser(9),
            _artsy = (Participant) Assembly.get_instance().getUser(10);
}
