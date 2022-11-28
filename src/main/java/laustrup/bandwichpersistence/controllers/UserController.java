package laustrup.bandwichpersistence.controllers;

import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.services.controller_services.UserControllerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/user/")
public class UserController {

    @PostMapping(value = "login", consumes = "application/json")
    public ResponseEntity<User> logIn(@RequestBody Login login) {
        return UserControllerService.get_instance().get(login);
    }

    @PostMapping("{id}")
    public ResponseEntity<User> get(@PathVariable(name = "id") long id) {
        return UserControllerService.get_instance().get(id);
    }
}
