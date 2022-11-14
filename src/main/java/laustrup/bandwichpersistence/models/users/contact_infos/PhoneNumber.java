package laustrup.bandwichpersistence.models.users.contact_infos;

import lombok.Getter;
import lombok.Setter;

public class PhoneNumber {

    @Getter
    @Setter
    private Country _country;

    @Getter @Setter
    private long _numbers;

    @Getter
    private boolean _mobile;

    public PhoneNumber(Country country, long numbers, boolean mobile) {
        _country = country;
        _numbers = numbers;
        _mobile = mobile;
    }
}
