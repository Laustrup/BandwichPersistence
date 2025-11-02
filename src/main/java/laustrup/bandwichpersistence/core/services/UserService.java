package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.models.Organisation.Employee;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.BusinessUser;
import laustrup.bandwichpersistence.core.models.users.Participant;
import laustrup.bandwichpersistence.core.models.users.User;

public class UserService {

  //TODO Make these more dry
  public static User.UserDTO<? extends User.Id> from(User<? extends User.Id> user) {
    if (user.getClass().equals(Artist.class)) {
      return new Artist.DTO((Artist) user);
    } else if (user.getClass().equals(Participant.class)) {
      return new Participant.DTO((Participant) user);
    } else if (user.getClass().equals(Employee.class)) {
      return new Organisation.Employee.DTO((Employee) user);
    }

    return null;
  }

  public static User<? extends User.Id> from(User.UserDTO<? extends User.Id> user) {
    if (user.getClass().equals(Artist.DTO.class)) {
      return new Artist((Artist.DTO) user);
    } else if (user.getClass().equals(Participant.DTO.class)) {
      return new Participant((Participant.DTO) user);
    } else if (user.getClass().equals(Employee.DTO.class)) {
      return new Organisation.Employee((Employee.DTO) user);
    }

    return null;
  }

  public static BusinessUser<? extends User.Id> fromBusinessUser(User.UserDTO<? extends User.Id> user) {
    return (BusinessUser<? extends User.Id>) from(user);
  }

  public static BusinessUser.BusinessUserDTO<?> fromBusinessUser(User<? extends User.Id> user) {
    return (BusinessUser.BusinessUserDTO<?>) from(user);
  }
}
