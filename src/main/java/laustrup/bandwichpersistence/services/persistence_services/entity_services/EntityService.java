package laustrup.bandwichpersistence.services.persistence_services.entity_services;

import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.repositories.sub_repositories.ModelRepository;
import laustrup.bandwichpersistence.repositories.sub_repositories.UserRepository;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.utilities.Plato;
import laustrup.bandwichpersistence.utilities.Printer;

/**
 * Contains common methods for entity services to use.
 */
public class EntityService {

    /**
     * Will upsert User values such as contact info and Subscription
     * Also closes connections.
     * @param user The User that should have its values upserted.
     * @return True if it is a success.
     */
    protected boolean upsert(User user) {
        if (ModelRepository.get_instance().upsert(user.get_contactInfo())) {
            if (ModelRepository.get_instance().upsert(user.get_subscription())) {
                Plato success = Assembly.get_instance().closeConnections();
                if (success.get_truth())
                    return true;
                else
                    Printer.get_instance().print(success.get_message(), new Exception());
            }
        }
        return false;
    }
}
