package laustrup.bandwichpersistence.controllers;

import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.ParticipantControllerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/participant/")
public class ParticipantController {

    @PostMapping(value = "create/{password}", consumes = "application/json")
    public ResponseEntity<Participant> create(@RequestBody Participant participant,
                                                        @PathVariable(name = "password") String password) {
        return ParticipantControllerService.get_instance().create(new Participant(participant.get_primaryId(), participant.get_username(),
                participant.get_firstName(), participant.get_lastName(), participant.get_description(),participant.get_contactInfo(),
                participant.get_images(), participant.get_ratings(), participant.get_events(),participant.get_chatRooms(),
                participant.get_subscription(),participant.get_bulletins(),participant.get_timestamp(), participant.get_idols()),
                password
        );
    }
}
