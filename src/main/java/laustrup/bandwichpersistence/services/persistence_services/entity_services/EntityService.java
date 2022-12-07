package laustrup.bandwichpersistence.services.persistence_services.entity_services;

import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.repositories.sub_repositories.ModelRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;

import java.util.Objects;

/**
 * Contains common methods for entity services to use.
 */
public class EntityService<E> {

    /**
     * Will upsert Element values.
     * Also closes connections.
     * @param element The Element that should have its values upserted.
     * @return True if it is a success.
     */
    protected boolean upsert(E element) {
        if (element.getClass() == Artist.class ||
            element.getClass() == Band.class ||
            element.getClass() == Venue.class ||
            element.getClass() == Package.class ||
                Objects.equals(element.getClass(), User.class)) {
            assert element instanceof User;
            return upsert((User) element);
        }
        //if (element.getClass() == Event.class)
            //return upsert((Event) element);

        return false;
    }


    /**
     * Upserts values for User and finishes the User by setting assembling to false and closing connections.
     * @param user The User that will be upserted.
     * @return True if it is a success.
     */
    private boolean upsert(User user) {
        if (ModelRepository.get_instance().upsert(user.get_contactInfo())) {
            if (ModelRepository.get_instance().upsert(user.get_subscription())) {
                Assembly.get_instance().finish(user);
                return true;
            }
        }
        return false;
    }

    //TODO Make EventPersistenceService and UserPersistenceService use this method.
    /*
    /**
     * Upserts values for Event and finishes the Event by setting assembling to false and closing connections.
     * @param event The Event that will be upserted.
     * @return True if it is a success.
    private boolean upsert(Event event) {
        if (ModelRepository.get_instance().upsert(event.get_contactInfo())) {
            Assembly.get_instance().finish(event);
            return true;
        }
        return false;
    }
    */
}
