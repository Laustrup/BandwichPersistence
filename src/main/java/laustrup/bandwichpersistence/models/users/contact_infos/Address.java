package laustrup.bandwichpersistence.models.users.contact_infos;

import laustrup.bandwichpersistence.models.dtos.users.contact_infos.AddressDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Contains values that determines address attributes.
 */
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

    public Address(AddressDTO address) {
        _street = address.getStreet();
        _floor = address.getFloor();
        _postal = address.getPostal();
        _city = address.getCity();
    }

    public Address(String street, String floor, String postal, String city) {
        _street = street;
        _floor = floor;
        _postal = postal;
        _city = city;
    }

    @Override
    public String toString() {
        String info = new String();

        info += get_street() != null ? get_street() + ", " : "";
        info += get_floor() != null ? get_floor() + ", " : "";
        info += get_postal() != null ? get_postal() + " " : "";
        info += get_city() != null ? get_city() : "";

        return info;
    }
}
