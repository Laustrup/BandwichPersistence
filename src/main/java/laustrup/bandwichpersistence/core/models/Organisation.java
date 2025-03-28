package laustrup.bandwichpersistence.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.Request;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt.copy;

@Getter
public class Organisation extends Model {

    private Seszt<Request> _requests;

    private ContactInfo _contactInfo;

    private Seszt<Event> _events;

    private Seszt<Venue> _venues;

    private Seszt<ChatRoom.Template> _chatRoomTemplates;

    private Seszt<Album> _albums;

    private Seszt<Employee> _employees;

    public Organisation(DTO organisation) {
        this(
                organisation.getId(),
                organisation.getTitle(),
                copy(organisation.getEvents(),Event::new),
                copy(organisation.getVenues(), Venue::new),
                copy(organisation.getRequests(),Request::new),
                new ContactInfo(organisation.getContactInfo()),
                copy(organisation.getChatRoomTemplates(),ChatRoom.Template::new),
                copy(organisation.getAlbums(),Album::new),
                copy(organisation.getEmployees(),Employee::new),
                organisation.getTimestamp()
        );
    }

    public Organisation(
            UUID id,
            String title,
            Seszt<Event> events,
            Seszt<Venue> venues,
            Seszt<Request> requests,
            ContactInfo contactInfo,
            Seszt<ChatRoom.Template> chatRoomTemplates,
            Seszt<Album> albums,
            Seszt<Employee> employees,
            Instant timestamp
    ) {
        super(id, title, timestamp);
        _requests = requests;
        _contactInfo = contactInfo;
        _events = events;
        _venues = venues;
        _chatRoomTemplates = chatRoomTemplates;
        _albums = albums;
        _employees = employees;
    }

    @Getter @FieldNameConstants
    public static class DTO extends ModelDTO {

        private Set<Request.DTO> requests;

        private ContactInfo.DTO contactInfo;

        private Set<Event.DTO> events;

        private Set<Venue.DTO> venues;

        private Set<ChatRoom.Template.DTO> chatRoomTemplates;

        private Set<Album.DTO> albums;

        private Set<Employee.DTO> employees;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public DTO(
                @JsonProperty UUID id,
                @JsonProperty String title,
                @JsonProperty Instant timestamp,
                @JsonProperty Set<Request.DTO> requests,
                @JsonProperty ContactInfo.DTO contactInfo,
                @JsonProperty Set<Event.DTO> events,
                @JsonProperty Set<Venue.DTO> venues,
                @JsonProperty Set<ChatRoom.Template.DTO> chatRoomTemplates,
                @JsonProperty Set<Album.DTO> albums,
                @JsonProperty Set<Employee.DTO> employees
        ) {
            super(id, title, timestamp);
            this.requests = requests;
            this.contactInfo = contactInfo;
            this.events = events;
            this.venues = venues;
            this.chatRoomTemplates = chatRoomTemplates;
            this.albums = albums;
            this.employees = employees;
        }

        public DTO(Organisation organisation) {
            super(organisation);
            requests = organisation.get_requests().asSet(Request.DTO::new);
            contactInfo = new ContactInfo.DTO(organisation.get_contactInfo());
            events = organisation.get_events().asSet(Event.DTO::new);
            venues = organisation.get_venues().asSet(Venue.DTO::new);
            chatRoomTemplates = organisation.get_chatRoomTemplates().asSet(ChatRoom.Template.DTO::new);
            albums = organisation.get_albums().asSet(Album.DTO::new);
            employees = organisation.get_employees().asSet(Employee.DTO::new);
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

        @Getter @FieldNameConstants
        public static class DTO extends BusinessUserDTO {

            private Set<Role> roles;

            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            public DTO(
                    @JsonProperty UUID id,
                    @JsonProperty String username,
                    @JsonProperty String firstName,
                    @JsonProperty String lastName,
                    @JsonProperty String description,
                    @JsonProperty ContactInfo.DTO contactInfo,
                    @JsonProperty Set<Participation.DTO> participations,
                    @JsonProperty Subscription.DTO subscription,
                    @JsonProperty Set<ChatRoom.DTO> chatRooms,
                    @JsonProperty Set<Authority> authorities,
                    @JsonProperty History history,
                    @JsonProperty Instant timestamp,
                    @JsonProperty Set<Role> roles
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
                        chatRooms,
                        authorities,
                        history,
                        timestamp
                );
                this.roles = roles;
            }

            public DTO(Organisation.Employee employee) {
                super(employee);
                roles = new HashSet<>(employee.get_roles());
            }
        }
    }
}
