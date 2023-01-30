package laustrup.bandwichpersistence.models.dtos.users.contact_infos;

import laustrup.bandwichpersistence.models.users.contact_infos.Country;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An object with information about a curtain Country.
 */
@NoArgsConstructor @Data
public class CountryDTO {

    /**
     * The name of the Country.
     */
    private String title;

    /**
     * The two digits indexes of the Country.
     */
    private CountryIndexes indexes;

    /**
     * The value of the first few digits of a phone number.
     */
    private int firstPhoneNumberDigits;

    public CountryDTO(Country country) {
        title = country.get_title();
        indexes = CountryIndexes.valueOf(country.get_indexes().toString());
        firstPhoneNumberDigits = country.get_firstPhoneNumberDigits();
    }

    /**
     * An enum with indexes of the Country.
     */
    public enum CountryIndexes { DK, SE, DE }
}
