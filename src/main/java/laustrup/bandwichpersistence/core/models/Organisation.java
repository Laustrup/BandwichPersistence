package laustrup.bandwichpersistence.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.Request;
import laustrup.bandwichpersistence.core.models.identification.CommonIdentity;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.core.models.users.BusinessUser;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.DatabaseEntity;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.DatabaseJunction;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static laustrup.bandwichpersistence.core.utilities.collections.Seszt.copy;

@Getter @DatabaseEntity(value = "organisations") @FieldNameConstants
public class Organisation extends Model<Organisation.Id, Signature.UUID> {

    private Seszt<Request> _requests;

    private ContactInfo _contactInfo;

    private Seszt<Event> _events;

    private Seszt<Venue> _venues;

    private Seszt<ChatRoom.Template> _chatRoomTemplates;

    private Seszt<Album> _albums;

    private Seszt<Employee> _employees;

    public Organisation(DTO organisation) {
        this(
                new Id(organisation.getId()),
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
            Id id,
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

    public static class Id extends CommonIdentity<Signature.UUID> {

        public Id(Signature.UUID signature) {
            super(signature);
        }

        public Id(java.util.UUID signature) {
            super(new Signature.UUID(signature));
        }

        @Override
        public Class<?> getOwnerClassType() {
            return Organisation.class;
        }
    }

    @Getter @FieldNameConstants @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DTO extends ModelDTO<Organisation.Id, Signature.UUID, java.util.UUID> {

        private Set<Request.DTO> requests;

        private ContactInfo.DTO contactInfo;

        private Set<Event.DTO> events;

        private Set<Venue.DTO> venues;

        private Set<ChatRoom.Template.DTO> chatRoomTemplates;

        private Set<Album.DTO> albums;

        private Set<Employee.DTO> employees;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public DTO(
                @JsonProperty java.util.UUID id,
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
    @DatabaseEntity(value = "organisation_employees")
    @FieldNameConstants
    public static class Employee extends BusinessUser<Employee.Id> {

        private Seszt<Role> _roles;

        private Seszt<Authority> _authorities;

        public Employee(DTO employee) {
            this(
                    new Employee.Id(employee.getId()),
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
                Id id,
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
                    chatRooms,
                    participations,
                    history,
                    timestamp
            );
            _roles = roles;
            _authorities = authorities;
        }

        @DatabaseEntity(value = "organisation_employments")
        public enum Role {
            BOOKER,
            PR,
            LEADER
        }

        public static class Id extends BusinessUser.Id {

            public Id(Signature.UUID identifier) {
                super(identifier);
            }

            public Id(java.util.UUID identifier) {
                super(identifier);
            }

            @Override
            public Class<Organisation.Employee> getOwnerClassType() {
                return Organisation.Employee.class;
            }
        }

        @DatabaseJunction(
                title = "organisation_employee_authorities",
                entityColumns = {
                        @DatabaseEntity.Column(value = "organisation_employee_id"),
                        @DatabaseEntity.Column(value = "authority_id")
                }
        )
        public enum Authority implements laustrup.bandwichpersistence.core.models.identification.Authority {
            STANDARD,
            ADMIN
        }

        @Getter @FieldNameConstants
        public static class DTO extends BusinessUserDTO<Employee.Id> {

            private Set<Role> roles;

            private Set<Authority> authorities;

            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            public DTO(
                    @JsonProperty java.util.UUID id,
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
                        history,
                        timestamp
                );
                this.roles = roles;
                this.authorities = authorities;
            }

            public DTO(Organisation.Employee employee) {
                super(employee);
                roles = new HashSet<>(employee.get_roles());
            }
        }
    }
}
