package laustrup.bandwichpersistence.controllers;

import laustrup.models.Rating;
import laustrup.models.Response;
import laustrup.models.Search;
import laustrup.models.albums.Album;
import laustrup.models.chats.messages.Bulletin;
import laustrup.dtos.RatingDTO;
import laustrup.dtos.albums.AlbumDTO;
import laustrup.dtos.chats.messages.BulletinDTO;
import laustrup.dtos.users.LoginDTO;
import laustrup.dtos.users.UserDTO;
import laustrup.dtos.users.subscriptions.CardDTO;
import laustrup.models.users.Login;
import laustrup.models.users.sub_users.participants.Participant;
import laustrup.models.users.subscriptions.Card;
import laustrup.services.DTOService;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.UserControllerService;
import laustrup.utilities.parameters.Plato;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") @RestController
public class UserController {

    private final String _endpointDirectory = "/api/user/";

    @GetMapping("/api") public String main() { return "This is the API of Bandwich"; }
    @PostMapping(value = _endpointDirectory + "login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<UserDTO>> logIn(@RequestBody LoginDTO login) {
        return UserControllerService.get_instance().get(new Login(login));
    }

    @PostMapping(_endpointDirectory + "get/{id}")
    public ResponseEntity<Response<UserDTO>> get(@PathVariable(name = "id") long id) {
        return UserControllerService.get_instance().get(id);
    }

    @PostMapping(_endpointDirectory + "get")
    public ResponseEntity<Response<UserDTO[]>> get() {
        return UserControllerService.get_instance().get();
    }

    @PostMapping(_endpointDirectory + "search/{search_query}")
    public ResponseEntity<Response<Search>> search(@PathVariable(name = "search_query") String searchQuery) {
        return UserControllerService.get_instance().search(searchQuery);
    }

    @DeleteMapping(_endpointDirectory + "{id}")
    public ResponseEntity<Response<Plato.Argument>> delete(@PathVariable(name = "id") long id) {
        return UserControllerService.get_instance().delete(new Participant(id));
    }

    @DeleteMapping(value = _endpointDirectory + "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Plato.Argument>> delete(@RequestBody UserDTO user) {
        return UserControllerService.get_instance().delete(DTOService.get_instance().convertFromDTO(user));
    }

    @PatchMapping(value = _endpointDirectory + "upsert/bulletin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<UserDTO>> upsert(@RequestBody BulletinDTO bulletin) {
        return UserControllerService.get_instance().upsert(new Bulletin(bulletin));
    }

    @PatchMapping(value = _endpointDirectory + "upsert/rating", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<UserDTO>> upsert(@RequestBody RatingDTO rating) {
        return UserControllerService.get_instance().upsert(new Rating(rating));
    }

    @PatchMapping(value = _endpointDirectory + "upsert/album", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<UserDTO>> upsert(@RequestBody AlbumDTO album) {
        return UserControllerService.get_instance().upsert(new Album(album));
    }

    @PutMapping(value = _endpointDirectory + "follow", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<UserDTO[]>> follow(@RequestBody UserDTO[] users) {
        return UserControllerService.get_instance().follow(
                DTOService.get_instance().convertFromDTO(users[0]),
                DTOService.get_instance().convertFromDTO(users[1])
        );
    }

    @DeleteMapping(value = _endpointDirectory + "unfollow", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<UserDTO[]>> unfollow(@RequestBody UserDTO[] users) {
        return UserControllerService.get_instance().unfollow(
                DTOService.get_instance().convertFromDTO(users[0]),
                DTOService.get_instance().convertFromDTO(users[1])
                );
    }

    @PatchMapping(value = _endpointDirectory + "update/{login_username}/{login_password}/{password}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<UserDTO>> update(@RequestBody UserDTO user,
                                                 @PathVariable(name = "login_username") String username,
                                                 @PathVariable(name = "login_password") String loginPassword,
                                                 @PathVariable(name = "password") String password) {
        return UserControllerService.get_instance().update(
                DTOService.get_instance().convertFromDTO(user),
                new Login(username,loginPassword),
                password
        );
    }

    @PatchMapping(value = _endpointDirectory + "upsert/card/{user_id}/{login_username}/{login_password}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<UserDTO>> upsert(@RequestBody CardDTO card,
                                                 @PathVariable(name = "user_id") long id,
                                                 @PathVariable(name = "login_username") String username,
                                                 @PathVariable(name = "login_password") String password) {
        return UserControllerService.get_instance().upsert(id, new Login(username,password), new Card(card));
    }

    @PatchMapping(value = _endpointDirectory + "upsert/subscription/{login_username}/{login_password}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<UserDTO>> upsert(@RequestBody UserDTO user,
                                                 @PathVariable(name = "login_username") String username,
                                                 @PathVariable(name = "login_password") String password) {
        return UserControllerService.get_instance().upsert(
                DTOService.get_instance().convertFromDTO(user),
                new Login(username, password)
        );
    }
}
