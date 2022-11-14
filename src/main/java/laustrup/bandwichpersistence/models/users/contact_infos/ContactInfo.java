package laustrup.bandwichpersistence.models.users.contact_infos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Contains information that people need in order to contact the user.
 */
@ToString
public class ContactInfo {

    @Getter @Setter
    private String _email;

    @Getter
    private PhoneNumber _phoneNumber;

    @Getter @Setter
    private Address _address;

    @Getter
    private Country _country;

    public ContactInfo(String email, PhoneNumber phoneNumber, Address address, Country country) {
        _email = email;
        _phoneNumber = phoneNumber;
        _address = address;
        _country = country;
    }
}
