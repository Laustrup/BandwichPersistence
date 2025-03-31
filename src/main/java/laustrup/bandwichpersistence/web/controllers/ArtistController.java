package laustrup.bandwichpersistence.web.controllers;

import laustrup.bandwichpersistence.core.managers.ArtistManager;
import laustrup.bandwichpersistence.core.models.users.Artist;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static laustrup.bandwichpersistence.web.services.WebService.respond;

@RestController
@RequestMapping("/artist/")
public class ArtistController {

    @PostMapping("upsert")
    public ResponseEntity<Artist.DTO> upsert(@RequestBody Artist.DTO artist) {
        return respond(() -> ArtistManager.upsert(new Artist(artist)));
    }
}
