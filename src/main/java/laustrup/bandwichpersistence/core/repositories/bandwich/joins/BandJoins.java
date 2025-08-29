package laustrup.bandwichpersistence.core.repositories.bandwich.joins;

import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Condition;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import static laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Join.left;
import static laustrup.bandwichpersistence.core.repositories.bandwich.BandwichCommonQueries.BandwichDatabasePropertiesCollection.*;

public class BandJoins {

    public static final Join
            LEFT_BAND_MEMBERSHIP_TO_ARTIST = left(BAND_MEMBERSHIP.get_title(), Condition.equals(
                    DatabaseField.of(BAND_MEMBERSHIP, ARTIST.get_idReference()),
                    DatabaseField.of(ARTIST)
    )), LEFT_BAND_TO_BAND_MEMBERSHIP = left(BAND.get_title(), Condition.equals(
            DatabaseField.of(BAND_MEMBERSHIP, BAND.get_idReference()),
            DatabaseField.of(BAND)
    ));

    public static final Seszt<Join>
            LEFT_BAND_TO_BAND_MEMBERSHIP_FULL = Seszt.of(
                    LEFT_BAND_MEMBERSHIP_TO_ARTIST,
                    LEFT_BAND_TO_BAND_MEMBERSHIP
            );
}
