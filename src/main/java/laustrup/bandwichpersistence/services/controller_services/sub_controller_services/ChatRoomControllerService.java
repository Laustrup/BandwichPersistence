package laustrup.bandwichpersistence.services.controller_services.sub_controller_services;

import laustrup.bandwichpersistence.models.Response;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.services.controller_services.ControllerService;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services.UserPersistenceService;

import org.springframework.http.ResponseEntity;

public class ChatRoomControllerService extends ControllerService<ChatRoom> {

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
    public ResponseEntity<Response<ChatRoom>> upsert(Mail mail) {
        return entityContent(UserPersistenceService.get_instance().upsert(mail));
    }
}
