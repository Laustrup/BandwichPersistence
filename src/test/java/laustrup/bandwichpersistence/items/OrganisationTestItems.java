package laustrup.bandwichpersistence.items;

import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;

import java.time.Instant;

import static java.util.UUID.randomUUID;
import static laustrup.bandwichpersistence.items.ContactInfoTestItems.*;
import static laustrup.bandwichpersistence.items.OrganisationEmployeeTestItems.OrganisationEmployeeTitle.JENS_JENSEN;
import static laustrup.bandwichpersistence.items.OrganisationEmployeeTestItems.generateOrganisationEmployee;

public class OrganisationTestItems {

    public static Organisation generate(OrganisationTitle title)
            throws NotImplementedException {
        return switch (title) {
            case IVÆRKSTED -> generateIværksted();
            case ARENA -> throw new NotImplementedException("Arena not yet implemented");
            case TWOGETHER -> throw new NotImplementedException("Twogether not yet implemented");
            case JAMSTER -> throw new NotImplementedException("Jamster not yet implemented");
            case null -> null;
        };
    }

    private static Organisation generateIværksted() throws NotImplementedException {
        return new Organisation(
                randomUUID(),
                "Iværksted",
                new Seszt<>(),
                new Seszt<>(),
                new Seszt<>(),
                generateIværkstedContactInfo("contact@ivaerkstedet.dk"),
                new Seszt<>(),
                new Seszt<>(),
                new Seszt<>(
                    generateOrganisationEmployee(JENS_JENSEN)
                ),
                Instant.now()
        );
    }

    public static ContactInfo generateIværkstedContactInfo(String email) throws NotImplementedException {
        return generateContactInfo(
                email,
                new Seszt<>(
                        generatePhone(
                                45,
                                12345678,
                                true,
                                true
                        )
                ),
                generateAddress(
                        "Værkstedsvej 57",
                        null,
                        "Sjælland",
                        "4600",
                        "Køge"
                ),
                generateCountry(
                        "Denmark",
                        "DK"
                )
        );
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
}
