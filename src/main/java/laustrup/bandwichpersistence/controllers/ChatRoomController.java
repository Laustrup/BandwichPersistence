package laustrup.bandwichpersistence.controllers;

import laustrup.bandwichpersistence.models.Response;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.dtos.chats.ChatRoomDTO;
import laustrup.bandwichpersistence.models.dtos.chats.messages.BulletinDTO;
import laustrup.bandwichpersistence.models.dtos.chats.messages.MailDTO;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.ChatRoomControllerService;

import laustrup.bandwichpersistence.utilities.Plato;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") @RestController
public class ChatRoomController {

    private final String _endpointDirectory = "/api/";
    private final String _chatRoomDirectory = _endpointDirectory + "chat_room/",
        _mailDirectory = _endpointDirectory + "mail/",
        _bulletinDirectory = _endpointDirectory + "bulletin/";

    @PutMapping(value = _mailDirectory + "upsert", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<ChatRoomDTO>> upsert(@RequestBody MailDTO mail) {
        return ChatRoomControllerService.get_instance().upsert(new Mail(mail));
    }

    @PostMapping(value = _chatRoomDirectory + "upsert", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<ChatRoomDTO>> upsert(@RequestBody ChatRoomDTO chatRoom) {
        return ChatRoomControllerService.get_instance().upsert(new ChatRoom(chatRoom));
    }

    @DeleteMapping(value = _chatRoomDirectory + "delete/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Plato.Argument>> deleteChatRoom(@PathVariable("id") long id) {
        return ChatRoomControllerService.get_instance().deleteChatRoom(id);
    }

    @DeleteMapping(value = _chatRoomDirectory + "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Plato.Argument>> deleteChatRoom(@RequestBody ChatRoomDTO chatRoom) {
        return ChatRoomControllerService.get_instance().deleteChatRoom(chatRoom.getPrimaryId());
    }

    @DeleteMapping(value = _bulletinDirectory + "/delete/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Plato.Argument>> deleteBulletin(@PathVariable("id") long id) {
        return ChatRoomControllerService.get_instance().deleteBulletin(id);
    }

    @DeleteMapping(value = _bulletinDirectory + "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Plato.Argument>> deleteBulletin(@RequestBody BulletinDTO bulletin) {
        return ChatRoomControllerService.get_instance().deleteBulletin(bulletin.getPrimaryId());
    }
}