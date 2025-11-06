package laustrup.bandwichpersistence.core.models.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.models.identification.CommonIdentity;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import java.util.Set;
import java.util.UUID;

import static laustrup.bandwichpersistence.core.services.ModelService.toStringify;
import static laustrup.bandwichpersistence.core.services.ObjectService.ifExists;
import static laustrup.bandwichpersistence.core.utilities.collections.Seszt.copy;

/**
 * Contains information that people need in order to contact the User.
 */
@Getter @FieldNameConstants @Table
public class ContactInfo {

  @Table.Column(isPrimary = true)
  private final Id _id;

  /**
   * The email that the User wants to be contacted through outside the application.
   */
  private final String _email;

  /**
   * A Phone object that is used to have information about how to contact the User through Phone.
   */
  private final Seszt<Phone> _phones;

  /**
   * An Address object with info about the location of the User.
   */
  @Setter
  private Address _address;

  /**
   * A Country object for the information of which Country the User is living in.
   */
  @Setter
  private Country _country;

  /**
   * Will translate a transport object of this object into a construct of this object.
   *
   * @param contactInfo The transport object to be transformed.
   */
  @Table.Constructor
  public ContactInfo(DTO contactInfo) {
    this(
        new Id(contactInfo.getId()),
        contactInfo.getEmail(),
        copy(contactInfo.getPhones(), Phone::new),
        new Address(contactInfo.getAddress()),
        new Country(contactInfo.getCountry())
    );
  }

  public ContactInfo(Id id, String email, Seszt<Phone> phones, Address address, Country country) {
    _id = id;
    _email = email;
    _phones = phones;
    _address = address;
    _country = country;
  }

  /**
   * Collects the details of the Address as a one liner String.
   *
   * @return The collected one liner String of the Address.
   */
  public String getAddressInfo() {
    return _address.toString();
  }

  public static class Id extends CommonIdentity<Signature.UUID> {

    public Id(Signature.UUID signature) {
      super(signature);
    }

    public Id(java.util.UUID signature) {
      super(new Signature.UUID(signature));
    }

    @Override
    public Class<?> getOwnerClassType() {
      return ContactInfo.class;
    }
  }

  @Override
  public String toString() {
    return toStringify(this);
  }

  /**
   * Contains values that determines address attributes.
   */
  @Setter
  @Getter
  @Table
  @FieldNameConstants
  public static class Address {

    @Table.Column(isPrimary = true)
    private Id _id;

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
     *
     * @param address The Object to be converted.
     */
    public Address(DTO address) {
      this(
          new Id(ifExists(address, DTO::id)),
          ifExists(address, DTO::street),
          ifExists(address, DTO::floor),
          ifExists(address, DTO::municipality),
          ifExists(address, DTO::zip),
          ifExists(address, DTO::city)
      );
    }

    @Table.Constructor
    public Address(Id id, String street, String floor, String municipality, String zip, String city) {
      _id = id;
      _street = street;
      _floor = floor;
      _municipality = municipality;
      _zip = zip;
      _city = city;
    }

    public static class Id extends CommonIdentity<Signature.UUID> {

      public Id(Signature.UUID signature) {
        super(signature);
      }

      public Id(java.util.UUID signature) {
        super(new Signature.UUID(signature));
      }

      @Override
      public Class<?> getOwnerClassType() {
        return Address.class;
      }
    }

    @Override
    public String toString() {
      return toStringify(this);
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     *
     * @param street The street and street number.
     * @param floor  The floor, if in an apartment, also include left or right.
     * @param zip    Some digits describing the city.
     * @param city   The city of the postal.
     */
        @FieldNameConstants
        public record DTO(UUID id, String street, String floor, String municipality, String zip, String city) {

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
           *
           * @param address The Object to be converted.
           */
          public DTO(Address address) {
            this(
                address.get_id().get_value(),
                address.get_street(),
                address.get_floor(),
                address.get_municipality(),
                address.get_zip(),
                address.get_city()
            );
          }
        }
  }

  @Table
  @FieldNameConstants
  public record Country(@Table.Column(isPrimary = true) Id id, String title, String code) {

    /**
     * Will translate a transport object of this object into a construct of this object.
     *
     * @param country The transport object to be transformed.
     */
    public Country(DTO country) {
      this(new Id(country.getId()), country.getTitle(), country.getCode());
    }

    @Table.Constructor
    public Country {
    }

    public static class Id extends CommonIdentity<Signature.UUID> {

      public Id(Signature.UUID signature) {
        super(signature);
      }

      public Id(UUID signature) {
        super(new Signature.UUID(signature));
      }

      @Override
      public Class<Country> getOwnerClassType() {
        return Country.class;
      }
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter
    @FieldNameConstants
    public static class DTO {

      private final UUID id;

      /**
       * The name of the Country.
       */
      private final String title;

      /**
       * The value of the first few digits of a phone number.
       */
      private final String code;

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
       *
       * @param country The Object to be converted.
       */
      public DTO(Country country) {
        this(
            country.id().get_value(),
            country.title(),
            country.code()
        );
      }
    }
  }

  /**
   * Details about phone contacting information.
   */
  @Getter
  @ToString
  @Table
  @FieldNameConstants
  public static class Phone {

    @Table.Column(isPrimary = true)
    public Id _id;

    /**
     * A country object, that represents the nationality of this PhoneNumber.
     */
    private final int _countryDigits;

    /**
     * The contact numbers for the Phone.
     */
    private final long _numbers;

    /**
     * True if the number is for a mobile.
     */
    private final boolean _mobile;

    private final boolean _business;

    /**
     * Will translate a transport object of this object into a construct of this object.
     *
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

    @Table.Constructor
    public Phone(int countryDigits, long numbers, boolean mobile, boolean business) {
      _countryDigits = countryDigits;
      _numbers = numbers;
      _mobile = mobile;
      _business = business;
    }

    public static class Id extends CommonIdentity<Signature.UUID> {

      public Id(Signature.UUID identifier) {
        super(identifier);
      }

      @Override
      public Class<?> getOwnerClassType() {
        return Phone.class;
      }
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter
    @FieldNameConstants
    public static class DTO {

      /**
       * A country object, that represents the nationality of this PhoneNumber.
       */
      private final int countryDigits;

      /**
       * The contact numbers for the Phone.
       */
      private final long numbers;

      /**
       * True if the number is for a mobile.
       */
      private final boolean isMobile;

      private final boolean isBusiness;

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
       *
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
  @Getter
  @Setter
  @FieldNameConstants
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class DTO {

    private java.util.UUID id;

    /**
     * The email that the User wants to be contacted through outside the application.
     */
    private String email;

    /**
     * A Phone object that is used to have information about how to contact the User through Phone.
     */
    private Set<Phone.DTO> phones;

    /**
     * An Address object with info about the location of the User.
     */
    private Address.DTO address;

    /**
     * A Country object for the information of which Country the User is living in.
     */
    private Country.DTO country;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DTO(
        @JsonProperty(Model.ModelDTO.Fields.id) java.util.UUID id,
        @JsonProperty(ContactInfo.DTO.Fields.email) String email,
        @JsonProperty(ContactInfo.DTO.Fields.phones) Set<Phone.DTO> phones,
        @JsonProperty(ContactInfo.DTO.Fields.address) Address.DTO address,
        @JsonProperty(ContactInfo.DTO.Fields.country) Country.DTO country
    ) {
      this.id = id;
      this.email = email;
      this.phones = phones;
      this.address = address;
      this.country = country;
    }

    /**
     * Converts into this DTO Object.
     *
     * @param contactInfo The Object to be converted.
     */
    public DTO(ContactInfo contactInfo) {
      this(
          contactInfo.get_id().get_value(),
          contactInfo.get_email(),
          contactInfo.get_phones().asSet(Phone.DTO::new),
          new Address.DTO(contactInfo.get_address()),
          new Country.DTO(contactInfo.get_country())
      );
    }
  }
}
