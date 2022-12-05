package laustrup.bandwichpersistence.services.persistence_services.assembling_services.logic_assemblings;

import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.user_assemblings.UserAssembly;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Plato;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AssemblyHandler {

    public Liszt<Album> handleMusic(ResultSet set, Liszt<Album> music, User user) throws SQLException {
        String table = "album";

        Album album = null;
        if (set.getBoolean("album_relations.is_author"))
            album = new Album(set.getLong(table+".id"),
                set.getString(table+".title"), new Liszt<>(), user, new Liszt<>(),
                    (((Long) set.getLong("album_relations.event_id")) != null ?
                        new Event(set.getLong("album_relations.event_id")) : null),
                    Album.Kind.MUSIC, set.getTimestamp(table+".`timestamp`").toLocalDateTime());
        else
            for (int i = 1; i <= music.size(); i++) {
                if (music.get(i).get_primaryId() == set.getLong("album_relations.album_id")) {
                    music.get(i).add(user);
                    break;
                }
            }

        if (music != null) {
            assert album != null;
            if (!music.contains(album.toString()))
                music.add(album);
        }

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
                !album.get_endpoints().contains(endpoint))
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

    public Liszt<Gig> handleGigs(ResultSet set, Liszt<Gig> gigs) throws SQLException {
        String table = "acts";
        // This Artist could also be a Band, but only the id is needed and will be specified in Assembly.
        Performer performer = new Artist(set.getLong(table+".user_id"));

        table = "gigs";
        Gig gig = new Gig(set.getLong(table+".id"),
                new Event(set.getLong(table+".event_id")),
                new Performer[]{},
                set.getTimestamp(table+".`start`").toLocalDateTime(),
                set.getTimestamp(table+".`end`").toLocalDateTime(),
                set.getTimestamp(table+".`timestamp`").toLocalDateTime());

        if (gigs.get(gigs.size()).get_primaryId()!=set.getLong(table+".id"))
            gigs.add(gig);
        if (!gigs.get(gigs.size()).contains(performer))
            gigs.get(gigs.size()).add(performer);

        return gigs;
    }

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

    public Liszt<ChatRoom> handleChatRooms(ResultSet set, Liszt<ChatRoom> chatRooms) throws SQLException {
        String table = "chat_rooms";
        ChatRoom chatRoom = new ChatRoom(set.getLong(table+".id"),
                set.getString(table+".title"),
                set.getTimestamp(table+".`timestamp`").toLocalDateTime());

        if (!chatRooms.contains(chatRoom))
            chatRooms.add(chatRoom);

        return chatRooms;
    }

    public Liszt<Bulletin> handleBulletins(ResultSet set, Liszt<Bulletin> bulletins, boolean forEvents) throws SQLException {
        String table = forEvents ? "event_bulletins" : "user_bulletins";
        Bulletin bulletin = new Bulletin(set.getLong(table+".id"),
                set.getString(table+".content"),set.getBoolean(table+".is_sent"),
                new Plato(Plato.Argument.valueOf(set.getString(table+".is_edited"))), set.getBoolean(table+".is_public"),
                set.getTimestamp(table+".`timestamp`").toLocalDateTime());

        if (!bulletins.contains(bulletin))
            bulletins.add(bulletin);

        return bulletins;
    }

    public Liszt<User> handleIdols(ResultSet set, Liszt<User> idols) throws SQLException {
        String table = "followings";
        User idol;
        switch (set.getString(table+".idol_kind")) {
            case "BAND" -> idol = new Band(set.getLong(table + ".idol_id"));
            case "ARTIST" -> idol = new Artist(set.getLong(table + ".idol_id"));
            case "VENUE" -> idol = new Venue(set.getLong(table + ".idol_id"));
            case "PARTICIPANT" -> idol = new Participant(set.getLong(table + ".idol_id"));
            default -> idol = null;
        }

        if (idol != null && !idols.contains(idol))
            idols.add(idol);

        return idols;
    }

    public Liszt<User> handleFans(ResultSet set, Liszt<User> fans) throws SQLException {
        String table = "followings";
        User fan;
        switch (set.getString(table+".fan_kind")) {
            case "BAND" -> fan = new Band(set.getLong(table+".fan_id"));
            case "ARTIST" -> fan = new Artist(set.getLong(table+".fan_id"));
            case "VENUE" -> fan = new Venue(set.getLong(table+".fan_id"));
            case "PARTICIPANT" -> fan = new Participant(set.getLong(table+".fan_id"));
            default -> fan = null;
        }

        if (fan != null && !fans.contains(fan))
            fans.add(fan);

        return fans;
    }

    public Liszt<Request> handleRequests(ResultSet set, Liszt<Request> requests, User user) throws SQLException {
        String table = "requests";
        Request request = new Request(user, new Event(set.getLong(table+".event_id")),
                new Plato(Plato.Argument.valueOf(set.getString(table+".is_approved"))),
                set.getString(table+".message"),
                set.getTimestamp(table+".`timestamp`").toLocalDateTime());

        if (!requests.contains(request.toString()))
            requests.add(request);

        return requests;
    }

    public Liszt<Mail> handleMails(ResultSet set, Liszt<Mail> mails) throws SQLException {
        String table = "mails";
        Mail mail = new Mail(set.getLong(table+".id"),new Artist(set.getLong(table+".author_id")),
                set.getString(table+".content"),set.getBoolean(table+".is_sent"),
                new Plato(Plato.Argument.valueOf(set.getString(table+".is_edited"))),set.getBoolean(table+".is_public"),
                set.getTimestamp(table+".`timestamp`").toLocalDateTime());

        if (!mails.contains(mail)) {
            User author = UserAssembly.get_instance().assemble(mail.get_author().get_primaryId());
            mails.add(new Mail(set.getLong(table+".id"),author,
                    set.getString(table+".content"),set.getBoolean(table+".is_sent"),
                    new Plato(Plato.Argument.valueOf(set.getString(table+".is_edited"))),set.getBoolean(table+".is_public"),
                    set.getTimestamp(table+".`timestamp`").toLocalDateTime()));
        }

        return mails;
    }
    public Liszt<User> handleChatters(ResultSet set, Liszt<User> chatters) throws SQLException {
        String table = "chatters";
        boolean chatterExists = false;

        for (User user : chatters) {
            if (user.get_primaryId() == set.getLong(table+".user_id")) {
                chatterExists = true;
                break;
            }
        }

        if (!chatterExists)
            chatters.add(UserAssembly.get_instance().assemble(set.getLong(table+".user_id")));

        return chatters;
    }
}
