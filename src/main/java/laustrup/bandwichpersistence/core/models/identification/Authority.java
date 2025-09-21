package laustrup.bandwichpersistence.core.models.identification;

import laustrup.bandwichpersistence.core.persistence.worm.annotations.DatabaseEntity;

@DatabaseEntity.Enum(title = "authorities", columns = {
        @DatabaseEntity.Column(value = "id"),
        @DatabaseEntity.Column(value = "level")
})
public interface Authority {
}
