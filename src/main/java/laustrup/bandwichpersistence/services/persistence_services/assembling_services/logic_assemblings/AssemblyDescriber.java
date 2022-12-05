package laustrup.bandwichpersistence.services.persistence_services.assembling_services.logic_assemblings;

import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.repositories.sub_repositories.UserRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings.user_assemblings.UserAssembly;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A class that fills objects with only id values' other values.
 */
public class AssemblyDescriber {

    /**
     * Rebuilds Users that are only with ids.
     * Will be initiated as objects with primitive amounts of attributes.
     * @param users The User objects that should be described.
     * @return The described Users.
     */
    public Liszt<User> describeUsers(Liszt<User> users) {
        Liszt<Long> ids = new Liszt<>();
        for (User user : users)
            ids.add(user.get_primaryId());

        users = new Liszt<>();
        ResultSet set = UserRepository.get_instance().get(ids);

        for (long id : ids) {
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
        Liszt<Long> ids = new Liszt<>();

        for (ChatRoom chatRoom : chatRooms)
            ids.add(chatRoom.get_primaryId());

        chatRooms = new Liszt<>();
        ResultSet set = UserRepository.get_instance().getChatRooms(ids);

        for (long id : ids) {
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
}
