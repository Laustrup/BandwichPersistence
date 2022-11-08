package laustrup.bandwichpersistence.models.users.contact_infos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Contains values that determines address attributes.
 */
@ToString
public class Address {

    @Getter @Setter
    private String _street, _floor, _postal, _city;

    public Address(String street, String floor, String postal, String city) {
        _street = street;
        _floor = floor;
        _postal = postal;
        _city = city;
    }
}
