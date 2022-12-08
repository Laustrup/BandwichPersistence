package laustrup.bandwichpersistence.controllers;

import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.Response;
import laustrup.bandwichpersistence.models.Search;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.UserControllerService;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Plato;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("api/user/")
public class UserController {

    @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User>> logIn(@RequestBody Login login) {
        return UserControllerService.get_instance().get(login);
    }

    @PostMapping("get/{id}")
    public ResponseEntity<Response<User>> get(@PathVariable(name = "id") long id) {
        return UserControllerService.get_instance().get(id);
    }

    @PostMapping("get")
    public ResponseEntity<Response<Liszt<User>>> get() {
        return UserControllerService.get_instance().get();
    }

    @PostMapping("search/{search_query}")
    public ResponseEntity<Response<Search>> search(@PathVariable(name = "search_query") String searchQuery) {
        return UserControllerService.get_instance().search(searchQuery);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<Plato>> delete(@PathVariable(name = "id") long id) {
        return UserControllerService.get_instance().delete(new Participant(id));
    }

    @DeleteMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Plato>> delete(@RequestBody User user) {
        return UserControllerService.get_instance().delete(user);
    }

    @PatchMapping(value = "upsert/bulletin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User>> upsert(@RequestBody Bulletin bulletin) {
        return UserControllerService.get_instance().upsert(new Bulletin(
                    bulletin.get_primaryId(),bulletin.get_author(),bulletin.get_receiver(),
                    bulletin.get_content(),bulletin.is_sent(),bulletin.get_edited(),
                    bulletin.is_public(), bulletin.get_timestamp()
                )
        );
    }

    @PatchMapping(value = "upsert/rating", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User>> upsert(@RequestBody Rating rating) {
        return UserControllerService.get_instance().upsert(new Rating(
                    rating.get_value(), rating.get_appointed(),
                    rating.get_judge(), rating.get_timestamp()
                )
        );
    }
}
