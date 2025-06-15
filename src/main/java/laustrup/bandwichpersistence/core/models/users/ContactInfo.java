package laustrup.bandwichpersistence.core.models.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import laustrup.bandwichpersistence.core.models.Model;

import laustrup.bandwichpersistence.core.models.Table;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import java.util.*;

import static laustrup.bandwichpersistence.core.services.ModelService.defineToString;
import static laustrup.bandwichpersistence.core.utilities.collections.Seszt.copy;

/**
 * Contains information that people need in order to contact the User.
 */
@Getter @FieldNameConstants
@Table(title = "contact_info")
public class ContactInfo {

    private UUID _id;

    /**
     * The email that the User wants to be contacted through outside the application.
     */
    @Setter
    private String _email;

    /**
     * A Phone object that is used to have information about how to contact the User through Phone.
     */
    private Seszt<Phone> _phones;

    /**
     * An Address object with info about the location of the User.
     */
    @Setter
    private Address _address;

    /**
     * A Country object for the information of which Country the User is living in.
     */
    private Country _country;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param contactInfo The transport object to be transformed.
     */
    public ContactInfo(DTO contactInfo) {
        this(
                contactInfo.getId(),
                contactInfo.getEmail(),
                copy(contactInfo.getPhones(), Phone::new),
                new Address(contactInfo.getAddress()),
                new Country(contactInfo.getCountry())
        );
    }

    public ContactInfo(UUID id, String email, Seszt<Phone> phones, Address address, Country country) {
        _id = id;
        _email = email;
        _phones = phones;
        _address = address;
        _country = country;
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
        return defineToString(
            getClass().getSimpleName(),
            get_id(),
            new String[] {
                Model.Fields._id,
                Fields._email,
                Fields._address,
                Fields._phones,
                Fields._country
            },
            new String[] {
                String.valueOf(get_id()),
                get_email(),
                get_address().toString(),
                get_phones().toString(),
                get_country().toString()
            }
        );
    }

    /**
     * Contains values that determines address attributes.
     */
    @Setter @Getter @Table(title = "addresses")
    public static class Address {

        private UUID _id;

        /**
         * The street and street number.
         */
        private String _street;

        /**
         * The floor, if in an apartment, also include left or right.
         */
        private String _floor;

        private String _municipality;

        /**
         * Some digits describing the city.
         */
        private String _zip;

        /**
         * The city of the postal.
         */
        private String _city;

        /**
         * Converts into this DTO Object.
         * @param address The Object to be converted.
         */
        public Address(DTO address) {
            this(
                    address == null ? null : address.getId(),
                    address == null ? null : address.getStreet(),
                    address == null ? null : address.getFloor(),
                    address == null ? null : address.getMunicipality(),
                    address == null ? null : address.getZip(),
                    address == null ? null : address.getCity()
            );
        }

        public Address(UUID id, String street, String floor, String municipality, String zip, String city) {
            _id = id;
            _street = street;
            _floor = floor;
            _municipality = municipality;
            _zip = zip;
            _city = city;
        }

        @Override
        public String toString() {
            return String.join(", ", Arrays.stream(new String[] {
                    get_street(),
                    get_floor(),
                    get_municipality(),
                    get_zip(),
                    get_city()
            }).filter(Objects::nonNull).toList());
        }

        /**
         * The Data Transfer Object.
         * Is meant to be used as having common fields and be the body of Requests and Responses.
         * Doesn't have any logic.
         */
        @Getter @FieldNameConstants
        public static class DTO {

            private UUID id;

            /** The street and street number. */
            private String street;

            /** The floor, if in an apartment, also include left or right. */
            private String floor;

            private String municipality;

            /** Some digits describing the city. */
            private String zip;

            /** The city of the postal. */
            private String city;

            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            public DTO(
                    @JsonProperty UUID id,
                    @JsonProperty String street,
                    @JsonProperty String floor,
                    @JsonProperty String municipality,
                    @JsonProperty String zip,
                    @JsonProperty String city
            ) {
                this.id = id;
                this.street = street;
                this.floor = floor;
                this.municipality = municipality;
                this.zip = zip;
                this.city = city;
            }

            /**
             * Converts into this DTO Object.
             * @param address The Object to be converted.
             */
            public DTO(Address address) {
                this(
                        address.get_id(),
                        address.get_street(),
                        address.get_floor(),
                        address.get_municipality(),
                        address.get_zip(),
                        address.get_city()
                );
            }
        }
    }

    /**
     * An object with information about a curtain Country.
     */
    @Getter @ToString @Table(title = "countries")
    public static class Country {

        private UUID _id;

        /**
         * The name of the Country.
         */
        private String _title;

        /**
         * The value of the first few digits of a phone number.
         */
        private String _code;

        /**
         * Will translate a transport object of this object into a construct of this object.
         * @param country The transport object to be transformed.
         */
        public Country(Country.DTO country) {
            this(
                    country.getId(),
                    country.getTitle(),
                    country.getCode()
            );
        }

        public Country(UUID id, String title, String code) {
            _id = id;
            _title = title;
            _code = code;
        }

        /**
         * The Data Transfer Object.
         * Is meant to be used as having common fields and be the body of Requests and Responses.
         * Doesn't have any logic.
         */
        @Getter @Setter @FieldNameConstants
        public static class DTO {

            private UUID id;

            /** The name of the Country. */
            private String title;

            /** The value of the first few digits of a phone number. */
            private String code;

            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            public DTO(
                    @JsonProperty UUID id,
                    @JsonProperty String title,
                    @JsonProperty String code
            ) {
                this.id = id;
                this.title = title;
                this.code = code;
            }

            /**
             * Converts into this DTO Object.
             * @param country The Object to be converted.
             */
            public DTO(Country country) {
                this(
                        country.get_id(),
                        country.get_title(),
                        country.get_code()
                );
            }
        }
    }

    /**
     * Details about phone contacting information.
     */
    @Getter @ToString @Table(title = "phones")
    public static class Phone {

        /**
         * A country object, that represents the nationality of this PhoneNumber.
         */
        @Setter
        private int _countryDigits;

        /**
         * The contact numbers for the Phone.
         */
        @Setter
        private long _numbers;

        /**
         * True if the number is for a mobile.
         */
        private boolean _mobile;

        private boolean _business;

        /**
         * Will translate a transport object of this object into a construct of this object.
         * @param phone The transport object to be transformed.
         */
        public Phone(DTO phone) {
            this(
                    phone.getCountryDigits(),
                    phone.getNumbers(),
                    phone.isMobile(),
                    phone.isBusiness()
            );
        }

        public Phone(int countryDigits, long numbers, boolean mobile, boolean business) {
            _countryDigits = countryDigits;
            _numbers = numbers;
            _mobile = mobile;
            _business = business;
        }

        /**
         * The Data Transfer Object.
         * Is meant to be used as having common fields and be the body of Requests and Responses.
         * Doesn't have any logic.
         */
        @Getter @Setter @FieldNameConstants
        public static class DTO {

            /** A country object, that represents the nationality of this PhoneNumber. */
            private int countryDigits;

            /** The contact numbers for the Phone. */
            private long numbers;

            /** True if the number is for a mobile. */
            private boolean isMobile;

            private boolean isBusiness;

            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            public DTO(
                    @JsonProperty int countryDigits,
                    @JsonProperty long numbers,
                    @JsonProperty boolean isMobile,
                    @JsonProperty boolean isBusiness
            ) {
                this.countryDigits = countryDigits;
                this.numbers = numbers;
                this.isMobile = isMobile;
                this.isBusiness = isBusiness;
            }

            /**
             * Converts into this DTO Object.
             * @param phone The Object to be converted.
             */
            public DTO(Phone phone) {
                this(
                        phone.get_countryDigits(),
                        phone.get_numbers(),
                        phone.is_mobile(),
                        phone.is_business()
                );
            }
        }
    }


    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter @FieldNameConstants
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DTO {

        private UUID id;

        /** The email that the User wants to be contacted through outside the application. */
        private String email;

        /** A Phone object that is used to have information about how to contact the User through Phone. */
        private Set<Phone.DTO> phones;

        /** An Address object with info about the location of the User. */
        private Address.DTO address;

        /** A Country object for the information of which Country the User is living in. */
        private Country.DTO country;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public DTO(
                @JsonProperty(Fields.id) UUID id,
                @JsonProperty(Fields.email) String email,
                @JsonProperty(Fields.phones) Set<Phone.DTO> phones,
                @JsonProperty(Fields.address) Address.DTO address,
                @JsonProperty(Fields.country) Country.DTO country
        ) {
            this.id = id;
            this.email = email;
            this.phones = phones;
            this.address = address;
            this.country = country;
        }

        /**
         * Converts into this DTO Object.
         * @param contactInfo The Object to be converted.
         */
        public DTO(ContactInfo contactInfo) {
            this(
                    contactInfo.get_id(),
                    contactInfo.get_email(),
                    contactInfo.get_phones().asSet(Phone.DTO::new),
                    new Address.DTO(contactInfo.get_address()),
                    new Country.DTO(contactInfo.get_country())
            );
        }
    }
}
