package laustrup.bandwichpersistence.models.users.contact_infos;

import lombok.Getter;

/**
 * An object with information about a curtain Country.
 */
public class Country {

    /**
     * The name of the Country.
     */
    @Getter
    private String _title;

    /**
     * The two digits indexes of the Country.
     */
    @Getter
    private CountryIndexes _indexes;

    /**
     * The value of the first few digits of a phone number.
     */
    @Getter
    private int _firstPhoneNumberDigits;

    public Country(String title, CountryIndexes indexes, int firstPhoneNumberDigits) {
        _title = title;
        _indexes = indexes;
        _firstPhoneNumberDigits = firstPhoneNumberDigits;
    }

    /**
     * An enum with indexes of the Country.
     */
    public enum CountryIndexes { DK, SE, DE }
}
