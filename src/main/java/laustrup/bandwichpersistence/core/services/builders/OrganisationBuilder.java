package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Organisation;

public class OrganisationBuilder extends BuilderService<Organisation> {

  private static OrganisationBuilder _instance;

  public static OrganisationBuilder get_instance() {
    if (_instance == null)
      _instance = new OrganisationBuilder();

    return _instance;
  }

  private OrganisationBuilder() {

  }

  @Override
  protected void completion(Organisation collective, Organisation part) {
    combine(collective.get_events(), part.get_events());
    combine(collective.get_venues(), part.get_venues());
    combine(collective.get_requests(), part.get_requests());
    combine(collective.get_chatRoomTemplates(), part.get_chatRoomTemplates());
    combine(collective.get_albums(), part.get_albums());
    combine(collective.get_employees(), part.get_employees());
  }
}
