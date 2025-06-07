package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.Organisation.Employee.Role;
import laustrup.bandwichpersistence.core.models.User.Authority;
import laustrup.bandwichpersistence.core.models.User.Participation;
import laustrup.bandwichpersistence.core.models.User.UserDTO;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.models.Model.ModelDTO.*;
import static laustrup.bandwichpersistence.core.models.Organisation.Employee;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class OrganisationEmployeeBuilder extends BuilderService<Employee> {

    private static final Logger _logger = Logger.getLogger(OrganisationEmployeeBuilder.class.getSimpleName());

    private static OrganisationEmployeeBuilder _instance;

    private static final ChatRoomBuilder _chatRoomBuilder = ChatRoomBuilder.get_instance();

    private static final ContactInfoBuilder _contactInfoBuilder = ContactInfoBuilder.get_instance();

    private static final SubscriptionBuilder _subscriptionBuilder = SubscriptionBuilder.get_instance();

    private static final HistoryBuilder _historyBuilder = HistoryBuilder.get_instance();

    public static OrganisationEmployeeBuilder get_instance() {
        if (_instance == null)
            _instance = new OrganisationEmployeeBuilder();

        return _instance;
    }

    private OrganisationEmployeeBuilder() {
        super(
                Employee.class,
                classToTableName(Organisation.class, Employee.class),
                _logger
        );
    }

    @Override
    protected void completion(Employee collective, Employee part) {
        combine(collective.get_roles(), part.get_roles());
        combine(collective.get_authorities(), part.get_authorities());
        combine(collective.get_chatRooms(), part.get_chatRooms());
        combine(collective.get_participations(), part.get_participations());
    }

    @Override
    protected Function<Function<String, Field>, Employee> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID> id = new AtomicReference<>();
            AtomicReference<String>
                    username = new AtomicReference<>(),
                    firstName = new AtomicReference<>(),
                    lastName = new AtomicReference<>(),
                    description = new AtomicReference<>();
            AtomicReference<ContactInfo> contactInfo = new AtomicReference<>();
            AtomicReference<Subscription> subscription = new AtomicReference<>();
            Seszt<Role> roles = new Seszt<>();
            Seszt<Authority> authorities = new Seszt<>();
            Seszt<ChatRoom> chatRooms = new Seszt<>();
            Seszt<Participation> participations = new Seszt<>();
            AtomicReference<History> history = new AtomicReference<>(new History(History.JoinTableDetails.ORGANISATION_EMPLOYEE));
            AtomicReference<Instant> timestamp = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Fields.id));
                        set(username, table.apply(UserDTO.Fields.username));
                        set(firstName, table.apply(UserDTO.Fields.firstName));
                        set(lastName, table.apply(UserDTO.Fields.lastName));
                        set(description, table.apply(UserDTO.Fields.description));
                        _contactInfoBuilder.complete(contactInfo, resultSet);
                        _subscriptionBuilder.complete(subscription, resultSet);
                        add(roles, Field.of(
                                Organisation.class.getSimpleName() + "Employments",
                                Employee.DTO.Fields.roles.replace("s", ""))
                        );
                        add(authorities, Field.of(UserDTO.Fields.authorities, "level"));
                        combine(chatRooms, _chatRoomBuilder.build(resultSet));
                        combine(history.get().get_stories(), _historyBuilder.buildStory(resultSet, history.get()));
                        timestamp.set(getTimestamp(Fields.timestamp, Timestamp::toInstant));
                    },
                    id
            );

            return new Employee(
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
        };
    }
}
