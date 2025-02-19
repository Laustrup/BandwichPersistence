package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.Participant;

public class UserService {

    public static User.UserDTO from(User user) {
        return user.getClass() == Artist.class
                ? new Artist.DTO((Artist) user)
                : new Participant.DTO((Participant) user);
    }

    public static User from(User.UserDTO user) {
        return user.getClass() == Artist.DTO.class
                ? new Artist((Artist.DTO) user)
                : new Participant((Participant.DTO) user);
    }
}
