package laustrup.bandwichpersistence.models.users.contact_infos;

import laustrup.bandwichpersistence.models.dtos.users.contact_infos.PhoneDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Details about phone contacting information.
 */
@ToString
public class Phone {

    /**
     * A country object, that represents the nationality of this PhoneNumber.
     */
    @Getter @Setter
    private Country _country;

    /**
     * The contact numbers for the Phone.
     */
    @Getter @Setter
    private long _numbers;

    /**
     * True if the number is for a mobile.
     */
    @Getter
    private boolean _mobile;

    public Phone(PhoneDTO phone) {
        _country = new Country(phone.getCountry());
        _numbers = phone.getNumbers();
        _mobile = phone.isMobile();
    }
    public Phone(Country country, long numbers, boolean mobile) {
        _country = country;
        _numbers = numbers;
        _mobile = mobile;
    }
}
