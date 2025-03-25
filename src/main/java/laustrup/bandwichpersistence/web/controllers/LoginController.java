package laustrup.bandwichpersistence.web.controllers;

import laustrup.bandwichpersistence.core.managers.UserDetailsManager;
import laustrup.bandwichpersistence.core.models.Login;
import laustrup.bandwichpersistence.core.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static laustrup.bandwichpersistence.web.services.WebService.respond;

@RestController
public class LoginController {

    @PostMapping("login")
    public ResponseEntity<User> login(
            @RequestBody Login login
    ) {
        return respond(() -> UserDetailsManager.getUser(login));
    }
}
