package laustrup.bandwichpersistence.web.controllers;

import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.core.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.managers.UserDetailsManager.getUser;
import static laustrup.bandwichpersistence.web.services.WebService.respond;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
public class UserDetailsController {

    private final Logger _logger = Logger.getLogger(UserDetailsController.class.getName());

    @PostMapping("login")
    public ResponseEntity<User> login(@RequestBody Login login) {
        return respond(() -> getUser(login));
    }

    @ResponseStatus(value = BAD_REQUEST, reason = "Instantiation exception for login!")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handleException(HttpMessageNotReadableException ex) {
        _logger.warning(ex.getMessage());
    }
}
