package laustrup.bandwichpersistence.services.persistence_services.assembling_services;

import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Assembler {

    /**
     * The class that handles assembling logic of Assemblers.
     */
    protected AssemblyHandler _handler = new AssemblyHandler();

    protected User defineUserType(ResultSet set) throws SQLException {
        switch (set.getString("users.kind")) {
            case "BAND" -> {
                return new Band(set.getLong("users.id"));
            }
            case "ARTIST" -> {
                return new Artist(set.getLong("users.id"));
            }
            case "VENUE" -> {
                return new Venue(set.getLong("users.id"));
            }
            case "PARTICIPANT" -> {
                return new Participant(set.getLong("users.id"));
            }
            default -> {
                return null;
            }
        }
    }
}
