package laustrup.bandwichpersistence;

import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.persistence.models.Query;
import laustrup.bandwichpersistence.core.services.builders.OrganisationBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.util.UUID;

import static laustrup.bandwichpersistence.core.persistence.DatabaseManager.*;

public class TestItems {

    private static OrganisationBuilder _organisationBuilder = OrganisationBuilder.get_instance();

    private static Query generateQuery(String table, String title) {
        return new Query(String.format(/*language=mysql*/ """
                select * from %s
                     inner join contact_info
                         on %s.contact_info_id = contact_info.id
                     inner join addresses
                         on contact_info.address_id = addresses.id
                     inner join countries
                         on contact_info.country_id = countries.id
                     inner join phones
                         on contact_info.id = phones.contact_info_id
                     left join organisation_venues
                         on %s.id = organisation_venues.organisation_id
                     left join venues
                         on organisation_venues.venue_id = venues.id
                     left join organisation_employments
                         on %s.id = organisation_employments.organisation_id
                     left join organisation_employees
                         on contact_info.id = organisation_employees.contact_info_id
                where
                    %s.title = '%s'
                """,
                table, table, table, table, table,
                title
        ));
    }

    private static Query generateVenueQuery(String title) {
        return generateQuery("venues", title);
    }

    public static Organisation generateOrganisation(OrganisationTitle organisation) {
        return _organisationBuilder.build(
                read(generateVenueQuery(organisation.get_naming()))
                        .get_resultSet()
        );
    }

    public static ResultSet generateResultSet() {
        return read(generateVenueQuery(OrganisationTitle.ARENA.get_naming()))
                .get_resultSet();
    }

    @Getter
    public enum OrganisationTitle {
        IVÆRKSTED("Iværksted"),
        ARENA("Arena"),
        TWOGETHER("Twogether"),
        JAMSTER("Jamster");

        private String _naming;

        OrganisationTitle(String naming) {
            _naming = naming;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Instance {

        private UUID _id;

        private String _title;

        private boolean _active;

        private int _amount;

        public Instance(String title) {
            this(title, 0);
        }

        public Instance(String title, int amount) {
            this(UUID.randomUUID(), title, true, amount);
        }
    }
}
