package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class OrganisationEmployeeBuilder extends BuilderService<Organisation.Employee> {

    private final ContactInfoBuilder _contactInfoBuilder = new ContactInfoBuilder();

    private final SubscriptionBuilder _subscriptionBuilder = new SubscriptionBuilder();

    private final ChatRoomBuilder _chatRoomBuilder = new ChatRoomBuilder();

    protected OrganisationEmployeeBuilder() {
        super(Organisation.Employee.class, OrganisationEmployeeBuilder.class);
    }

    @Override
    protected void completion(Organisation.Employee collective, Organisation.Employee part) {
        combine(collective.get_roles(), part.get_roles());
        combine(collective.get_authorities(), part.get_authorities());
        combine(collective.get_chatRooms(), part.get_chatRooms());
        combine(collective.get_participations(), part.get_participations());
    }

    @Override
    protected Function<Function<String, Field>, Organisation.Employee> logic(ResultSet resultSet) {
        return table -> {
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

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Model.ModelDTO.Fields.id));
                        set(username, table.apply(User.UserDTO.Fields.username));
                        set(firstName, table.apply(User.UserDTO.Fields.firstName));
                        set(lastName, table.apply(User.UserDTO.Fields.lastName));
                        set(description, table.apply(User.UserDTO.Fields.description));
                        _contactInfoBuilder.complete(contactInfo, resultSet);
                        _subscriptionBuilder.complete(subscription, resultSet);
                        add(roles, Field.of(
                                Organisation.class.getSimpleName() + "Employments",
                                Organisation.Employee.DTO.Fields.roles.replace("s", ""))
                        );
                        add(authorities, Field.of(User.UserDTO.Fields.authorities, "level"));
                        combine(chatRooms, _chatRoomBuilder.build(resultSet));
                        combine(history.get().get_stories(), HistoryBuilder.buildStory(resultSet, history.get()));
                        timestamp.set(getTimestamp(Model.ModelDTO.Fields.timestamp, Timestamp::toInstant));
                    },
                    id
            );

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
        };
    }
}
