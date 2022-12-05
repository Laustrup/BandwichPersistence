package laustrup.bandwichpersistence.services.persistence_services.assembling_services;

import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.logic_assemblings.AssemblyDescriber;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.logic_assemblings.AssemblyHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An abstract class that includes logic classes and common assembling methods.
 */
public abstract class Assembler {

    /**
     * The logic class that handles assembling logic of Assemblers.
     */
    protected AssemblyHandler _handler = new AssemblyHandler();

    /**
     * The logic class that will describe values of models with only ids.
     */
    protected AssemblyDescriber _describer = new AssemblyDescriber();

    /**
     * Will define a User with only and id, where it's class kind is decided from ResultSet users.kind.
     * @param set A ResultSet from the database with specified values.
     * @return A User with an id and described class.
     * @throws SQLException Will be thrown if the ResultsSet makes an Exception of a SQL reason.
     */
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
