package laustrup.bandwichpersistence.controllers;

import laustrup.models.Response;
import laustrup.dtos.users.sub_users.venues.VenueDTO;
import laustrup.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.VenueControllerService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") @RestController
public class VenueController {

    private final String _endpointDirectory = "/api/venue/";

    @PostMapping(value = _endpointDirectory + "create/{password}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<VenueDTO>> create(@RequestBody VenueDTO venue,
                                                     @PathVariable(name = "password") String password) {
        return VenueControllerService.get_instance().create(new Venue(venue), password);
    }
}
