package laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings;

import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.events.Participation;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.repositories.sub_repositories.EventRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembler;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Plato;
import laustrup.bandwichpersistence.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class EventAssembly extends Assembler {

    /**
     * Singleton instance of the Service.
     */
    private static EventAssembly _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static EventAssembly get_instance() {
        if (_instance == null) _instance = new EventAssembly();
        return _instance;
    }

    private EventAssembly() {}

    /**
     * Builds an Event object with the informations given from the EventRepository.
     * Will be initiated as objects with primitive amounts of attributes.
     * @param id The id of the Event that is wished to be assembled.
     * @return The assembled Event.
     */
    public Event assemble(long id) { return assemble(EventRepository.get_instance().get(id),true); }

    /**
     * Builds all Event objects with the informations given from the EventRepository.
     * Will be initiated as objects with primitive amounts of attributes.
     * @return All the assembled Events.
     */
    public Liszt<Event> assembles() {
        return assembles(EventRepository.get_instance().get());
    }

    /**
     * Builds Event objects that are alike the search query, where the informations are given from the EventRepository.
     * Will be initiated as objects with primitive amounts of attributes.
     * @param searchQuery The search that should have something in common with some Events.
     * @return The assembled Events similar to the search query.
     */
    public Liszt<Event> assembles(String searchQuery) {
        return assembles(EventRepository.get_instance().search(searchQuery));
    }

    /**
     * The private method that assembles multiple Events, that are defined in other public methods.
     * @param set The ResultSet that will define the values for the Events.
     * @return The assembled Events.
     */
    public Liszt<Event> assembles(ResultSet set) {
        Liszt<Event> events = new Liszt<>();

        try {
            while (!set.isAfterLast()) {
                if (set.isBeforeFirst())
                    set.next();
                events.add(assemble(set, false));
            }
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't assemble Events...", e);
        }

        return events;
    }

    /**
     * Assembles the Event from a ResultSet.
     * Will not close the Connection to the database, when it is done assembling.
     * @param set A JDBC ResultSet that is gathered from the rows of the SQL statement to the database.
     * @param preInitiate If true, it will set the ResultsSet at first row, should only be done,
     *                   if there is only expected a single entity.
     * @return The assembled Event.
     */
    public Event assemble(ResultSet set, boolean preInitiate) {
        Event event = null;

        try {
            if (preInitiate)
                set.next();
            event = assemble(set);
        } catch (SQLException e) {
            Printer.get_instance().print("Trouble assembling user...", e);
        }

        return event;
    }

    public Event assemble(ResultSet set) throws SQLException {
        long id = set.getLong("`events`.id");
        String title = set.getString("`events`.title"),
            description = set.getString("`events`.`description`");
        LocalDateTime openDoors = set.getTimestamp("`events`.open_doors").toLocalDateTime();
        Plato isVoluntary = new Plato(Plato.Argument.valueOf(set.getString("`events`.is_voluntary"))),
            isPublic = new Plato(Plato.Argument.valueOf(set.getString("`events`.is_public"))),
            isCancelled = new Plato(Plato.Argument.valueOf(set.getString("`events`.is_cancelled"))),
            isSoldOut = new Plato(Plato.Argument.valueOf(set.getString("`events`.is_sold_out")));
        String location = set.getString("`events`.location");
        double price = set.getDouble("`events`.price");
        String ticketsURL = set.getString("`events`.tickets_url");
        ContactInfo contactInfo = ModelAssembly.get_instance().assembleContactInfo(set);
        Liszt<Gig> gigs = new Liszt<>();
        Venue venue = new Venue(set.getLong("`events`.venue_id"));
        Liszt<Request> requests = new Liszt<>();
        Liszt<Participation> participations = new Liszt<>();
        Liszt<Bulletin> bulletins = new Liszt<>();
        Album images = new Album(set.getLong("albums.id"),
                title+":images", new Liszt<>(), defineUserType(set), new Liszt<>(), null,
                Album.Kind.IMAGE, set.getTimestamp("albums.`timestamp`").toLocalDateTime());
        LocalDateTime timestamp = set.getTimestamp("`events`.`timestamp`").toLocalDateTime();

        try {
            do {
                if (id != set.getLong("`events`.id"))
                    break;

                gigs = _handler.handleGigs(set, gigs);
                requests = _handler.handleRequests(set, requests,
                        set.getLong("requests.user_id") != venue.get_primaryId()
                                ? new Artist(set.getLong("requests.user_id"))
                                : new Venue(set.getLong("requests.user_id")));
                images = _handler.handleEndpoints(set, images);
            } while (set.next());
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't assemble Event...",e);
            return null;
        }

        return new Event(id,title,description,openDoors,isVoluntary,isPublic,isCancelled,isSoldOut,location,price,
                ticketsURL,contactInfo,gigs,venue,requests,participations,bulletins,images,timestamp);
    }
}
