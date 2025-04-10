package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService.Field;

import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.set;

public class LoginBuilder extends BuilderService<Login> {

    protected LoginBuilder() {
        super(Login.class, LoginBuilder.class);
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
