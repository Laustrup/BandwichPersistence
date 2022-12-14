package laustrup.bandwichpersistence.controllers;

import laustrup.bandwichpersistence.models.Response;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.ChatRoomControllerService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/chat_room/")
public class ChatRoomController {

    @PutMapping(value = "upsert", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<ChatRoom>> upsert(@RequestBody Mail mail) {
        return ChatRoomControllerService.get_instance().upsert(new Mail(
                mail.get_primaryId(),mail.get_chatRoom(),mail.get_author(),
                mail.get_content(),mail.is_sent(),mail.get_edited(),
                mail.is_public(),mail.get_timestamp()
                )
        );
    }

    @PostMapping(value = "upsert", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<ChatRoom>> upsert(@RequestBody ChatRoom chatRoom) {
        return ChatRoomControllerService.get_instance().upsert(new ChatRoom(
                chatRoom.get_primaryId(),chatRoom.get_title(),chatRoom.get_mails(),
                chatRoom.get_chatters(),chatRoom.get_responsible(),chatRoom.get_timestamp()
                )
        );
    }
}