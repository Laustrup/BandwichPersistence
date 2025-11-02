package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.Organisation.Employee;
import laustrup.bandwichpersistence.core.models.users.BusinessUser;
import laustrup.bandwichpersistence.core.models.users.User;

import java.util.UUID;

public class OrganisationEmployeeBuilder extends BuilderService<Employee> {

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

  @Override
  protected Employee construct() {
    return new Employee(
        new Employee.Id((UUID) get_field(Model.Fields._identity)),
        get_field(User.Fields._username),
        get_field(User.Fields._firstName),
        get_field(User.Fields._lastName),
        get_field(User.Fields._description),
        get_field(User.Fields._contactInfo),
        get_field(User.Fields._subscription),
        get_field(Employee.Fields._roles),
        get_field(Employee.Fields._authorities),
        get_field(BusinessUser.Fields._chatRooms),
        get_field(User.Fields._participations),
        get_field(User.Fields._history),
        get_field(Model.Fields._timestamp)
    );
  }
}
