package laustrup.bandwichpersistence.core.managers;

import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.core.models.Response;
import laustrup.bandwichpersistence.core.models.User;
import laustrup.bandwichpersistence.core.services.builders.UserBuilder;

import static laustrup.bandwichpersistence.core.managers.ManagerService.databaseInteraction;

public class LoginManager {

    public static Response<User> getUser(Login login) {
        return databaseInteraction(() -> UserBuilder.buildLogins());
    }
}
