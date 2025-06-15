package laustrup.bandwichpersistence.items;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.Situation;
import laustrup.bandwichpersistence.core.models.ToStringArgument;
import laustrup.bandwichpersistence.core.persistence.Field;
import laustrup.bandwichpersistence.core.persistence.models.Query;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Properties;
import laustrup.bandwichpersistence.core.persistence.services.SelectService.Selecting.Where.Whereing.Thating;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService.ResultSetService.Configurations;
import laustrup.bandwichpersistence.core.utilities.Coollection;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import laustrup.bandwichpersistence.core.utilities.parameters.Truthiness;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import static laustrup.bandwichpersistence.core.persistence.DatabaseManager.read;
import static laustrup.bandwichpersistence.core.persistence.services.SelectService.selecting;
import static laustrup.bandwichpersistence.core.services.StringService.randomString;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.ResultSetService.get;

public class TestItems {

    private static Query selectQuery(String table, String title) {
        return new Query(String.format(/*language=mysql*/ """
                select * from %s
                     inner join contact_info contactInfo
                         on %s.contact_info_id = contactInfo.id
                     inner join addresses
                         on contactInfo.address_id = addresses.id
                     inner join countries
                         on contactInfo.country_id = countries.id
                     inner join phones
                         on contactInfo.id = phones.contact_info_id
                     left join organisation_venues organisationVenues
                         on %s.id = organisationVenues.organisation_id
                     left join venues
                         on organisationVenues.venue_id = venues.id
                     left join organisation_employments organisationEmployments
                         on %s.id = organisationEmployments.organisation_id
                     left join organisation_employees organisationEmployees
                         on organisationEmployments.organisation_employee_id = organisationEmployees.id
                where
                    %s.title = '%s'
                """,
                table, table, table, table, table,
                title
        ));
    }

    private static Query selectOrganizationQuery(String title) {
        return selectQuery("organisations", title);
    }

    public static ResultSet generateResultSet() {
        return read(selectOrganizationQuery(OrganisationTestItems.OrganisationTitle.ARENA.get_naming())).get_resultSet();
    }

    public static UUID generateUUID(String table, Thating that) {
        return get(new Configurations(
                Field.of(table, "id"), read(
                        new Query(selecting(new Properties(table, that)).select())
                ).get_resultSet()),
                UUID.class
        );
    }

    public static UUID generateUUID(String table, Selecting selecting) {
        return get(new Configurations(
                        Field.of(table, "id"), read(
                        new Query(selecting.select())
                ).get_resultSet()),
                UUID.class
        );
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Instance {

        private UUID _id;

        private String _title;

        private boolean _active;

        private int _amount;

        public Instance(UUID id) {
            this(id, randomString(), true, randomAmount());
        }

        public Instance(String title) {
            this(title, 0);
        }

        public Instance(String title, int amount) {
            this(UUID.randomUUID(), title, true, amount);
        }

        public static Instance initialise() {
            return new Instance(randomString(), randomAmount());
        }

        public static Instance initialise(Instance instance) {
            return instance == null ? initialise() : new Instance(instance.get_id());
        }

        public boolean isSameAs(Instance instance) {
            return hasTruthinessOf(instance).is_totallyTrue();
        }

        public Truthiness hasTruthinessOf(Instance instance) {
            Truthiness truthiness = new Truthiness();

            if (instance.get_id().equals(_id)) {
                AtomicInteger
                        equals = new AtomicInteger(),
                        count = new AtomicInteger();

                BiConsumer<Object, Object> compare = (field, comparison) -> {
                    boolean isEqual = field == comparison;
                    if (isEqual)
                        equals.incrementAndGet();
                    count.incrementAndGet();
                };

                compare.accept(_title, instance.get_title());
                compare.accept(_active, instance.is_active());
                compare.accept(_amount, instance.get_amount());

                truthiness.set_argument(BigDecimal.valueOf(count.get() / equals.get()));
            }

            return truthiness;
        }

        public static Model toModel(Instance instance) {
            return new Model() {
                @Override
                public UUID get_id() {
                    return instance.get_id();
                }

                @Override
                public String get_title() {
                    return instance.get_title();
                }

                @Override
                public Instant get_timestamp() {
                    return Instant.now();
                }

                @Override
                public Situation get_situation() {
                    return super.get_situation();
                }

                @Override
                public String toString() {
                    return super.toString();
                }

                @Override
                public void set_title(String _title) {
                    super.set_title(_title);
                }

                @Override
                public Situation set_situation(Situation situation) {
                    return super.set_situation(situation);
                }

                @Override
                protected String defineToString(String title, Coollection<ToStringArgument> arguments) {
                    return super.defineToString(title, arguments);
                }

                @Override
                protected String defineToString(String title, String[][] values) {
                    return super.defineToString(title, values);
                }

                @Override
                protected String defineToString(String title, String[] keys, String[] values) {
                    return super.defineToString(title, keys, values);
                }
            };
        }

        private static int randomAmount() {
            return new Random().nextInt(100);
        }
    }

    public record InstanceCollection(Seszt<Instance> collection, Instance entity) {}
    public record Instances(Instance expected, Instance actual) {}
}
