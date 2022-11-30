package laustrup.bandwichpersistence.services.persistence_services.assembling_services;

import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Plato;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AssemblyHandler {

    public Liszt<Album> handleMusic(ResultSet set, Liszt<Album> music) throws SQLException {
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

    public Album handleEndpoints(ResultSet set, Album album) throws SQLException {
        String table = "album_endpoints";
        String endpoint = set.getString(table+".`value`");
        if (album.get_primaryId() == set.getLong(table+".album_id") &&
                !album._endpoints.contains(endpoint))
            album.add(endpoint);

        return album;
    }

    public Liszt<Rating> handleRatings(ResultSet set, Liszt<Rating> ratings) throws SQLException {
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
    public Liszt<Gig> handleGigs(ResultSet set, Liszt<Gig> gigs) throws SQLException {
        String table = "acts";
        // This Artist could also be a Band, but only the id is needed and will be specified in Assembly.
        Performer performer = new Artist(set.getLong(table+".user_id"));

        table = "gigs";
        Gig gig = new Gig(set.getLong(table+".id"), new Performer[]{},
                set.getTimestamp(table+".`start`").toLocalDateTime(),
                set.getTimestamp(table+".`end`").toLocalDateTime(),
                set.getTimestamp(table+".`timestamp`").toLocalDateTime());

        if (gigs.get(gigs.size()).get_primaryId()!=set.getLong(table+".id"))
            gigs.add(gig);
        if (!gigs.get(gigs.size()).contains(performer))
            gigs.get(gigs.size()).add(performer);

        return gigs;
    }

    //TODO Finish values
    public Liszt<Event> handleEvents(ResultSet set, Liszt<Event> events) throws SQLException {
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
    public Liszt<ChatRoom> handleChatRooms(ResultSet set, Liszt<ChatRoom> chatRooms) throws SQLException {
        String table = "chat_rooms";
        ChatRoom chatRoom = new ChatRoom(set.getLong(table+".id"),
                set.getString(table+".title"),
                set.getTimestamp(table+".`timestamp`").toLocalDateTime());

        if (!chatRooms.contains(chatRoom))
            chatRooms.add(chatRoom);

        return chatRooms;
    }

    //TODO Finish values
    public Liszt<Bulletin> handleBulletins(ResultSet set, Liszt<Bulletin> bulletins) throws SQLException {
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
