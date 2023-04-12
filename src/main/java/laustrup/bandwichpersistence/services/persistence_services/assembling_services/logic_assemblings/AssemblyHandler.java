package laustrup.bandwichpersistence.services.persistence_services.assembling_services.logic_assemblings;

import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.albums.AlbumItem;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.events.Participation;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.services.TimeService;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.user_assemblings.UserAssembly;
import laustrup.bandwichpersistence.utilities.collections.lists.Liszt;
import laustrup.bandwichpersistence.utilities.parameters.Plato;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;

public class AssemblyHandler {

    public Liszt<Album> handleAlbums(ResultSet set, Liszt<Album> albums) throws SQLException {
        String table = "albums";

        if (set.getLong(table+".id") > 0) {
            Album album = new Album(set.getLong(table+".id"),set.getString(table+".title"),
                    new Liszt<>(),new Participant(set.getLong(table+".author_id")),
                    TimeService.get_instance().convertFromDatabase(set,table+".timestamp"));

            if (!albums.contains(album.toString()))
                albums.add(handleAlbumItems(set,album));

            if (!albums.getLast().get_items().isEmpty()) {
                if (!albums.getLast().get_items().getLast().get_endpoint()
                        .equals(set.getString("albums_items.endpoint")))
                    albums.set(albums.size(),handleAlbumItems(set,albums.getLast()));
                else if (albums.getLast().get_items().getLast().get_tags()
                        .getLast().get_primaryId() != set.getLong("tags.user_id"))
                    albums.set(albums.size(),handleTags(set,albums.getLast()));
            }
        }

        return albums;
    }

    public Album handleAlbumItems(ResultSet set, Album album) throws SQLException {
        String table = "album_items";

        if (set.getString(table+".endpoint") != null) {
            AlbumItem item = new AlbumItem(set.getString(table+".title"),
                    set.getString(table+".endpoint"),
                    set.getString(table+".kind") == null ? null :
                            AlbumItem.Kind.valueOf(set.getString(table+".kind")),new Liszt<>(),
                    new Event(set.getLong(table+".event_id")),
                    TimeService.get_instance().convertFromDatabase(set,table+".timestamp"));

            if (!album.get_items().contains(item))
                album.add(item);

            if (!album.get_items().isEmpty())
                if (!album.get_items().getLast().get_tags().isEmpty())
                    if (album.get_items().getLast().get_tags().getLast().get_primaryId()
                            != set.getLong("tags.user_id"))
                        album = handleTags(set,album);
        }

        return album;
    }

    public Album handleTags(ResultSet set, Album album) throws SQLException {
        String table = "tags";

        if (set.getLong(table+".user_id") > 0) {
            User user = new Participant(set.getLong(table+".user_id"));

            if (album.get_items().getLast().get_tags().getLast().get_primaryId() != user.get_primaryId())
                album.get_items().getLast().get_tags().add(user);
        }

        return album;
    }

    public Liszt<Rating> handleRatings(ResultSet set, Liszt<Rating> ratings) throws SQLException {
        String table = "ratings";

        try {
            Rating rating = new Rating(set.getInt(table+".value"),
                    set.getLong(table+".appointed_id"),
                    set.getLong(table+".judge_id"),
                    set.getString(table+".comment"),
                    TimeService.get_instance().convertFromDatabase(set,table+".timestamp"));

            if (!ratings.contains(rating.toString()))
                ratings.add(rating);
        } catch (InputMismatchException ignored) {}

        return ratings;
    }

    public Liszt<Gig> handleGigs(ResultSet set, Liszt<Gig> gigs) throws SQLException {
        String table = "acts";

        if (set.getLong(table+".user_id") > 0) {
            // This Artist could also be a Band, but only the id is needed and will be specified in Assembly.
            Performer performer = new Artist(set.getLong(table+".user_id"));

            table = "gigs";
            if (set.getLong(table+".id") > 0) {
                Gig gig = new Gig(set.getLong(table+".id"),
                        new Event(set.getLong(table+".event_id")),
                        new Performer[]{},
                        TimeService.get_instance().convertFromDatabase(set,table+".start"),
                        TimeService.get_instance().convertFromDatabase(set,table+".end"),
                        TimeService.get_instance().convertFromDatabase(set,table+".timestamp"));

                if (!gigs.isEmpty()) {
                    if (gigs.getLast().get_primaryId()!=set.getLong(table+".id"))
                        gigs.add(gig);
                    if (!gigs.getLast().contains(performer))
                        gigs.getLast().add(performer);
                }
                else
                    gigs.add(gig);
            }
        }

        return gigs;
    }

    public Liszt<Event> handleEvents(ResultSet set, Liszt<Event> events) throws SQLException {
        String table = "events";

        if (set.getLong(table+".id") > 0) {
            Event event = new Event(set.getLong(table+".id"),
                    set.getString(table+".title"),
                    set.getString(table+".description"),
                    TimeService.get_instance().convertFromDatabase(set,table+".open_doors"),
                    convertPlatoFromDatabase(set,table+".is_voluntary"),
                    convertPlatoFromDatabase(set,table+".is_public"),
                    convertPlatoFromDatabase(set,table+".is_cancelled"),
                    convertPlatoFromDatabase(set,table+".is_sold_out"),
                    set.getString(table+".location"),set.getDouble(table+".price"),
                    set.getString(table+".tickets_url"),null,new Liszt<>(),null,new Liszt<>(),
                    new Liszt<>(),new Liszt<>(),new Liszt<>(),
                    TimeService.get_instance().convertFromDatabase(set,table+".timestamp")
            );

            if (!events.contains(event.toString()))
                events.add(event);
        }

        return events;
    }

    public Liszt<Participation> handleParticipations(ResultSet set, Liszt<Participation> participations) throws SQLException {
        String table = "participations";

        if (set.getLong(table+".participant_id") > 0 && set.getLong(table+".event_id") > 0) {
            Participation participation = new Participation(
                    new Participant(set.getLong(table+".participant_id")),
                    new Event(set.getLong(table+".event_id")),
                    Participation.ParticipationType.valueOf(set.getString(table+".type"))
            );

            if (!participations.contains(participation.toString()))
                participations.add(participation);
        }

        return participations;
    }

    public Liszt<ChatRoom> handleChatRooms(ResultSet set, Liszt<ChatRoom> chatRooms) throws SQLException {
        String table = "chat_rooms";

        if (set.isBeforeFirst())
            set.next();

        if (set.getLong(table+".id") > 0) {
            ChatRoom chatRoom = new ChatRoom(set.getLong(table+".id"), set.getBoolean("is_local"),
                    set.getString(table+".title") == null ? new String() : set.getString(table+".title"),
                    TimeService.get_instance().convertFromDatabase(set,table+".timestamp"));

            if (!chatRooms.contains(chatRoom))
                chatRooms.add(chatRoom);
        }

        return chatRooms;
    }

    public Liszt<Bulletin> handleBulletins(ResultSet set, Liszt<Bulletin> bulletins) throws SQLException {
        String table = "bulletins";

        if (set.getLong(table+".id") > 0) {
            Bulletin bulletin = new Bulletin(set.getLong(table+".id"),
                    set.getString(table+".content"),set.getBoolean(table+".is_sent"),
                    convertPlatoFromDatabase(set,table+".is_edited"), set.getBoolean(table+".is_public"),
                    TimeService.get_instance().convertFromDatabase(set,table+".timestamp"));

            if (!bulletins.contains(bulletin))
                bulletins.add(bulletin);
        }

        return bulletins;
    }

    public Liszt<User> handleIdols(ResultSet set, Liszt<User> idols) throws SQLException {
        String table = "followings";
        User idol = null;
        if (set.getString(table+".idol_kind") != null && set.getLong(table + ".idol_id") > 0) {
            switch (set.getString(table+".idol_kind")) {
                case "BAND" -> idol = new Band(set.getLong(table + ".idol_id"));
                case "ARTIST" -> idol = new Artist(set.getLong(table + ".idol_id"));
                case "VENUE" -> idol = new Venue(set.getLong(table + ".idol_id"));
                case "PARTICIPANT" -> idol = new Participant(set.getLong(table + ".idol_id"));
            }

            if (idol != null && !idols.contains(idol))
                idols.add(idol);
        }

        return idols;
    }

    public Liszt<User> handleFans(ResultSet set, Liszt<User> fans) throws SQLException {
        String table = "followings";
        User fan;
        if (set.getString(table+".fan_kind") != null && set.getLong(table + ".fan_id") > 0) {
            switch (set.getString(table+".fan_kind")) {
                case "BAND" -> fan = new Band(set.getLong(table+".fan_id"));
                case "ARTIST" -> fan = new Artist(set.getLong(table+".fan_id"));
                case "VENUE" -> fan = new Venue(set.getLong(table+".fan_id"));
                case "PARTICIPANT" -> fan = new Participant(set.getLong(table+".fan_id"));
                default -> fan = null;
            }
            if (fan != null && !fans.contains(fan))
                fans.add(fan);
        }

        return fans;
    }

    public Liszt<Request> handleRequests(ResultSet set, Liszt<Request> requests, User user) throws SQLException {
        String table = "requests";

        if (set.getLong(table+".event_id") > 0 && set.getLong(table+".user_id") > 0) {
            Request request = new Request(user, new Event(set.getLong(table+".event_id")),
                    convertPlatoFromDatabase(set,table+".is_approved"),
                    set.getString(table+".message"),
                    TimeService.get_instance().convertFromDatabase(set,table+".timestamp"));

            if (!requests.contains(request.toString()))
                requests.add(request);
        }

        return requests;
    }

    public Liszt<Mail> handleMails(ResultSet set, Liszt<Mail> mails) throws SQLException {
        String table = "mails";

        if (set.getLong(table+".id") > 0) {
            Mail mail = new Mail(set.getLong(table+".id"),new Artist(set.getLong(table+".author_id")),
                    set.getString(table+".content"),set.getBoolean(table+".is_sent"),
                    convertPlatoFromDatabase(set,table+".is_edited"),
                    set.getBoolean(table+".is_public"),
                    TimeService.get_instance().convertFromDatabase(set,table+".timestamp"));

            if (!mails.contains(mail)) {
                User author = UserAssembly.get_instance().assemble(mail.get_author().get_primaryId(),true);
                mails.add(new Mail(set.getLong(table+".id"),author,
                        set.getString(table+".content"),set.getBoolean(table+".is_sent"),
                        convertPlatoFromDatabase(set,table+".is_edited"), set.getBoolean(table+".is_public"),
                        TimeService.get_instance().convertFromDatabase(set,table+".timestamp")));
            }
        }

        return mails;
    }
    public Liszt<User> handleChatters(ResultSet set, Liszt<User> chatters) throws SQLException {
        String table = "chatters";

        if (set.getLong(table+".user_id") > 0) {
            boolean chatterExists = false;

            for (User user : chatters) {
                if (user.get_primaryId() == set.getLong(table+".user_id")) {
                    chatterExists = true;
                    break;
                }
            }

            if (!chatterExists)
                chatters.add(UserAssembly.get_instance().assemble(set.getLong(table+".user_id"),true));
        }

        return chatters;
    }

    private Plato convertPlatoFromDatabase(ResultSet set, String row) throws SQLException {
        return set.getString(row) == null ? null :
                new Plato(Plato.Argument.valueOf(set.getString(row)));
    }
}
