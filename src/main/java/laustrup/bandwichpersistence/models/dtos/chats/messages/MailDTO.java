package laustrup.bandwichpersistence.models.dtos.chats.messages;

import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.dtos.chats.ChatRoomDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @Data
public class MailDTO extends MessageDTO {

    private ChatRoomDTO chatRoom;

    public MailDTO(Mail mail) {
        super(mail.get_primaryId(), mail.get_author(), mail.get_content(),
                mail.is_sent(), mail.get_edited(), mail.is_public(), mail.get_timestamp());
        chatRoom = new ChatRoomDTO(mail.get_chatRoom());
    }
}
