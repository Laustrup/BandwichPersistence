package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.models.Rating;
import laustrup.bandwichpersistence.core.models.Venue;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.get;

public class RatingBuilder extends BuilderService<Rating> {

    private final Service _service = new Service();

    public RatingBuilder() {
        super(Rating.class, RatingBuilder.class);
    }

    @Override
    protected void completion(Rating reference, Rating object) {

    }

    @Override
    protected Function<Function<String, JDBCService.Field>, Rating> logic(ResultSet resultSet) {
        return table -> _service.generateLogic(resultSet, Service.Implementation.STANDARD, table);
    }

    public static class Service {

        private final OrganisationBuilder _organisationBuilder = new OrganisationBuilder();

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
                                _organisationBuilder.complete(organisation, resultSet);
                            timestamp.set(getInstant(Rating.DTO.Fields.timestamp));
                        },
                        primary -> !get(
                                JDBCService::getUUID,
                                Rating.DTO.Fields.appointedId
                        ).equals(primary) || !get(
                                JDBCService::getUUID,
                                Rating.DTO.Fields.reviewerId
                        ).equals(primary),
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
