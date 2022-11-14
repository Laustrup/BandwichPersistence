package laustrup.bandwichpersistence.models.users.contact_infos;

import lombok.Getter;

public class Country {

    @Getter
    private String _title;

    @Getter
    private CountryIndexes _indexes;

    @Getter
    private int _firstPhoneNumberIntegers;

    public Country(String title, CountryIndexes indexes, int firstPhoneNumberIntegers) {
        _title = title;
        _indexes = indexes;
        _firstPhoneNumberIntegers = firstPhoneNumberIntegers;
    }

    public enum CountryIndexes { DK, SE, DE }
}
