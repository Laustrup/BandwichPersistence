package laustrup.bandwichpersistence.models.users.sub_users.venues;

import laustrup.bandwichpersistence.models.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @ToString
public class Venue extends User {

    @Getter @Setter
    private String _location;

}
