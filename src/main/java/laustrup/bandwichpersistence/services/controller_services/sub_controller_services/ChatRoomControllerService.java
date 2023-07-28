package laustrup.bandwichpersistence.services.controller_services.sub_controller_services;

import laustrup.models.Response;
import laustrup.models.chats.ChatRoom;
import laustrup.models.chats.messages.Mail;
import laustrup.dtos.chats.ChatRoomDTO;
import laustrup.bandwichpersistence.services.controller_services.ControllerService;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services.ChatPersistenceService;
import laustrup.utilities.parameters.Plato;

import org.springframework.http.ResponseEntity;

public class ChatRoomControllerService extends ControllerService<ChatRoomDTO> {

    /**
     * Singleton instance of the Service.
     */
    private static ChatRoomControllerService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ChatRoomControllerService get_instance() {
        if (_instance == null) _instance = new ChatRoomControllerService();
        return _instance;
    }

    private ChatRoomControllerService() {}

    /**
     * Upserts a Mail, will insert depending on if there is an id
     * or if the id already exists, in that case it will update it.
     * @param mail The Mail that will be upserted.
     * @return A ResponseEntity with the Response of the ChatRoom of the Mail and the HttpStatus.
     */
    public ResponseEntity<Response<ChatRoomDTO>> upsert(Mail mail) {
        return entityContent(new ChatRoomDTO(ChatPersistenceService.get_instance().upsert(mail)));
    }

    /**
     * Upserts a ChatRoom, will insert depending on if there is an id
     * or if the id already exists, in that case it will update it.
     * Also inserts Chatters if they exist.
     * @param chatRoom The ChatRoom that will be upserted.
     * @return A ResponseEntity with the Response of the ChatRoom and the HttpStatus.
     */
    public ResponseEntity<Response<ChatRoomDTO>> upsert(ChatRoom chatRoom) {
        return entityContent(new ChatRoomDTO(ChatPersistenceService.get_instance().upsert(chatRoom)));
    }

    /**
     * Will use the ChatPersistenceService to delete the ChatRoom with its Mails that
     * matches the id in the database.
     * @param id The id of the ChatRoom that will be deleted.
     * @return The Plato argument of the result in a Response of a ResponseEntity.
     */
    public ResponseEntity<Response<Plato.Argument>> deleteChatRoom(long id) {
        return platoContent(ChatPersistenceService.get_instance().deleteChatRoom(id));
    }

    /**
     * Will use the ChatPersistenceService to delete the Bulletin that matches the id in the database.
     * @param id The id of the Bulletin that will be deleted.
     * @return The Plato argument of the result in a Response of a ResponseEntity.
     */
    public ResponseEntity<Response<Plato.Argument>> deleteBulletin(long id) {
        return platoContent(ChatPersistenceService.get_instance().deleteBulletin(id));
    }
}
