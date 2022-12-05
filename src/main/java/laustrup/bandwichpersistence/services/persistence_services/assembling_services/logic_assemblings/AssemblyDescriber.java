package laustrup.bandwichpersistence.services.persistence_services.assembling_services.logic_assemblings;

import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.repositories.sub_repositories.EventRepository;
import laustrup.bandwichpersistence.repositories.sub_repositories.MiscRepository;
import laustrup.bandwichpersistence.repositories.sub_repositories.UserRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.EventAssembly;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.user_assemblings.UserAssembly;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

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
                users.add(UserAssembly.get_instance().assemble(set, false));
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
        ResultSet set = MiscRepository.get_instance().chatRooms(_ids);

        for (long id : _ids) {
            try {
                if (set.isBeforeFirst())
                    set.next();
                chatRooms.add(UserAssembly.get_instance().assembleChatRoom(set));
            } catch (SQLException e) {
                Printer.get_instance().print("Couldn't describe Users...", e);
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
        _ids = new Liszt<>();
        for (Event event : events)
            _ids.add(event.get_primaryId());

        events = new Liszt<>();
        ResultSet set = EventRepository.get_instance().get(_ids);

        for (long id : _ids) {
            try {
                if (set.isBeforeFirst())
                    set.next();
                events.add(EventAssembly.get_instance().assemble(set, false));
            } catch (SQLException e) {
                Printer.get_instance().print("Couldn't describe Events...", e);
            }
        }

        return events;
    }

    /**
     * Rebuilds the author of the Bulletin from the ids of the authors
     * @param bulletins The Bulletin objects that should have the authers described.
     * @return The described Bulletins.
     */
    public Liszt<Bulletin> describeBulletins(Liszt<Bulletin> bulletins) {
        int bulletinAmount = bulletins.size();
        _ids = new Liszt<>();
        for (Bulletin bulletin : bulletins)
            _ids.add(bulletin.get_author().get_primaryId());

        bulletins = new Liszt<>();
        ResultSet set = MiscRepository.get_instance().chatRooms(_ids);

        for (int i = 1; i <= (_ids.size() == bulletinAmount ? _ids.size() : 0); i++) {
            try {
                if (set.isBeforeFirst())
                    set.next();
                bulletins.get(i).set_author(UserAssembly.get_instance().assemble(_ids.get(i)));
            } catch (SQLException e) {
                Printer.get_instance().print("Couldn't describe Bulletins...", e);
            }
        }

        return bulletins;
    }
}
