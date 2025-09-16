package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.ARTIST;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.CHAT_ROOM;

public class ArtistJoins {

    public static final Join
            LEFT_ARTIST_TO_CONTACT_INFO = left(ARTIST, CHAT_ROOM);

    public static final Seszt<Join>
            LEFT_ARTIST_TO_CONTACT_INFO_FULL = Seszt.of(LEFT_ARTIST_TO_CONTACT_INFO);
}
