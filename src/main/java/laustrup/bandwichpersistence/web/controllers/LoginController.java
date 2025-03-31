package laustrup.bandwichpersistence.web.controllers;

import laustrup.bandwichpersistence.core.managers.UserDetailsManager;
import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.core.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

import static laustrup.bandwichpersistence.web.services.WebService.respond;

@RestController
public class LoginController {

    private final Logger _logger = Logger.getLogger(LoginController.class.getName());

    @PostMapping("login")
    public ResponseEntity<User> login(@RequestBody Login login) {
        return respond(() -> UserDetailsManager.getUser(login));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Instantiation exception for login!")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handleException(HttpMessageNotReadableException ex) {
        _logger.warning(ex.getMessage());
    }
}
