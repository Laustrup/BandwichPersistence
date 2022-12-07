package laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services;

import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.repositories.sub_repositories.ModelRepository;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.UserControllerService;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
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
     * Will upsert a Mail
     * @param mail
     * @return
     */
    public ChatRoom upsert(Mail mail) {
        ResultSet set = ModelRepository.get_instance().upsert(mail);
        if (set != null) {
            try {
                if (set.isBeforeFirst())
                    set.next();
                return Assembly.get_instance().finish(
                        Assembly.get_instance().getChatRoomUnassembled(
                                set.getLong("mails.chat_room_id")));
            } catch (SQLException e) {
                Printer.get_instance().print("Couldn't get ChatRoom of upserted Mail...",e);
            }
        }
        return null;
    }
}
