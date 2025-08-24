package laustrup.bandwichpersistence.core.models.users;

import laustrup.bandwichpersistence.core.models.DatabaseTable;
import laustrup.bandwichpersistence.core.models.History;
import laustrup.bandwichpersistence.core.models.Subscription;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.services.DatabaseTableAnnotationService;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter @FieldNameConstants
public abstract class BusinessUser<IDENTITY extends User.Id> extends User<IDENTITY> {

    private Seszt<ChatRoom> _chatRooms;

    public BusinessUser(BusinessUserDTO<IDENTITY> user, IDENTITY identity) {
        super(user, identity);
        _chatRooms = new Seszt<>(user.getChatRooms().stream().map(ChatRoom::new));
    }

    public BusinessUser(
            IDENTITY id,
            String username,
            String firstName,
            String lastName,
            String description,
            ContactInfo contactInfo,
            Subscription subscription,
            Seszt<ChatRoom> chatRooms,
            Seszt<Participation> participations,
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
                participations,
                subscription,
                history,
                timestamp
        );
        _chatRooms = chatRooms;
    }

    @Getter @FieldNameConstants
    public abstract static class BusinessUserDTO<IDENTITY extends User.Id> extends UserDTO<IDENTITY> {

        private Set<ChatRoom.DTO> chatRooms;

        public BusinessUserDTO(
                UUID id,
                String username,
                String firstName,
                String lastName,
                String description,
                ContactInfo.DTO contactInfo,
                Set<Participation.DTO> participations,
                Subscription.DTO subscription,
                Set<ChatRoom.DTO> chatRooms,
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
                    participations,
                    subscription,
                    history,
                    timestamp
            );
            this.chatRooms = chatRooms;
        }

        public BusinessUserDTO(BusinessUser<IDENTITY> businessUser) {
            super(businessUser);
            chatRooms = Arrays.stream(businessUser.get_chatRooms().get_data())
                    .map(ChatRoom.DTO::new)
                    .collect(Collectors.toSet());
        }
    }
}
