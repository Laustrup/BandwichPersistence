package laustrup.bandwichpersistence.core.models.users;

import laustrup.bandwichpersistence.core.models.Model;

import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * Contains information that people need in order to contact the User.
 */
@Getter @FieldNameConstants
public class ContactInfo extends Model {

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
        super(contactInfo);
        _email = contactInfo.getEmail();
        _address = new Address(contactInfo.getAddress());
        _country = new Country(contactInfo.getCountry());

        for (Phone.DTO phone : contactInfo.getPhones())
            _phones.add(new Phone(phone));
    }

    /**
     * Generates a timestamp of now.
     * @param id The primary id that identifies this unique Object.
     * @param email The email that the User wants to be contacted through outside the application.
     * @param phones A Phone object that is used to have information about how to contact the User through Phone.
     * @param address An Address object with info about the location of the User.
     * @param country A Country object for the information of which Country the User is living in.
     */
    public ContactInfo(UUID id, String email, Seszt<Phone> phones, Address address, Country country) {
        _id = id;
        _title = "Contact-info: " + id;
        _email = email;
        _phones = phones;
        _address = address;
        _country = country;
    }

    /**
     * A constructor with all the values of this Object.
     * @param id The primary id that identifies this unique Object.
     * @param email The email that the User wants to be contacted through outside the application.
     * @param phones A Phone object that is used to have information about how to contact the User through Phone.
     * @param address An Address object with info about the location of the User.
     * @param country A Country object for the information of which Country the User is living in.
     * @param timestamp The date and time this ContactInfo was created.
     */
    public ContactInfo(UUID id, String email, Seszt<Phone> phones, Address address, Country country, Instant timestamp) {
        _id = id;
        _title = "Contact-info: " + id;
        _email = email;
        _phones = phones;
        _address = address;
        _country = country;
        _timestamp = timestamp;
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
                _country.toString()
            }
        );
    }

    /**
     * Contains values that determines address attributes.
     */
    @Setter @Getter @FieldNameConstants
    public static class Address {

        /**
         * The street and street number.
         */
        private String _street;

        /**
         * The floor, if in an apartment, also include left or right.
         */
        private String _floor;

        /**
         * Some digits describing the city.
         */
        private String _postal;

        /**
         * The city of the postal.
         */
        private String _city;

        /**
         * Converts into this DTO Object.
         * @param address The Object to be converted.
         */
        public Address(DTO address) {
            _street = address.getStreet();
            _floor = address.getFloor();
            _postal = address.getPostal();
            _city = address.getCity();
        }

        /**
         * A constructor with all the values of this Object.
         * @param street The street and street number.
         * @param floor The floor, if in an apartment, also include left or right.
         * @param postal Some digits describing the city.
         * @param city The city of the postal.
         */
        public Address(String street, String floor, String postal, String city) {
            _street = street;
            _floor = floor;
            _postal = postal;
            _city = city;
        }

        @Override
        public String toString() {
            return String.join(", ", Arrays.stream(new String[] {
                    get_street(),
                    get_floor(),
                    get_postal(),
                    get_city()
            }).filter(Objects::nonNull).toList());
        }

        /**
         * The Data Transfer Object.
         * Is meant to be used as having common fields and be the body of Requests and Responses.
         * Doesn't have any logic.
         */
        @Getter @Setter
        public static class DTO {

            /** The street and street number. */
            private String street;

            /** The floor, if in an apartment, also include left or right. */
            private String floor;

            /** Some digits describing the city. */
            private String postal;

            /** The city of the postal. */
            private String city;

            /**
             * Converts into this DTO Object.
             * @param address The Object to be converted.
             */
            public DTO(Address address) {
                street = address.get_street();
                floor = address.get_floor();
                postal = address.get_postal();
                city = address.get_city();
            }
        }
    }

    /**
     * An object with information about a curtain Country.
     */
    @Getter @ToString
    @FieldNameConstants
    public static class Country {

        /**
         * The name of the Country.
         */
        private String _title;

        /**
         * The value of the first few digits of a phone number.
         */
        private int _firstPhoneNumberDigits;

        /**
         * Will translate a transport object of this object into a construct of this object.
         * @param country The transport object to be transformed.
         */
        public Country(Country.DTO country) {
            _title = country.getTitle();
            _firstPhoneNumberDigits = country.getFirstPhoneNumberDigits();
        }

        /**
         The primary id that identifies this unique Object.
         * @param title The name of the Country.
         * @param firstPhoneNumberDigits The value of the first few digits of a phone number.
         */
        public Country(String title, int firstPhoneNumberDigits) {
            _title = title;
            _firstPhoneNumberDigits = firstPhoneNumberDigits;
        }

        /**
         * The Data Transfer Object.
         * Is meant to be used as having common fields and be the body of Requests and Responses.
         * Doesn't have any logic.
         */
        @Getter @Setter
        public static class DTO {

            /** The name of the Country. */
            private String title;

            /** The value of the first few digits of a phone number. */
            private int firstPhoneNumberDigits;

            /**
             * Converts into this DTO Object.
             * @param country The Object to be converted.
             */
            public DTO(Country country) {
                title = country.get_title();
                firstPhoneNumberDigits = country.get_firstPhoneNumberDigits();
            }
        }
    }

    /**
     * Details about phone contacting information.
     */
    @Getter @ToString
    public static class Phone {

        /**
         * A country object, that represents the nationality of this PhoneNumber.
         */
        @Setter
        private Country _country;

        /**
         * The contact numbers for the Phone.
         */
        @Setter
        private long _numbers;

        /**
         * True if the number is for a mobile.
         */
        private boolean _mobile;

        /**
         * Will translate a transport object of this object into a construct of this object.
         * @param phone The transport object to be transformed.
         */
        public Phone(DTO phone) {
            _country = new Country(phone.getCountry());
            _numbers = phone.getNumbers();
            _mobile = phone.isMobile();
        }

        /**
         * The primary id that identifies this unique Object.
         * @param country A country object, that represents the nationality of this PhoneNumber.
         * @param numbers The contact numbers for the Phone.
         * @param mobile True if the number is for a mobile.
         */
        public Phone(Country country, long numbers, boolean mobile) {
            _country = country;
            _numbers = numbers;
            _mobile = mobile;
        }

        /**
         * The Data Transfer Object.
         * Is meant to be used as having common fields and be the body of Requests and Responses.
         * Doesn't have any logic.
         */
        @Getter @Setter
        public static class DTO {

            /** A country object, that represents the nationality of this PhoneNumber. */
            private Country.DTO country;

            /** The contact numbers for the Phone. */
            private long numbers;

            /** True if the number is for a mobile. */
            private boolean mobile;

            /**
             * Converts into this DTO Object.
             * @param phone The Object to be converted.
             */
            public DTO(Phone phone) {
                country = new ContactInfo.Country.DTO(phone.get_country());
                numbers = phone.get_numbers();
                mobile = phone.is_mobile();
            }
        }
    }


    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter
    public static class DTO extends ModelDTO {

        /** The email that the User wants to be contacted through outside the application. */
        private String email;

        /** A Phone object that is used to have information about how to contact the User through Phone. */
        private Phone.DTO[] phones;

        /** An Address object with info about the location of the User. */
        private Address.DTO address;

        /** A Country object for the information of which Country the User is living in. */
        private Country.DTO country;

        /**
         * Converts into this DTO Object.
         * @param contactInfo The Object to be converted.
         */
        public DTO(ContactInfo contactInfo) {
            super(contactInfo);
            email = contactInfo.get_email();
            address = new Address.DTO(contactInfo.get_address());
            country = new Country.DTO(contactInfo.get_country());

            phones = new Phone.DTO[contactInfo.get_phones().size()];
            for (int i = 0; i < phones.length; i++)
                phones[i] = new Phone.DTO(contactInfo.get_phones().get(i));
        }
    }
}
