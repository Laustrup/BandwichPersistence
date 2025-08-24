package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.*;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.BAND;

public class BandJoins {

    public static final Join
            LEFT_BAND_MEMBERSHIP_TO_ARTIST = left(BAND_MEMBERSHIP.get_title(), Condition.equals(
                    Field.of(BAND_MEMBERSHIP, ARTIST.get_idReference()),
                    Field.of(ARTIST)
    )), LEFT_BAND_TO_BAND_MEMBERSHIP = left(BAND.get_title(), Condition.equals(
            Field.of(BAND_MEMBERSHIP, BAND.get_idReference()),
            Field.of(BAND)
    ));
}
