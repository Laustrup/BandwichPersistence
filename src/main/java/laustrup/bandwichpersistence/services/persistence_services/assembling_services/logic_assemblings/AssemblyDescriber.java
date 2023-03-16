package laustrup.bandwichpersistence.services.persistence_services.assembling_services.logic_assemblings;

import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.events.Participation;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.repositories.sub_repositories.ChatRepository;
import laustrup.bandwichpersistence.repositories.sub_repositories.EventRepository;
import laustrup.bandwichpersistence.repositories.sub_repositories.UserRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.EventAssembly;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.user_assemblings.UserAssembly;
import laustrup.bandwichpersistence.utilities.collections.Liszt;
import laustrup.bandwichpersistence.utilities.console.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * A class that fills objects with only id values' other values.
 */
public class AssemblyDescriber {

    private Liszt<Long> _ids = new Liszt<>();

    /**
     * Rebuilds Users that are only with ids.
     * Will be initiated as objects with primitive amounts of attributes.
     * @param users The User objects that should be described.
     * @return The described Users.
     */
    public Liszt<User> describeUsers(Liszt<User> users) {
        _ids = new Liszt<>();
        for (User user : users)
            _ids.add(user.get_primaryId());

        users = new Liszt<>();
        ResultSet set = UserRepository.get_instance().get(_ids);

        for (long id : _ids) {
            try {
                if (set.isBeforeFirst())
                    set.next();
                users.add(UserAssembly.get_instance().assemble(set, false, true));
            } catch (SQLException e) {
                Printer.get_instance().print("Couldn't describe Users...", e);
            }
        }

        return users;
    }

    /**
     * Rebuilds ChatRooms that are only with ids.
     * Will be initiated as objects with primitive amounts of attributes.
     * @param chatRooms The ChatRoom objects that should be described.
     * @return The described ChatRooms.
     */
    public Liszt<ChatRoom> describeChatRooms(Liszt<ChatRoom> chatRooms) {
        _ids = new Liszt<>();

        for (ChatRoom chatRoom : chatRooms)
            _ids.add(chatRoom.get_primaryId());

        chatRooms = new Liszt<>();
        ResultSet set = ChatRepository.get_instance().chatRooms(_ids);

        if (set != null) {
            for (long id : _ids) {
                try {
                    if (set.isBeforeFirst())
                        set.next();
                    chatRooms.add(UserAssembly.get_instance().assembleChatRoom(set));
                } catch (SQLException e) {
                    Printer.get_instance().print("Couldn't describe Chat rooms...", e);
                }
            }
        }

        return chatRooms;
    }

    /**
     * Rebuilds Events that are only with ids.
     * Will be initiated as objects with primitive amounts of attributes.
     * @param events The Event objects that should be described.
     * @return The described Events.
     */
    public Liszt<Event> describeEvents(Liszt<Event> events) {
        if (events != null) {
            _ids = new Liszt<>();
            for (Event event : events)
                _ids.add(event.get_primaryId());

            events = new Liszt<>();
            ResultSet set = EventRepository.get_instance().get(_ids);

            if (set != null) {
                for (long id : _ids) {
                    try {
                        if (set.isBeforeFirst())
                            set.next();
                        events.add(EventAssembly.get_instance().assemble(set, false));
                    } catch (SQLException e) {
                        Printer.get_instance().print("Couldn't describe Events...", e);
                    }
                }
            }
        }

        return events;
    }

    /**
     * Rebuilds the author of the Bulletins from the ids of the authors.
     * @param bulletins The Bulletin objects that should have the authers described.
     * @return The described Bulletins.
     */
    public Liszt<Bulletin> describeBulletinAuthors(Liszt<Bulletin> bulletins) {
        int bulletinAmount = bulletins.size();
        _ids = new Liszt<>();
        for (Bulletin bulletin : bulletins)
            if (bulletin.get_author() != null)
                _ids.add(bulletin.get_author().get_primaryId());

        ResultSet set = UserRepository.get_instance().get(_ids);

        for (int i = 1; i <= (_ids.size() == bulletinAmount ? _ids.size() : 0); i++) {
            try {
                if (set.isBeforeFirst())
                    set.next();
                bulletins.Get(i).set_author(UserAssembly.get_instance().assemble(set, false,true));
            } catch (SQLException e) {
                Printer.get_instance().print("Couldn't describe Bulletins...", e);
            }
        }

        return bulletins;
    }

    /**
     * Rebuilds the User or Event of the Requests from ids.
     * @param requests The Request objects that should be described.
     * @param forUser If it is for a User, it will describe Event, otherwise describe User.
     * @return The described Requests.
     */
    public Liszt<Request> describeRequests(Liszt<Request> requests, boolean forUser) {
        _ids = new Liszt<>();
        for (Request request : requests)
            if (request.hasSecondaryId())
                _ids.add(!forUser ? request.get_primaryId() : request.get_secondaryId());

        ResultSet set = !forUser ? UserRepository.get_instance().get(_ids) : EventRepository.get_instance().get(_ids);

        if (set != null) {
            for (int i = 1; i <= _ids.size(); i++) {
                try {
                    if (set.isBeforeFirst())
                        set.next();
                    if (!forUser)
                        requests.Get(i).set_event(EventAssembly.get_instance().assemble(set, false));
                    else
                        requests.Get(i).set_user(UserAssembly.get_instance().assemble(set, false, true));
                } catch (SQLException e) {
                    Printer.get_instance().print("Couldn't describe Requests...", e);
                }
            }
        }

        return requests;
    }

    //TODO Another gig describe for Event
    public Liszt<Gig> describeGigs(Liszt<Gig> gigs) {
        Liszt<Event> events = new Liszt<>();
        for (Gig gig : gigs)
            events.add(gig.get_event());

        events = describeEvents(events);
        Liszt<Performer[]> acts = generateActs(gigs);

        Liszt<Gig> described = new Liszt<>();

        for (int i = 1; i <= gigs.size(); i++)
            described.add(new Gig(gigs.Get(i).get_primaryId(), events.Get(i), acts.Get(i), gigs.Get(i).get_start(),
                    gigs.Get(i).get_end(), gigs.Get(i).get_timestamp()));

        return described;
    }

    private Liszt<Performer[]> generateActs(Liszt<Gig> gigs) {
        Set<Long> ids = new HashSet<>();
        for (Gig gig : gigs)
            for (Performer performer : gig.get_act())
                ids.add(performer.get_primaryId());

        Liszt<User> users = new Liszt<>();
        for (long id : ids)
            users.add(UserAssembly.get_instance().assemble(id, true));

        Liszt<Performer[]> acts = new Liszt<>();

        for (int i = 1; i <= gigs.size(); i++) {
            Performer[] performers = null;
            for (int j = 0; j < gigs.Get(i).get_act().length; j++) {
                performers = new Performer[gigs.Get(i).get_act().length];
                for (User user : users) {
                    if (user.get_primaryId() == gigs.Get(i).get_act()[i-1].get_primaryId()) {
                        performers[i-1] = (Performer) user;
                        break;
                    }
                }
            }
            acts.add(performers);
        }

        return acts;
    }

    public Liszt<Participation> describeParticipations(Event event) {
        Liszt<Participation> participations = event.get_participations();
        Liszt<Long> ids = new Liszt<>();
        for (Participation participation : participations)
            ids.add(participation.get_primaryId());

        ResultSet set = EventRepository.get_instance().participations(ids);
        try {
            if (set.isBeforeFirst())
                set.next();

            for (int i = 1; i <= participations.size(); i++) {
                participations.set(i, new Participation(
                            (Participant) Assembly.get_instance().getUser(participations.Get(i).get_primaryId()),
                            event, Participation.ParticipationType.valueOf(set.getString("participations.`type`")),
                            set.getTimestamp("participations.`timestamp`").toLocalDateTime()
                        )
                );
                set.next();
            }
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't describe Participations...",e);
        }

        return participations;
    }
}
