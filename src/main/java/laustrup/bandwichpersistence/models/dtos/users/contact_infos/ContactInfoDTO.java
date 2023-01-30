package laustrup.bandwichpersistence.models.dtos.users.contact_infos;

import laustrup.bandwichpersistence.models.dtos.ModelDTO;

import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains information that people need in order to contact the User.
 */
@NoArgsConstructor @Data
public class ContactInfoDTO extends ModelDTO {

    /**
     * The email that the User wants to be contacted through outside the application.
     */
    private String email;

    /**
     * A Phone object that is used to have information about how to contact the User through Phone.
     */
    private PhoneDTO phone;

    /**
     * An Address object with info about the location of the User.
     */
    private AddressDTO address;

    /**
     * A Country object for the information of which Country the User is living in.
     */
    private CountryDTO country;

    public ContactInfoDTO(ContactInfo contactInfo) {
        super(contactInfo.get_primaryId(), "Contact-info: "+contactInfo.get_primaryId(), contactInfo.get_timestamp());
        email = contactInfo.get_email();
        phone = new PhoneDTO(contactInfo.get_phone());
        address = new AddressDTO(contactInfo.get_address());
        country = new CountryDTO(contactInfo.get_country());
    }
}
