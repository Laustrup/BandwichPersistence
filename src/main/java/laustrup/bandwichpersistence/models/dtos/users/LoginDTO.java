package laustrup.bandwichpersistence.models.dtos.users;

import laustrup.bandwichpersistence.models.users.Login;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Is use for logging in a user.
 * Can check validations of email and password.
 */
@NoArgsConstructor @Data
public class LoginDTO {

    /**
     * The title used for a user.
     * Can also be as an email, since it's possible to log in with email.
     */
    private String username;

    /**
     * This makes sure, that the user is allowed to log in.
     * The length must be 7, also must contain a special character and number.
     */
    private String password;

    /**
     * Specifies the time this entity was created.
     */
    private LocalDateTime timestamp;

    public LoginDTO(Login login) {
        username = login.get_username();
        password = login.get_password();
        timestamp = login.get_timestamp();
    }
}
