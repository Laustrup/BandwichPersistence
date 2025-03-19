package laustrup.bandwichpersistence.core.services.builders;

import java.util.UUID;
import java.util.logging.Logger;

public class BuilderService {

    static void printError(Class<?> origin, UUID id, Exception exception, Logger logger) {
        logger.warning(String.format(
                "Could not build %s of %s:\n%s",
                origin.getSimpleName(),
                id,
                exception.getMessage()
        ));
    }
}
