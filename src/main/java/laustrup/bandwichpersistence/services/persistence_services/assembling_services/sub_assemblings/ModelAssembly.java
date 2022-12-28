package laustrup.bandwichpersistence.services.persistence_services.assembling_services.sub_assemblings;

import laustrup.bandwichpersistence.models.users.contact_infos.Address;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.contact_infos.Country;
import laustrup.bandwichpersistence.models.users.contact_infos.Phone;
import laustrup.bandwichpersistence.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistence.models.users.subscriptions.SubscriptionOffer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Is only used for database read functions.
 * Will build various model objects from database row values.
 * Is a singleton.
 */
public class ModelAssembly {

    /**
     * Singleton instance of the Service.
     */
    private static ModelAssembly _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ModelAssembly get_instance() {
        if (_instance == null) _instance = new ModelAssembly();
        return _instance;
    }

    private ModelAssembly() {}

    public ContactInfo assembleContactInfo(ResultSet set) throws SQLException {
        String table = "contact_informations";
        return new ContactInfo(set.getString(table+".user_id"),
                new Phone(new Country(set.getString(table+".country_title"),
                            Country.CountryIndexes.valueOf(set.getString(table+".country_indexes")),
                            set.getInt(table+".first_digits")),
                            set.getInt(table+".phone_number"),
                            set.getBoolean(table+".phone_is_mobile")),
                new Address(set.getString(table+".street"),
                        set.getString(table+".floor"),
                        set.getString(table+".postal"),
                        set.getString(table+".city")),
                new Country(set.getString(table+".country_title"),
                        Country.CountryIndexes.valueOf(set.getString(table+".country_indexes")),
                        set.getInt(table+".first_digits"))
        );
    }

    public Subscription assembleSubscription(ResultSet set) throws SQLException {
        String table = "subscriptions";
        return new Subscription(set.getLong(table+".id"), Subscription.Type.valueOf(set.getString(table+".subscription_type")),
                Subscription.Status.valueOf(set.getString(table+".status")),
                new SubscriptionOffer(set.getTimestamp(table+".offer_expires").toLocalDateTime(),
                        SubscriptionOffer.Type.valueOf(set.getString(table+".offer_type")),
                        set.getDouble(table+".offer_effect")
                        ),
                set.getLong(table+".card_id")
        );
    }
}
