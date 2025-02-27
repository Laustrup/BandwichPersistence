package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.models.BusinessUser;
import laustrup.bandwichpersistence.core.models.Organisation;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.Participant;

public class UserService {

    public static User.UserDTO from(User user) {
        return user.getClass() == Artist.class
                ? new Artist.DTO((Artist) user)
                : (user.getClass() == Participant.class
                        ? new Participant.DTO((Participant) user)
                        : new Organisation.Employee.DTO((Organisation.Employee) user)
                );
    }

    public static User from(User.UserDTO user) {
        return user.getClass() == Artist.DTO.class
                ? new Artist((Artist.DTO) user)
                : (user.getClass() == Participant.DTO.class
                        ? new Participant((Participant.DTO) user)
                        : new Organisation.Employee((Organisation.Employee.DTO) user)
                );
    }

    public static BusinessUser fromBusinessUser(User.UserDTO user) {
        return (BusinessUser) from(user);
    }

    public static BusinessUser.BusinessUserDTO fromBusinessUser(User user) {
        return (BusinessUser.BusinessUserDTO) from(user);
    }
}
