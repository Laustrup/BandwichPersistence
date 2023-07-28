package laustrup.bandwichpersistence.controllers;

import laustrup.models.Response;
import laustrup.models.chats.messages.Bulletin;
import laustrup.dtos.chats.messages.BulletinDTO;
import laustrup.dtos.events.EventDTO;
import laustrup.dtos.events.ParticipationDTO;
import laustrup.models.events.Event;
import laustrup.models.events.Participation;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.EventControllerService;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.utilities.parameters.Plato;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") @RestController
public class EventController {

    private final String _endpointDirectory = "/api/event/";

    @PostMapping(_endpointDirectory + "get/{id}")
    public ResponseEntity<Response<EventDTO>> get(@PathVariable(name = "id") long id) {
        return EventControllerService.get_instance().get(id);
    }

    @PostMapping(_endpointDirectory + "get")
    public ResponseEntity<Response<EventDTO[]>> get() {
        return EventControllerService.get_instance().get();
    }

    @PostMapping(value = _endpointDirectory + "create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<EventDTO>> create(@RequestBody EventDTO event) {
        return EventControllerService.get_instance().create(new Event(event));
    }

    @DeleteMapping(_endpointDirectory + "{id}")
    public ResponseEntity<Response<Plato.Argument>> delete(@PathVariable(name = "id") long id) {
        return EventControllerService.get_instance().delete(new Event(id));
    }

    @DeleteMapping(value = _endpointDirectory + "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Plato.Argument>> delete(@RequestBody EventDTO event) {
        return EventControllerService.get_instance().delete(new Event(event));
    }

    @PatchMapping(value = _endpointDirectory + "update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<EventDTO>> update(@RequestBody EventDTO event) {
        return EventControllerService.get_instance().update(new Event(event));
    }

    @PutMapping(value = _endpointDirectory + "upsert/participations", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<EventDTO>> upsert(@RequestBody ParticipationDTO[] dtos) {
        Liszt<Participation> participations = new Liszt<>();
        for (ParticipationDTO participation : dtos)
            participations.add(new Participation(participation));
        return EventControllerService.get_instance().upsert(participations);
    }

    @PatchMapping(value = "upsert/bulletin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<EventDTO>> upsert(@RequestBody BulletinDTO bulletin) {
        return EventControllerService.get_instance().upsert(new Bulletin(bulletin));
    }
}
