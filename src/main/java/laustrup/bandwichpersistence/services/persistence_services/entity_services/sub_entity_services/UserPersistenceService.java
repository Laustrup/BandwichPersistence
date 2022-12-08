package laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services;

import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.repositories.sub_repositories.ModelRepository;
import laustrup.bandwichpersistence.repositories.sub_repositories.UserRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.utilities.Plato;
import laustrup.bandwichpersistence.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contains logic for CRUD of Participants.
 */
public class UserPersistenceService {

    /**
     * Singleton instance of the Service.
     */
    private static UserPersistenceService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static UserPersistenceService get_instance() {
        if (_instance == null) _instance = new UserPersistenceService();
        return _instance;
    }

    private UserPersistenceService() {}

    /**
     * Will upsert a Mail.
     * Closes database connections and sets Mail into assembled.
     * @param mail The Mail that will be upserted.
     * @return The ChatRoom of the Mail from the database.
     */
    public ChatRoom upsert(Mail mail) {
        ResultSet set = ModelRepository.get_instance().upsert(mail);
        if (set != null) {
            try {
                if (set.isBeforeFirst())
                    set.next();
                return Assembly.get_instance().finish(
                        Assembly.get_instance().getChatRoomUnassembled(
                                set.getLong("mails.chat_room_id"))
                );
            } catch (SQLException e) {
                Printer.get_instance().print("Couldn't get ChatRoom of upserted Mail...",e);
            }
        }
        return null;
    }

    /**
     * Will upsert a ChatRoom and inserts its chatters if they exist.
     * Closes database connections and sets ChatRoom into assembled.
     * @param chatRoom The ChatRoom that will be upserted.
     * @return The ChatRoom from the database.
     */
    public ChatRoom upsert(ChatRoom chatRoom) {
        Long id = ModelRepository.get_instance().upsert(chatRoom);
        if (id!=null)
            return Assembly.get_instance().finish(
                    Assembly.get_instance().getChatRoomUnassembled(id)
            );
        return null;
    }

    /**
     * Will delete a User and connections from repository will be closed.
     * @param user The User that will be deleted.
     * @return A Plato with true truth if success, otherwise false with a message.
     */
    public Plato delete(User user) {
        if (user.get_primaryId()>0)
            return new Plato(UserRepository.get_instance().delete(user));
        Plato status = new Plato(false);
        status.set_message("Couldn't delete " + user.get_title() + "...");
        return status;
    }

    /**
     * Will upsert a Bulletin of a User.
     * Closes database connections and sets User into assembled.
     * @param bulletin The Bulletin that will be upserted.
     * @return The Receiver from the database.
     */
    public User upsert(Bulletin bulletin) {
        if (ModelRepository.get_instance().upsert(bulletin, true))
            return Assembly.get_instance().getUser(bulletin.get_receiver().get_primaryId());
        return (User) bulletin.get_receiver();
    }
}
