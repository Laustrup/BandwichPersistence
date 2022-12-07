package laustrup.bandwichpersistence.controllers;

import laustrup.bandwichpersistence.models.Response;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.ChatRoomControllerService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/chat_room/")
public class ChatRoomController {

    @PostMapping(value = "upsert/mail", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<ChatRoom>> upsert(@RequestBody Mail mail) {
        return ChatRoomControllerService.get_instance().upsert(new Mail(
                mail.get_primaryId(),mail.get_chatRoom(),mail.get_author(),
                mail.get_content(),mail.is_sent(),mail.get_edited(),
                mail.is_public(),mail.get_timestamp())
        );
    }
}