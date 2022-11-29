package laustrup.bandwichpersistence.services.persistence_services.assembling_services.users;

import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.subscriptions.Subscription;
import laustrup.bandwichpersistence.repositories.sub_repositories.UserRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.ModelAssembly;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Plato;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Is only used for database read functions.
 * Will build Bands from database row values.
 * Is a singleton.
 */
public class BandAssembly {

    /**
     * Singleton instance of the Service.
     */
    private static BandAssembly _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static BandAssembly get_instance() {
        if (_instance == null) _instance = new BandAssembly();
        return _instance;
    }

    private BandAssembly() {}

    /**
     * Assembles a Band with values from the ResultSet.
     * There needs to be made a set.next() before executing this method.
     * @param set A ResultSet that has been executed early, must include the values intended for the assembled object.
     * @return A Band object made from the values of the ResultSet.
     * @throws SQLException Will be triggered from the ResultSet, if there is an error.
     */
    public Band assemble(ResultSet set) throws SQLException {
        long id = set.getLong("users.id");
        String username = set.getString("users.username"), description = set.getString("users.`description`");
        ContactInfo contactInfo = ModelAssembly.get_instance().assembleContactInfo(set);
        Album images = new Album(username+":images", Album.Kind.IMAGE);
        Liszt<Rating> ratings = new Liszt<>();
        Liszt<Event> events = new Liszt<>();
        Liszt<ChatRoom> chatRooms = new Liszt<>();
        Subscription subscription = ModelAssembly.get_instance().assembleSubscription(set);
        Liszt<Bulletin> bulletins = new Liszt<>();
        LocalDateTime timestamp = set.getTimestamp("users.`timestamp`").toLocalDateTime();
        Liszt<Album> music = new Liszt<>();
        Liszt<Long> memberIds = new Liszt<>();
        String runner = set.getString("gear.`description`");
        Liszt<Participant> fans = new Liszt<>();
        Liszt<User> followings = new Liszt<>();

        do {
            if (Album.Kind.valueOf(set.getString("albums.kind")).equals(Album.Kind.IMAGE))
                images = handleEndpoints(set, images);
            else
                music = handleMusic(set, music);

            ratings = handleRatings(set, ratings);
            events = handleEvents(set, events);
            chatRooms = handleChatRooms(set, chatRooms);
            bulletins = handleBulletins(set, bulletins);

            if (!memberIds.contains(set.getLong("band_members.artist_id")))
                memberIds.add(set.getLong("band_members.artist_id"));

        } while (set.next());

        Liszt<Artist> members = ArtistAssembly.get_instance().assembles(UserRepository.get_instance().get(memberIds));


        return new Band(id,username,description,contactInfo,images,ratings,events,chatRooms,subscription,bulletins,
                timestamp,music,members,runner,fans,followings);
    }

    private Liszt<Album> handleMusic(ResultSet set, Liszt<Album> music) throws SQLException {
        String table = "album";
        Album album = new Album(set.getLong(table+".id"),
                set.getString(table+".title"),
                new Liszt<>(),null, new Liszt<>(), Album.Kind.MUSIC,
                set.getTimestamp(table+".`timestamp`").toLocalDateTime());

        if (!music.contains(album.toString()))
            music.add(album);

        try {
            music.replace(handleEndpoints(set, music.get(music.size())),music.size());
        } catch (ClassNotFoundException e) {
            music.set(music.size(), handleEndpoints(set, music.get(music.size())));
        }

        return music;
    }

    private Album handleEndpoints(ResultSet set, Album album) throws SQLException {
        String table = "album_endpoints";
        String endpoint = set.getString(table+".`value`");
        if (album.get_primaryId() == set.getLong(table+".album_id") &&
                !album._endpoints.contains(endpoint))
            album.add(endpoint);

        return album;
    }

    private Liszt<Rating> handleRatings(ResultSet set, Liszt<Rating> ratings) throws SQLException {
        String table = "ratings";
        Rating rating = new Rating(set.getInt(table+".`value`"),
                set.getLong(table+".appointed_id"),
                set.getLong(table+".judge_id"),
                set.getTimestamp(table+".`timestamp`").toLocalDateTime());

        if (!ratings.contains(rating.toString()))
            ratings.add(rating);

        return ratings;
    }

    //TODO Finish values
    private Liszt<Event> handleEvents(ResultSet set, Liszt<Event> events) throws SQLException {
        String table = "`events`";
        Event event = new Event(set.getLong(table+".id"),
                set.getString(table+".title"),
                set.getString(table+".description"),
                set.getTimestamp(table+".open_doors").toLocalDateTime(),
                new Plato(Plato.Argument.valueOf(set.getString(table+".is_voluntary"))),
                new Plato(Plato.Argument.valueOf(set.getString(table+".is_public"))),
                new Plato(Plato.Argument.valueOf(set.getString(table+".is_cancelled"))),
                new Plato(Plato.Argument.valueOf(set.getString(table+".is_sold_out"))),
                set.getString(table+".location"),set.getDouble(table+".price"),
                set.getString(table+".tickets_url"),null,new Liszt<>(),null,new Liszt<>(),
                new Liszt<>(),new Liszt<>(),new Album(),set.getTimestamp(table+".timestamp").toLocalDateTime());

        if (!events.contains(event.toString()))
            events.add(event);

        return events;
    }

    //TODO Finish values
    private Liszt<ChatRoom> handleChatRooms(ResultSet set, Liszt<ChatRoom> chatRooms) throws SQLException {
        String table = "chat_rooms";
        ChatRoom chatRoom = new ChatRoom(set.getLong(table+".id"),
                set.getString(table+".title"),
                set.getTimestamp(table+".`timestamp`").toLocalDateTime());

        if (!chatRooms.contains(chatRoom))
            chatRooms.add(chatRoom);

        return chatRooms;
    }

    //TODO Finish values
    private Liszt<Bulletin> handleBulletins(ResultSet set, Liszt<Bulletin> bulletins) throws SQLException {
        String table = "bulletins";

        Bulletin bulletin = new Bulletin(set.getLong(table+".id"),
                set.getString(table+".content"),set.getBoolean(table+".is_sent"),
                set.getBoolean(table+".is_edited"), set.getBoolean(table+".is_public"),
                set.getTimestamp(table+".`timestamp`").toLocalDateTime());

        if (!bulletins.contains(bulletin))
            bulletins.add(bulletin);

        return bulletins;
    }

}
