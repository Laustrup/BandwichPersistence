package laustrup.bandwichpersistence.controllers;

import laustrup.bandwichpersistence.models.Response;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.VenueControllerService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/venue/")
public class VenueController {

    @PostMapping(value = "create/{password}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Venue>> create(@RequestBody Venue venue,
                                                  @PathVariable(name = "password") String password) {
        return VenueControllerService.get_instance().create(new Venue(venue.get_primaryId(),venue.get_username(),
                        venue.get_description(),venue.get_contactInfo(), venue.get_albums(),venue.get_ratings(),
                        venue.get_events(),venue.get_chatRooms(),venue.get_timestamp(), venue.get_location(),
                        venue.get_gearDescription(), venue.get_subscription().get_status(),
                        venue.get_subscription().get_offer(), venue.get_bulletins(),
                        venue.get_size(),venue.get_requests()),
                password
        );
    }
}
