package laustrup.bandwichpersistence.repositories.sub_repositories;

import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.subscriptions.Subscription;
import laustrup.bandwichpersistence.repositories.Repository;
import laustrup.bandwichpersistence.utilities.Liszt;

import java.sql.ResultSet;

/**
 * Are handling Repository actions for objects that aren't of a specific User.
 * Extends the abstract Repository class,
 * that handles database connections and
 * uses JDBC to perform SQLs created by this class.
 */
public class ModelRepository extends Repository {

    /**
     * Singleton instance of the Repository.
     */
    private static ModelRepository _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ModelRepository get_instance() {
        if (_instance == null) _instance = new ModelRepository();
        return _instance;
    }

    private ModelRepository() {}

    public ResultSet chatRooms(Liszt<Long> ids) {
        StringBuilder where = new StringBuilder("WHERE ");

        for (int i = 1; i <= ids.size(); i++) {
            where.append("chat_rooms.id = ").append(ids.get(i));
            if (i < ids.size())
                where.append(" OR ");
        }

        return read("SELECT * FROM chat_rooms " +
                "INNER JOIN chatters ON chat_rooms.id = chatters.chat_room_id " +
                "INNER JOIN mails ON chat_rooms.id = mails.chat_room id " +
                where + ";");
    }

    public ResultSet bulletins(Liszt<Long> ids) {
        StringBuilder where = new StringBuilder("WHERE ");

        for (int i = 1; i <= ids.size(); i++) {
            where.append("user_bulletins.id = ").append(ids.get(i));
            if (i < ids.size())
                where.append(" OR ");
        }

        return read("SELECT * FROM user_ " +
                "INNER JOIN chatters ON chat_rooms.id = chatters.chat_room_id " +
                "INNER JOIN mails ON chat_rooms.id = mails.chat_room id " +
                where + ";");
    }

    /**
     * Will collect a JDBC ResultSet of a Card from the database, by using a SQL statement.
     * @param id The id of the Card, that is wished to be found.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet card(long id) {
        return read("SELECT * FROM cards WHERE id = " + id + ";");
    }

    /**
     * Upserts contact informations depending on the user_id/contact_id.
     * This means it will insert the values of the contact informations if they don't exist,
     * otherwise it will update them to the values of the contact informations.
     * Will not close connection.
     * @param contactInfo The contact informations that will have influence on the database table.
     * @return True if any rows have been affected.
     */
    public boolean upsert(ContactInfo contactInfo) {
        return edit("INSERT INTO contact_informations(" +
                    "user_id," +
                    "email," +
                    "first_digits," +
                    "phone_number," +
                    "phone_is_mobile," +
                    "street," +
                    "floor," +
                    "postal," +
                    "city," +
                    "country_title," +
                    "country_indexes," +
                ") " +
                "VALUES (" +
                    contactInfo.get_primaryId() + ",'" +
                    contactInfo.get_email() + "'," +
                    contactInfo.get_country().get_firstPhoneNumberDigits() + "," +
                    contactInfo.get_phone().get_numbers() + "," +
                    contactInfo.get_phone().is_mobile() + ",'" +
                    contactInfo.get_address().get_street() + "','" +
                    contactInfo.get_address().get_floor() + "','" +
                    contactInfo.get_address().get_postal() + "','" +
                    contactInfo.get_address().get_city() + "','" +
                    contactInfo.get_country().get_title() + "','" +
                    contactInfo.get_country().get_indexes() +
                "') " +
                "ON DUPLICATE KEY UPDATE " +
                    "email = '" + contactInfo.get_email() + "'," +
                    "first_digits = " + contactInfo.get_country().get_firstPhoneNumberDigits() + "," +
                    "phone_number = " + contactInfo.get_phone().get_numbers() + "," +
                    "phone_is_mobile = " + contactInfo.get_phone().is_mobile() + "," +
                    "street = '" + contactInfo.get_address().get_street() + "'," +
                    "floor = '" + contactInfo.get_address().get_floor() + "'," +
                    "postal = '" + contactInfo.get_address().get_postal() + "'," +
                    "city = '" + contactInfo.get_address().get_city() + "'," +
                    "country_title = '" + contactInfo.get_country().get_title() + "'," +
                    "country_indexes = '" + contactInfo.get_country().get_indexes() +
                ";", false);
    }

    /**
     * Upserts Subscription depending on the user_id/subscription_id.
     * This means it will insert the values of the Subscription if they don't exist,
     * otherwise it will update them to the values of the Subscription.
     * Will not close connection.
     * @param subscription The Subscription that will have influence on the database table.
     * @return True if any rows have been affected.
     */
    public boolean upsert(Subscription subscription) {
        return edit("INSERT INTO subscriptions(" +
                    "user_id," +
                    "`status`," +
                    "subscription_type," +
                    "offer_type," +
                    "offer_expires," +
                    "offer_effect," +
                    "card_id" +
                ") " +
                "VALUES (" +
                    subscription.get_user().get_primaryId() + ",'" +
                    subscription.get_status() + "','" +
                    subscription.get_type() + "','" +
                    subscription.get_offer().get_type() + "','" +
                    subscription.get_offer().get_expires() + "'," +
                    subscription.get_offer().get_effect() + "," +
                    subscription.get_cardId() +
                ") " +
                "ON DUPLICATE KEY UPDATE " +
                    "`status` = '" + subscription.get_status() + "'," +
                    "subscription_type = '" + subscription.get_type() + "'," +
                    "offer_type = '" + subscription.get_offer().get_type() + "'," +
                    "offer_expires = '" + subscription.get_offer().get_expires() + "'," +
                    "offer_effect = " + subscription.get_offer().get_effect() + "," +
                    "card_id = " + subscription.get_cardId() +
                ";", false);
    }
}