package laustrup.bandwichpersistence.core.models.identification;

import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;

@Table.Enum(title = "authorities", columns = {
    @Table.Column(value = "id"),
    @Table.Column(value = "level")
})
public interface Authority {
}
