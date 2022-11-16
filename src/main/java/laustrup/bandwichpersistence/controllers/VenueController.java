package laustrup.bandwichpersistence.controllers;

import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.services.controller_services.VenueControllerService;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/venue/")
public class VenueController {

    /*
    @GetMapping("{id]")
    public HttpEntity<Venue> getVenue() { return VenueControllerService.get_instance().getVenue(); }
    */
}
