package laustrup.bandwichpersistence.models.users.contact_infos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Contains values that determines address attributes.
 */
@ToString
public class Address {

    /**
     * The street and street number.
     */
    @Getter @Setter
    private String _street;

    /**
     * The floor, if in an apartment, also include left or right.
     */
    @Getter @Setter
    private String _floor;

    /**
     * Some digits describing the city.
     */
    @Getter @Setter
    private String _postal;

    /**
     * The city of the postal.
     */
    @Getter @Setter
    private String _city;

    public Address(String street, String floor, String postal, String city) {
        _street = street;
        _floor = floor;
        _postal = postal;
        _city = city;
    }
}
