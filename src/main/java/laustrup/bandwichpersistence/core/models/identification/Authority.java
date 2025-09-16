package laustrup.bandwichpersistence.core.models.identification;

import laustrup.bandwichpersistence.core.persistence.models.annotations.DatabaseEntity;

@DatabaseEntity.Enum(title = "authorities", columns = {
        @DatabaseEntity.Column(title = "id"),
        @DatabaseEntity.Column(title = "level")
})
public interface Authority {
}
