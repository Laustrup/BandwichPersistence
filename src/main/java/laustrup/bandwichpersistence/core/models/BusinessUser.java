package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import lombok.Getter;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public abstract class BusinessUser extends User {

    private Seszt<ChatRoom> _chatRooms;

    public BusinessUser(BusinessUserDTO user) {
        super(user);
        _chatRooms = new Seszt<>(user.getChatRooms().stream().map(ChatRoom::new));
    }

    public BusinessUser(
            UUID id,
            String username,
            String firstName,
            String lastName,
            String description,
            ContactInfo contactInfo,
            Seszt<Event> events,
            Subscription subscription,
            Seszt<Authority> authorities,
            Seszt<ChatRoom> chatRooms,
            History history,
            Instant timestamp
    ) {
        super(
                id,
                username,
                firstName,
                lastName,
                description,
                contactInfo,
                events,
                subscription,
                authorities,
                history,
                timestamp
        );
        _chatRooms = chatRooms;
    }

    @Getter
    public abstract static class BusinessUserDTO extends UserDTO {

        private Set<ChatRoom.DTO> chatRooms;

        public BusinessUserDTO(BusinessUser businessUser) {
            super(businessUser);
            chatRooms = Arrays.stream(businessUser.get_chatRooms().get_data())
                    .map(ChatRoom.DTO::new)
                    .collect(Collectors.toSet());
        }
    }
}
