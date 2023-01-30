package laustrup.bandwichpersistence.models.users.contact_infos;

import laustrup.bandwichpersistence.models.Model;

import laustrup.bandwichpersistence.models.dtos.users.contact_infos.ContactInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Contains information that people need in order to contact the User.
 */
public class ContactInfo extends Model {

    /**
     * The email that the User wants to be contacted through outside the application.
     */
    @Getter @Setter
    private String _email;

    /**
     * A Phone object that is used to have information about how to contact the User through Phone.
     */
    @Getter
    private Phone _phone;

    /**
     * An Address object with info about the location of the User.
     */
    @Getter @Setter
    private Address _address;

    /**
     * A Country object for the information of which Country the User is living in.
     */
    @Getter
    private Country _country;

    public ContactInfo(ContactInfoDTO contactInfo) {
        super(contactInfo.getPrimaryId(), "Contact-info: "+contactInfo.getPrimaryId(), contactInfo.getTimestamp());
        _email = contactInfo.getEmail();
        _phone = new Phone(contactInfo.getPhone());
        _address = new Address(contactInfo.getAddress());
        _country = new Country(contactInfo.getCountry());
    }
    public ContactInfo(long id, String email, Phone phone, Address address, Country country, LocalDateTime timestamp) {
        super(id, "Contact-info: "+id, timestamp);
        _email = email;
        _phone = phone;
        _address = address;
        _country = country;
    }

    public ContactInfo(String email, Phone phone, Address address, Country country) {
        super();
        _email = email;
        _phone = phone;
        _address = address;
        _country = country;
        _title = getAddressInfo();
    }

    /**
     * Collects the details of the Address as a one liner String.
     * @return The collected one liner String of the Address.
     */
    public String getAddressInfo() {
        return _address.toString();
    }

    @Override
    public String toString() {
        return "ContactInfo(" +
                    "id:" + _primaryId +
                    ",email:" + _email +
                    ",address:" + getAddressInfo() +
                    ",phone:" + _phone.toString() +
                    ",country:" + _country.toString() +
                ")";
    }
}
