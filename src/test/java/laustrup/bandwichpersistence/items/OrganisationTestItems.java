package laustrup.bandwichpersistence.items;

import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;

import java.time.Instant;

import static java.util.UUID.randomUUID;
import static laustrup.bandwichpersistence.items.ContactInfoTestItems.generateContactInfo;
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
        new Organisation.Id(randomUUID()),
        "Iværksted",
        new Seszt<>(),
        new Seszt<>(),
        new Seszt<>(),
        generateContactInfo(OrganisationTitle.IVÆRKSTED),
        new Seszt<>(),
        new Seszt<>(),
        new Seszt<>(generateOrganisationEmployee(JENS_JENSEN)),
        Instant.now()
    );
  }

  @Getter
  public enum OrganisationTitle {
    IVÆRKSTED("Iværksted"),
    ARENA("Arena"),
    TWOGETHER("Twogether"),
    JAMSTER("Jamster");

    private final String _naming;

    OrganisationTitle(String naming) {
      _naming = naming;
    }
  }
}
