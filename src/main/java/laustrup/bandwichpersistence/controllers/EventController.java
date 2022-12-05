package laustrup.bandwichpersistence.controllers;

import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.services.controller_services.EventControllerService;
import laustrup.bandwichpersistence.utilities.Liszt;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/event/")
public class EventController {

    @PostMapping("get/{id}")
    public ResponseEntity<Event> get(@PathVariable(name = "id") long id) {
        return EventControllerService.get_instance().get(id);
    }
    @PostMapping("get")
    public ResponseEntity<Liszt<Event>> get() {
        return EventControllerService.get_instance().get();
    }
}
