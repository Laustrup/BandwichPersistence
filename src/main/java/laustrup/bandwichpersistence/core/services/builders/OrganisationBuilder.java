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

public class OrganisationBuilder extends BuilderService {

    private static final Logger _logger = Logger.getLogger(OrganisationBuilder.class.getSimpleName());

    public static Organisation build(ResultSet resultSet) {
        Class<Organisation> clazz = Organisation.class;

        return handle(
                toTableName(clazz),
                field -> {
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
                                    set(id, field.apply(Model.ModelDTO.Fields.id));
                                    set(title, field.apply(getString(Model.ModelDTO.Fields.title)));
                                    events.add(EventBuilder.build(resultSet));
                                    venues.add(VenueBuilder.build(resultSet));
                                    requests.add(RequestBuilder.build(resultSet));
                                    contactInfo.set(UserBuilder.buildContactInfo(resultSet));
                                    chatRoomTemplates.add(ChatRoomBuilder.buildTemplate(resultSet));
                                    albums.add(AlbumBuilder.build(resultSet));
                                    employees.add(buildEmployee(resultSet));
                                    timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                                },
                                id.get()
                        );
                    } catch (SQLException exception) {
                        printError(clazz, id.get(), exception, _logger);
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
        );
    }

    public static Organisation.Employee buildEmployee(ResultSet resultset) {
        Class<Organisation.Employee> clazz = Organisation.Employee.class;

        return handle(
                toTableName(Organisation.class.getSimpleName() + clazz.getSimpleName()),
                field -> {
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
                                    id.set(get(field.apply(Model.ModelDTO.Fields.id), UUID.class));
                                    set(username, field.apply(User.UserDTO.Fields.username));
                                    set(firstName, field.apply(User.UserDTO.Fields.firstName));
                                    set(lastName, field.apply(User.UserDTO.Fields.lastName));
                                    set(description, field.apply(User.UserDTO.Fields.description));
                                    contactInfo.set(buildContactInfo(resultset));
                                    subscription.set(buildSubscription(resultset));
                                    add(roles, Field.of(
                                            Organisation.class.getSimpleName() + "Employments",
                                            Organisation.Employee.DTO.Fields.roles.replace("s", ""))
                                    );
                                    add(authorities, Field.of(User.UserDTO.Fields.authorities, "level"));
                                    chatRooms.add(ChatRoomBuilder.build(resultset));
                                    history.get().get_stories().add(HistoryBuilder.buildStory(resultset, history.get()));
                                    timestamp.set(getTimestamp(Model.ModelDTO.Fields.timestamp, Timestamp::toInstant));
                                },
                                id.get()
                        );
                    } catch (SQLException e) {
                        printError(clazz, id.get(), e, _logger);
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
        );
    }
}
