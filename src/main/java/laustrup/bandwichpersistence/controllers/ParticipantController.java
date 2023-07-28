package laustrup.bandwichpersistence.controllers;

import laustrup.models.Response;
import laustrup.dtos.users.sub_users.participants.ParticipantDTO;
import laustrup.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.ParticipantControllerService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") @RestController
public class ParticipantController {

    private final String _endpointDirectory = "/api/participant/";

    @PostMapping(value = _endpointDirectory + "create/{password}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<ParticipantDTO>> create(@RequestBody ParticipantDTO participant,
                                                           @PathVariable(name = "password") String password) {
        return ParticipantControllerService.get_instance().create(new Participant(participant), password);
    }
}
