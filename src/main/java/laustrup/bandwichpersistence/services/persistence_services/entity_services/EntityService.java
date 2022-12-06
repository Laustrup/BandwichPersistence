package laustrup.bandwichpersistence.services.persistence_services.entity_services;

import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.repositories.sub_repositories.ModelRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;

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
        if (element.getClass() == User.class) {
            if (ModelRepository.get_instance().upsert(((User) element).get_contactInfo())) {
                if (ModelRepository.get_instance().upsert(((User) element).get_subscription())) {
                    Assembly.get_instance().finish((User) element);
                    return true;
                }
            }
        }
        return false;
    }
}
