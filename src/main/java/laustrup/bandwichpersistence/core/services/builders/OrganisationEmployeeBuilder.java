package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Organisation.Employee;

public class OrganisationEmployeeBuilder extends BandwichBuilderService<Employee> {

  private static OrganisationEmployeeBuilder _instance;

  public static OrganisationEmployeeBuilder get_instance() {
    if (_instance == null)
      _instance = new OrganisationEmployeeBuilder();

    return _instance;
  }

  private OrganisationEmployeeBuilder() {

  }

  @Override
  protected void completion(Employee collective, Employee part) {
    combine(collective.get_roles(), part.get_roles());
    combine(collective.get_authorities(), part.get_authorities());
    combine(collective.get_chatRooms(), part.get_chatRooms());
    combine(collective.get_participations(), part.get_participations());
  }
}
