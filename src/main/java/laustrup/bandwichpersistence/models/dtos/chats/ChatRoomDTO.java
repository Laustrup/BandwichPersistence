package laustrup.bandwichpersistence.models.dtos.chats;

import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.dtos.ModelDTO;
import laustrup.bandwichpersistence.models.dtos.chats.messages.MailDTO;
import laustrup.bandwichpersistence.models.dtos.users.UserDTO;
import laustrup.bandwichpersistence.services.DTOService;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is used for multiple Users to communicate with each other through Mails.
 */
@NoArgsConstructor @Data
public class ChatRoomDTO extends ModelDTO {

    /**
     * All the Mails that has been sent will be stored here.
     */
    private MailDTO[] mails;

    /**
     * The Users, except the responsible, that can write with each other.
     */
    private UserDTO[] chatters;

    /**
     * This responsible are being calculated for answeringTime.
     */
    private UserDTO responsible;

    /**
     * The amount of time it takes, before the responsible have answered the chatroom,
     * measured from the first message.
     * Is calculated in minutes.
     */
    private Long answeringTime;

    /**
     * Is true if the responsible has answered with a message.
     */
    private boolean answered;

    public ChatRoomDTO(ChatRoom chatRoom) {
        super(chatRoom.get_primaryId(),
                chatRoom.get_title().isEmpty() || chatRoom.get_title() == null ?
                        "ChatRoom-"+chatRoom.get_primaryId() : chatRoom.get_title(), chatRoom.get_timestamp());
        mails = new MailDTO[chatRoom.get_mails().size()];
        for (int i = 0; i < mails.length; i++)
            mails[i] = new MailDTO(chatRoom.get_mails().Get(i+1));
        chatters = new UserDTO[chatRoom.get_chatters().size()];
        for (int i = 0; i < chatters.length; i++)
            chatters[i] = DTOService.get_instance().convertToDTO(chatRoom.get_chatters().Get(i+1));
        responsible = DTOService.get_instance().convertToDTO(chatRoom.get_responsible());
        answered = chatRoom.is_answered();
    }
}
