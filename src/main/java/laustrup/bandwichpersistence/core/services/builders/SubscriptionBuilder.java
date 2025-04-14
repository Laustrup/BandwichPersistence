package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.Subscription;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService.Field;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.getString;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.set;

public class SubscriptionBuilder extends BuilderService<Subscription> {

    private static final Logger _logger = Logger.getLogger(SubscriptionBuilder.class.getName());

    private static SubscriptionBuilder _instance;

    public static SubscriptionBuilder get_instance() {
        if (_instance == null)
            _instance = new SubscriptionBuilder();

        return _instance;
    }

    private SubscriptionBuilder() {
        super(Subscription.class, _logger);
    }

    @Override
    protected void completion(Subscription reference, Subscription object) {

    }

    @Override
    protected Function<Function<String, Field>, Subscription> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID> id = new AtomicReference<>();
            AtomicReference<Subscription.Status> status = new AtomicReference<>();
            AtomicReference<Subscription.Kind> kind = new AtomicReference<>();
            AtomicReference<Subscription.UserType> userType = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Model.ModelDTO.Fields.id));
                        status.set(Subscription.Status.valueOf(getString(Subscription.DTO.Fields.status)));
                        kind.set(Subscription.Kind.valueOf(getString(Subscription.DTO.Fields.kind)));
                        userType.set(Subscription.UserType.valueOf(getString(Subscription.DTO.Fields.userType)));
                    },
                    id
            );

            return new Subscription(
                    id.get(),
                    status.get(),
                    kind.get(),
                    userType.get()
            );
        };
    }
}
