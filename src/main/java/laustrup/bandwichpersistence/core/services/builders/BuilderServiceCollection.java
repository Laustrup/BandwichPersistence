package laustrup.bandwichpersistence.core.services.builders;

import java.util.Map;

public interface BuilderServiceCollection {

    Map<Class<?>, BuilderService<?>> get_builderServices();

}
