package laustrup.bandwichpersistence.controllers;

import laustrup.bandwichpersistence.models.Response;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.dtos.chats.ChatRoomDTO;
import laustrup.bandwichpersistence.models.dtos.chats.messages.MailDTO;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.ChatRoomControllerService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") @RestController
public class ChatRoomController {

    private final String _endpointDirectory = "/api/chat_room/";

    @PutMapping(value = _endpointDirectory + "/mail/upsert", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<ChatRoomDTO>> upsert(@RequestBody MailDTO mail) {
        return ChatRoomControllerService.get_instance().upsert(new Mail(mail));
    }

    @PostMapping(value = _endpointDirectory + "upsert", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<ChatRoomDTO>> upsert(@RequestBody ChatRoomDTO chatRoom) {
        return ChatRoomControllerService.get_instance().upsert(new ChatRoom(chatRoom));
    }
}