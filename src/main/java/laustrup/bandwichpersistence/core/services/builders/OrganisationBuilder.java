package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.Request;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.builders.UserBuilder.*;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class OrganisationBuilder {

    private static final Logger _logger = Logger.getLogger(OrganisationBuilder.class.getSimpleName());

    public static Organisation build(ResultSet resultSet) {
        AtomicReference<UUID> id = new AtomicReference<>();
        AtomicReference<String> title = new AtomicReference<>();
        Seszt<Event> events = new Seszt<>();
        Seszt<Venue> venues = new Seszt<>();
        Seszt<Request> requests = new Seszt<>();
        AtomicReference<ContactInfo> contactInfo = new AtomicReference<>();
        Seszt<ChatRoom.Template> chatRoomTemplates = new Seszt<>();
        Seszt<Album> albums = new Seszt<>();
        Seszt<Organisation.Employee> employees = new Seszt<>();
        AtomicReference<Instant> timestamp = new AtomicReference<>();

        try {
            JDBCService.build(
                    resultSet,
                    () -> {
                        id.set(getUUID(Model.ModelDTO.Fields.id));
                        title.set(getString(Model.ModelDTO.Fields.title));
                        events.add(EventBuilder.build(resultSet));
                        venues.add(VenueBuilder.build(resultSet));
                        requests.add(RequestBuilder.build(resultSet));
                        contactInfo.set(UserBuilder.buildContactInfo(resultSet));
                        chatRoomTemplates.add(ChatRoomBuilder.buildTemplate(resultSet));
                        albums.add(AlbumBuilder.build(resultSet));
                        employees.add(buildEmployee(resultSet));
                        timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                    },
                    primary -> !get(
                            JDBCService::getUUID,
                            Model.ModelDTO.Fields.id
                    ).equals(primary),
                    id.get()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new Organisation(
                id.get(),
                title.get(),
                events,
                venues,
                requests,
                contactInfo.get(),
                chatRoomTemplates,
                albums,
                employees,
                timestamp.get()
        );
    }

    public static Organisation.Employee buildEmployee(ResultSet resultset) {
        AtomicReference<UUID> id = new AtomicReference<>();
        AtomicReference<String>
                username = new AtomicReference<>(),
                firstName = new AtomicReference<>(),
                lastName = new AtomicReference<>(),
                description = new AtomicReference<>();
        AtomicReference<ContactInfo> contactInfo = new AtomicReference<>();
        AtomicReference<Subscription> subscription = new AtomicReference<>();
        Seszt<Organisation.Employee.Role> roles = new Seszt<>();
        Seszt<User.Authority> authorities = new Seszt<>();
        Seszt<ChatRoom> chatRooms = new Seszt<>();
        Seszt<User.Participation> participations = new Seszt<>();
        AtomicReference<History> history = new AtomicReference<>(new History(History.JoinTableDetails.ORGANISATION_EMPLOYEE));
        AtomicReference<Instant> timestamp = new AtomicReference<>();

        try {
            JDBCService.build(
                    resultset,
                    () -> {
                        id.set(get(
                                JDBCService::getUUID,
                                Model.ModelDTO.Fields.id
                        ));
                        username.set(get(
                                JDBCService::getString,
                                User.UserDTO.Fields.username
                        ));
                        firstName.set(get(
                                JDBCService::getString,
                                User.UserDTO.Fields.username
                        ));
                        lastName.set(get(
                                JDBCService::getString,
                                User.UserDTO.Fields.lastName
                        ));
                        description.set(get(
                                JDBCService::getString,
                                User.UserDTO.Fields.description
                        ));
                        contactInfo.set(buildContactInfo(resultset));
                        subscription.set(buildSubscription(resultset));
                        roles.add(Organisation.Employee.Role.valueOf(get(
                                JDBCService::getString,
                                Organisation.Employee.DTO.Fields.roles
                        )));
                        authorities.add(User.Authority.valueOf(get(
                                JDBCService::getString,
                                User.UserDTO.Fields.authorities
                        )));
                        chatRooms.add(ChatRoomBuilder.build(resultset));
                        history.get().get_stories().add(HistoryBuilder.buildStory(resultset));
                        timestamp.set(get(
                                column -> getTimestamp(column, Timestamp::toInstant),
                                Model.ModelDTO.Fields.timestamp
                        ));
                    },
                    primary -> !get(
                            JDBCService::getUUID,
                            Model.ModelDTO.Fields.id
                    ).equals(primary),
                    id.get()
            );
        } catch (SQLException e) {
            _logger.warning(String.format(
                    "Could not build organisation of %s:\n%s",
                    id.get(),
                    e.getMessage()
            ));
            return null;
        }

        return new Organisation.Employee(
                id.get(),
                username.get(),
                firstName.get(),
                lastName.get(),
                description.get(),
                contactInfo.get(),
                subscription.get(),
                roles,
                authorities,
                chatRooms,
                participations,
                history.get(),
                timestamp.get()
        );
    }
}
