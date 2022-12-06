package laustrup.bandwichpersistence.controllers;

import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.ArtistControllerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/artist/")
public class ArtistController {

    @PostMapping(value = "create/{password}", consumes = "application/json")
    public ResponseEntity<Artist> create(@RequestBody Artist artist, @PathVariable(name = "password") String password) {
        return ArtistControllerService.get_instance().create(new Artist(
                artist.get_username(),artist.get_firstName(),artist.get_lastName(), artist.get_description(),
                artist.get_subscription(), artist.get_contactInfo(),artist.get_bands(), artist.get_runner()), password
        );
    }
}