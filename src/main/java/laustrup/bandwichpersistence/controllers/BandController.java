package laustrup.bandwichpersistence.controllers;

import laustrup.models.Response;
import laustrup.dtos.users.sub_users.bands.BandDTO;
import laustrup.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.BandControllerService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") @RestController
public class BandController {

    private final String _endpointDirectory = "/api/band/";

    @PostMapping(value = _endpointDirectory + "create/{password}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<BandDTO>> create(@RequestBody BandDTO band,
                                                    @PathVariable(name = "password") String password) {
        return BandControllerService.get_instance().create(new Band(band), password);
    }
}