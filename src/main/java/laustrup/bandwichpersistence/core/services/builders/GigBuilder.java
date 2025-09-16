package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Event;
import laustrup.bandwichpersistence.core.models.Model;

import java.util.UUID;

public class GigBuilder extends BuilderService<Event.Gig> {

    private static GigBuilder _instance;

    public static GigBuilder get_instance() {
        if (_instance == null)
            _instance = new GigBuilder();

        return _instance;
    }

    private GigBuilder() {

    }

    @Override
    protected void completion(Event.Gig collective, Event.Gig part) {
        combine(collective.get_act(), part.get_act());
    }

    @Override
    protected Event.Gig construct() {
        return new Event.Gig(
                new Event.Gig.Id((UUID) get_field(Model.Fields._identity)),
                get_field(Event.Gig.Fields._event),
                get_field(Event.Gig.Fields._act),
                get_field(Event.Gig.Fields._start),
                get_field(Event.Gig.Fields._end),
                get_field(Model.Fields._timestamp)
        );
    }
}
