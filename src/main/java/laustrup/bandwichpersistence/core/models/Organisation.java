package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.Request;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class Organisation extends Model {

    private Seszt<Request> _requests;

    private ContactInfo _contactInfo;

    private Seszt<Album> _albums;

    private Seszt<Venue> _venues;

    private Seszt<Employee> _employees;

    public Organisation(
            UUID id,
            String title,
            History history,
            Instant timestamp,
            Seszt<Request> requests,
            ContactInfo contactInfo,
            Seszt<Album> albums,
            Seszt<Venue> venues,
            Seszt<Employee> employees
    ) {
        super(id, title, history, timestamp);
        _requests = requests;
        _contactInfo = contactInfo;
        _albums = albums;
        _venues = venues;
        _employees = employees;
    }

    public static class Employee extends BusinessUser {

        private Role _role;

        public Employee(
                UUID id,
                String username,
                String firstName,
                String lastName,
                String description,
                ContactInfo contactInfo,
                Seszt<Event> events,
                Subscription subscription,
                Role role,
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
                    chatRooms,
                    history,
                    timestamp
            );
            _role = role;
        }

        public enum Role {
            BOOKER,
            PR,
            LEADER
        }
    }
}
