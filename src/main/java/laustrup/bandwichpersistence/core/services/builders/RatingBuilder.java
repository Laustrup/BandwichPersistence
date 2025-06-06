package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.models.Rating;
import laustrup.bandwichpersistence.core.models.Venue;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class RatingBuilder extends BuilderService<Rating> {

    private static final Logger _logger = Logger.getLogger(RatingBuilder.class.getName());

    private static RatingBuilder _instance;

    public static RatingBuilder get_instance() {
        if (_instance == null)
            _instance = new RatingBuilder();

        return _instance;
    }

    private RatingBuilder() {
        super(Rating.class, _logger);
    }

    @Override
    protected void completion(Rating reference, Rating object) {

    }

    @Override
    protected Function<Function<String, Field>, Rating> logic(ResultSet resultSet) {
        return table -> Service.get_instance().generateLogic(resultSet, Service.Implementation.STANDARD, table);
    }

    public static class Service {

        private static Service _instance = new Service();

        public static Service get_instance() {
            if (_instance == null)
                _instance = new Service();

            return _instance;
        }

        private Service() {
        }

        public Rating generateLogic(ResultSet resultSet, Implementation implementation, Function<String, Field> table) {
            AtomicReference<Integer> value = new AtomicReference<>();
            AtomicReference<UUID>
                    appointedId = new AtomicReference<>(),
                    reviewerId = new AtomicReference<>();
            AtomicReference<String> comment = new AtomicReference<>();
            AtomicReference<Organisation> organisation = new AtomicReference<>();
            AtomicReference<Instant> timestamp = new AtomicReference<>();

            try {
                JDBCService.build(
                        resultSet,
                        () -> {
                            set(value, table.apply(Rating.DTO.Fields.value));
                            set(appointedId, table.apply(Rating.DTO.Fields.appointedId));
                            set(reviewerId, table.apply(Rating.DTO.Fields.reviewerId));
                            set(comment, table.apply(Rating.DTO.Fields.comment));
                            if (implementation == Implementation.VENUE)
                                OrganisationBuilder.get_instance().complete(organisation, resultSet);
                            timestamp.set(getInstant(Rating.DTO.Fields.timestamp));
                        },
                        primary -> !getUUID(Field.of("appointed_id")).equals(primary) ||
                                !getUUID(Field.of("reviewer_id")).equals(primary),
                        appointedId.get(),
                        reviewerId.get()
                );
            } catch (SQLException exception) {
                System.err.println(exception.getMessage());
            }

            return switch (implementation) {
                case VENUE -> new Venue.Rating(
                        value.get(),
                        appointedId.get(),
                        reviewerId.get(),
                        comment.get(),
                        organisation.get(),
                        timestamp.get()
                );
                case STANDARD -> new Rating(
                        value.get(),
                        appointedId.get(),
                        reviewerId.get(),
                        comment.get(),
                        timestamp.get()
                );
            };
        }

        public enum Implementation {
            STANDARD,
            VENUE
        }
    }
}
