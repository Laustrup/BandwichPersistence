package laustrup.bandwichpersistence.controllers;

import laustrup.dtos.users.sub_users.bands.ArtistDTO;
import laustrup.models.Response;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.ArtistControllerService;
import laustrup.models.users.sub_users.bands.Artist;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") @RestController
public class ArtistController {

    private final String _endpointDirectory = "/api/artist/";

    @PostMapping(value = _endpointDirectory + "create/{password}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<ArtistDTO>> create(@RequestBody ArtistDTO artist,
                                                      @PathVariable(name = "password") String password) {
        return ArtistControllerService.get_instance().create(new Artist(artist), password);
    }
}