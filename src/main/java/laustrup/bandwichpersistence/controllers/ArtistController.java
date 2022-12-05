package laustrup.bandwichpersistence.controllers;

import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.ArtistControllerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/artist/")
public class ArtistController {

    @PostMapping(value = "create", consumes = "application/json")
    public ResponseEntity<Artist> create(@RequestBody Artist artist) {
        return ArtistControllerService.get_instance().create(artist);
    }
}
