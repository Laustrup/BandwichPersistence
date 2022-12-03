package laustrup.bandwichpersistence.controllers;

import laustrup.bandwichpersistence.models.Search;
import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.services.controller_services.UserControllerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("api/user/")
public class UserController {

    @PostMapping(value = "login", consumes = "application/json")
    public ResponseEntity<User> logIn(@RequestBody Login login) {
        return UserControllerService.get_instance().get(login);
    }

    @PostMapping("get/{id}")
    public ResponseEntity<User> get(@PathVariable(name = "id") long id) {
        return UserControllerService.get_instance().get(id);
    }

    @PostMapping("search/{search_query}")
    public ResponseEntity<Search> search(@PathVariable(name = "search_query") String searchQuery) {
        return UserControllerService.get_instance().search(searchQuery);
    }
}
