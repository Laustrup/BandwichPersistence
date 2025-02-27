package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.Request;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import lombok.Getter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class Organisation extends Model {

    private Seszt<Request> _requests;

    private ContactInfo _contactInfo;

    private Seszt<ChatRoom.Template> _chatRoomTemplates;

    private Seszt<Album> _albums;

    private Seszt<Venue> _venues;

    private Seszt<Employee> _employees;

    public Organisation(DTO organisation) {
        this(
                organisation.getId(),
                organisation.getTitle(),
                new Seszt<>(organisation.get_requests().stream().map(Request::new)),
                new ContactInfo(organisation.get_contactInfo()),
                new Seszt<>(organisation.get_chatRoomTemplates().stream().map(ChatRoom.Template::new)),
                new Seszt<>(organisation.get_albums().stream().map(Album::new)),
                new Seszt<>(organisation.get_venues().stream().map(Venue::new)),
                new Seszt<>(organisation.get_employees().stream().map(Employee::new)),
                organisation.getHistory(),
                organisation.getTimestamp()
        );
    }

    public Organisation(
            UUID id,
            String title,
            Seszt<Request> requests,
            ContactInfo contactInfo,
            Seszt<ChatRoom.Template> chatRoomTemplates,
            Seszt<Album> albums,
            Seszt<Venue> venues,
            Seszt<Employee> employees,
            History history,
            Instant timestamp
    ) {
        super(id, title, history, timestamp);
        _requests = requests;
        _contactInfo = contactInfo;
        _chatRoomTemplates = chatRoomTemplates;
        _albums = albums;
        _venues = venues;
        _employees = employees;
    }

    @Getter
    public static class DTO extends ModelDTO {

        private Set<Request.DTO> _requests;

        private ContactInfo.DTO _contactInfo;

        private Set<ChatRoom.Template.DTO> _chatRoomTemplates;

        private Set<Album.DTO> _albums;

        private Set<Venue.DTO> _venues;

        private Set<Employee.DTO> _employees;

        public DTO(Organisation organisation) {
            super(organisation);
            _requests = organisation.get_requests().stream()
                    .map(Request.DTO::new)
                    .collect(Collectors.toSet());
            _contactInfo = new ContactInfo.DTO(organisation.get_contactInfo());
            _chatRoomTemplates = organisation.get_chatRoomTemplates().stream()
                    .map(ChatRoom.Template.DTO::new)
                    .collect(Collectors.toSet());
            _albums = organisation.get_albums().stream()
                    .map(Album.DTO::new)
                    .collect(Collectors.toSet());
            _venues = organisation.get_venues().stream()
                    .map(Venue.DTO::new)
                    .collect(Collectors.toSet());
            _employees = organisation.get_employees().stream()
                    .map(Employee.DTO::new)
                    .collect(Collectors.toSet());
        }
    }

    @Getter
    public static class Employee extends BusinessUser {

        private Seszt<Role> _roles;

        public Employee(DTO employee) {
            this(
                    employee.getId(),
                    employee.getUsername(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getDescription(),
                    new ContactInfo(employee.getContactInfo()),
                    new Subscription(employee.getSubscription()),
                    new Seszt<>(employee.getRoles().stream()),
                    new Seszt<>(employee.getAuthorities().stream()),
                    new Seszt<>(employee.getChatRooms().stream().map(ChatRoom::new)),
                    new Seszt<>(employee.getParticipations().stream().map(Participation::new)),
                    employee.getHistory(),
                    employee.getTimestamp()
            );
        }

        public Employee(
                UUID id,
                String username,
                String firstName,
                String lastName,
                String description,
                ContactInfo contactInfo,
                Subscription subscription,
                Seszt<Role> roles,
                Seszt<Authority> authorities,
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
                    subscription,
                    authorities,
                    chatRooms,
                    participations,
                    history,
                    timestamp
            );
            _roles = roles;
        }

        public enum Role {
            BOOKER,
            PR,
            LEADER
        }

        @Getter
        public static class DTO extends BusinessUserDTO {

            private Set<Role> roles;

            public DTO(Organisation.Employee employee) {
                super(employee);
                roles = new HashSet<>(employee.get_roles());
            }
        }
    }
}
