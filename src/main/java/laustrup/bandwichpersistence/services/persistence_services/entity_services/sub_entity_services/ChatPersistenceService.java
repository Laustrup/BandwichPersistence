package laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services;

import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.repositories.sub_repositories.ChatRepository;
import laustrup.bandwichpersistence.repositories.sub_repositories.ModelRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.utilities.parameters.Plato;
import laustrup.bandwichpersistence.utilities.console.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatPersistenceService {

    /**
     * Singleton instance of the Service.
     */
    private static ChatPersistenceService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ChatPersistenceService get_instance() {
        if (_instance == null) _instance = new ChatPersistenceService();
        return _instance;
    }

    private ChatPersistenceService() {}

    /**
     * Will upsert a Mail.
     * Closes database connection and sets Mail into assembled.
     * @param mail The Mail that will be upserted.
     * @return The ChatRoom of the Mail from the database.
     */
    public ChatRoom upsert(Mail mail) {
        ResultSet set = ChatRepository.get_instance().upsert(mail);
        if (set != null) {
            try {
                if (set.isBeforeFirst())
                    set.next();
                return mail.get_chatRoom().get_primaryId() > 0 ? Assembly.get_instance().finish(
                        Assembly.get_instance().getChatRoomUnassembled(mail.get_chatRoom().get_primaryId())) :
                        Assembly.get_instance().getUser(mail.get_author().get_primaryId()).get_chatRooms().getLast();
            } catch (SQLException e) {
                Printer.get_instance().print("Couldn't get ChatRoom of upserted Mail...",e);
            }
        }
        return null;
    }

    /**
     * Will upsert a ChatRoom and inserts its chatters if they exist.
     * Closes database connection and sets ChatRoom into assembled.
     * @param chatRoom The ChatRoom that will be upserted.
     * @return The ChatRoom from the database.
     */
    public ChatRoom upsert(ChatRoom chatRoom) {
        Long id = ChatRepository.get_instance().upsert(chatRoom);
        if (id!=null)
            return Assembly.get_instance().finish(
                    Assembly.get_instance().getChatRoomUnassembled(id)
            );
        return null;
    }

    /**
     * Uses the ChatRepository to delete the ChatRoom with its Mails
     * with the ids that matches in the database.
     * @param id The id Of the ChatRoom that will be deleted.
     * @return The Plato response of the result.
     */
    public Plato deleteChatRoom(long id) {
        return ChatRepository.get_instance().deleteChatRoom(id);
    }

    /**
     * Uses the ChatRepository to delete the Bulletin
     * with the ids that matches in the database.
     * @param id The id Of the Bulletin that will be deleted.
     * @return The Plato response of the result.
     */
    public Plato deleteBulletin(long id) {return ChatRepository.get_instance().deleteBulletin(id);}

    /**
     * Will upsert a Bulletin of a User.
     * Closes database connection and sets User into assembled.
     * @param bulletin The Bulletin that will be upserted.
     * @return The Receiver from the database.
     */
    public User upsert(Bulletin bulletin) {
        if (ChatRepository.get_instance().upsert(bulletin))
            return Assembly.get_instance().getUser(bulletin.get_receiver().get_primaryId());
        ModelRepository.get_instance().closeConnection();
        return (User) bulletin.get_receiver();
    }
}
