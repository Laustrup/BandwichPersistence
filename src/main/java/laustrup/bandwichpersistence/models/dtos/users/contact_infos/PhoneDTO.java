package laustrup.bandwichpersistence.models.dtos.users.contact_infos;

import laustrup.bandwichpersistence.models.users.contact_infos.Phone;
import lombok.*;

/**
 * Details about phone contacting information.
 */
@NoArgsConstructor @Data
public class PhoneDTO {

    /**
     * A country object, that represents the nationality of this PhoneNumber.
     */
    private CountryDTO country;

    /**
     * The contact numbers for the Phone.
     */
    private long numbers;

    /**
     * True if the number is for a mobile.
     */
    private boolean mobile;

    public PhoneDTO(Phone phone) {
        country = new CountryDTO(phone.get_country());
        numbers = phone.get_numbers();
        mobile = phone.is_mobile();
    }
}
