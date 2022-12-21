package laustrup.bandwichpersistence.controllers;

import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.Response;
import laustrup.bandwichpersistence.models.Search;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.subscriptions.Card;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.UserControllerService;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Plato;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/user/")
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

    @PatchMapping(value = "upsert/album", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User>> upsert(@RequestBody Album album) {
        return UserControllerService.get_instance().upsert(new Album(
                    album.get_title(), album.get_items(), album.get_author()
                )
        );
    }

    @PutMapping(value = "follow", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User[]>> follow(@RequestBody User[] users) {
        return UserControllerService.get_instance().follow(users[0], users[1]);
    }

    @DeleteMapping(value = "unfollow", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User[]>> unfollow(@RequestBody User[] users) {
        return UserControllerService.get_instance().unfollow(users[0], users[1]);
    }

    @PatchMapping(value = "update/{login_username}/{login_password}/{password}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User>> update(@RequestBody User user,
                                                 @PathVariable(name = "login_username") String username,
                                                 @PathVariable(name = "login_password") String loginPassword,
                                                 @PathVariable(name = "password") String password) {
        return UserControllerService.get_instance().update(user,new Login(username,loginPassword),password);
    }

    @PatchMapping(value = "upsert/card/{user_id}/{login_username}/{login_password}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User>> upsert(@RequestBody Card card,
                                                 @PathVariable(name = "user_id") long id,
                                                 @PathVariable(name = "login_username") String username,
                                                 @PathVariable(name = "login_password") String password) {
        return UserControllerService.get_instance().upsert(id, new Login(username,password), card);
    }

    @PatchMapping(value = "upsert/subscription/{login_username}/{login_password}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<User>> upsert(@RequestBody User user,
                                                 @PathVariable(name = "login_username") String username,
                                                 @PathVariable(name = "login_password") String password) {
        return UserControllerService.get_instance().upsert(user, new Login(username, password));
    }
}
