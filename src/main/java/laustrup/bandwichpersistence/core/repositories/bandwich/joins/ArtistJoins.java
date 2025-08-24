package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.*;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.*;

public class ArtistJoins {

    public static final Join
            LEFT_ARTIST_TO_CONTACT_INFO = left(ARTIST.get_title(), Condition.equals(
                    Field.of(ARTIST, CONTACT_INFO.get_idReference()),
                    Field.of(CONTACT_INFO)
    ));
}
