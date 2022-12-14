package laustrup.bandwichpersistence.controllers;

import laustrup.bandwichpersistence.models.Response;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.events.Participation;
import laustrup.bandwichpersistence.services.controller_services.sub_controller_services.EventControllerService;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Plato;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/event/")
public class EventController {

    @PostMapping("get/{id}")
    public ResponseEntity<Response<Event>> get(@PathVariable(name = "id") long id) {
        return EventControllerService.get_instance().get(id);
    }

    @PostMapping("get")
    public ResponseEntity<Response<Liszt<Event>>> get() {
        return EventControllerService.get_instance().get();
    }

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Event>> create(@RequestBody Event event) {
        return EventControllerService.get_instance().create(event);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<Plato>> delete(@PathVariable(name = "id") long id) {
        return EventControllerService.get_instance().delete(new Event(id));
    }

    @DeleteMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Plato>> delete(@RequestBody Event event) {
        return EventControllerService.get_instance().delete(event);
    }

    @PatchMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Event>> update(@RequestBody Event event) {
        return EventControllerService.get_instance().update(event);
    }

    @PutMapping(value = "upsert/participations", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Event>> upsert(@RequestBody Participation[] participations) {
        return EventControllerService.get_instance().upsert(new Liszt<>(participations));
    }

    @PatchMapping(value = "upsert/bulletin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Event>> upsert(@RequestBody Bulletin bulletin) {
        return EventControllerService.get_instance().upsert(new Bulletin(
                        bulletin.get_primaryId(),bulletin.get_author(),bulletin.get_receiver(),
                        bulletin.get_content(),bulletin.is_sent(),bulletin.get_edited(),
                        bulletin.is_public(), bulletin.get_timestamp()
                )
        );
    }
}
