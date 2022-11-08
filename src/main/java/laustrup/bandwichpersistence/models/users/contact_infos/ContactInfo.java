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
    private String _email, _phoneNumber;
    @Getter @Setter
    private Address _address;

    public ContactInfo(String email, String phoneNumber, Address address) {
        _email = email;
        _phoneNumber = phoneNumber;
        _address = address;
    }
}
