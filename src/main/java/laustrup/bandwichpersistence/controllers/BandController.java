package laustrup.bandwichpersistence.controllers;

import laustrup.bandwichpersistence.models.Response;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.BandControllerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/band/")
public class BandController {

    @PostMapping(value = "create/{password}", consumes = "application/json")
    public ResponseEntity<Response<Band>> create(@RequestBody Band band, @PathVariable(name = "password") String password) {
        return BandControllerService.get_instance().create(new Band(
                band.get_username(), band.get_description(), band.get_subscription(),
                        band.get_contactInfo(), band.get_members()),
                password
        );
    }
}