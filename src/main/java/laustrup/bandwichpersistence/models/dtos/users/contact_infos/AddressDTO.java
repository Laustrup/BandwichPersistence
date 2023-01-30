package laustrup.bandwichpersistence.models.dtos.users.contact_infos;

import laustrup.bandwichpersistence.models.users.contact_infos.Address;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains values that determines address attributes.
 */
@NoArgsConstructor @Data
public class AddressDTO {

    /**
     * The street and street number.
     */
    private String street;

    /**
     * The floor, if in an apartment, also include left or right.
     */
    private String floor;

    /**
     * Some digits describing the city.
     */
    private String postal;

    /**
     * The city of the postal.
     */
    private String city;

    public AddressDTO(Address address) {
        street = address.get_street();
        floor = address.get_floor();
        postal = address.get_postal();
        city = address.get_city();
    }
}
