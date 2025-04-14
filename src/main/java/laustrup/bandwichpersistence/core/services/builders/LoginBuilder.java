package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService.Field;

import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.set;

public class LoginBuilder extends BuilderService<Login> {

    private static final Logger _logger = Logger.getLogger(LoginBuilder.class.getName());

    private static LoginBuilder _instance;

    public static LoginBuilder get_instance() {
        if (_instance == null)
            _instance = new LoginBuilder();

        return _instance;
    }

    private LoginBuilder() {
        super(Login.class, _logger);
    }

    @Override
    protected void completion(Login reference, Login object) {

    }

    @Override
    protected Function<Function<String, Field>, Login> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<String>
                    username = new AtomicReference<>(),
                    password = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(username, table.apply(Login.Fields.username));
                        set(password, table.apply(Login.Fields.password));
                    }
            );

            return new Login(
                    username.get(),
                    password.get()
            );
        };
    }
}
