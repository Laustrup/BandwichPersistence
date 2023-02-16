package laustrup.bandwichpersistence.repositories.sub_repositories;

import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.albums.AlbumItem;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistence.repositories.Repository;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

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
        if (contactInfo != null && contactInfo.get_primaryId() > 0)
            return edit("INSERT INTO contact_informations(" +
                        "user_id," +
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
                        contactInfo.get_primaryId() + "," +
                        contactInfo.get_country().get_firstPhoneNumberDigits() + "," +
                        contactInfo.get_phone().get_numbers() + "," +
                        contactInfo.get_phone().is_mobile() + "," +
                        varCharColumn(contactInfo.get_address().get_street()) + "," +
                        varCharColumn(contactInfo.get_address().get_floor()) + "," +
                        varCharColumn(contactInfo.get_address().get_postal()) + "," +
                        varCharColumn(contactInfo.get_address().get_city()) + "," +
                        varCharColumn(contactInfo.get_country().get_title()) + "," +
                        varCharColumn(contactInfo.get_country().get_indexes().toString()) +
                    ") " +
                    "ON DUPLICATE KEY UPDATE " +
                        "email = " + varCharColumn(contactInfo.get_email()) + "," +
                        "first_digits = " + contactInfo.get_country().get_firstPhoneNumberDigits() + "," +
                        "phone_number = " + contactInfo.get_phone().get_numbers() + "," +
                        "phone_is_mobile = " + contactInfo.get_phone().is_mobile() + "," +
                        "street = " + varCharColumn(contactInfo.get_address().get_street()) + "," +
                        "floor = " + varCharColumn(contactInfo.get_address().get_floor()) + "," +
                        "postal = " + varCharColumn(contactInfo.get_address().get_postal()) + "," +
                        "city = " + varCharColumn(contactInfo.get_address().get_city()) + "," +
                        "country_title = " + varCharColumn(contactInfo.get_country().get_title()) + "," +
                        "country_indexes = " + varCharColumn(contactInfo.get_country().get_indexes().toString()) +
                    ";");
        return false;
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
        if (subscription != null && subscription.get_user().get_primaryId() > 0)
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
                        subscription.get_user().get_primaryId() + "," +
                        varCharColumn(subscription.get_status().toString()) + "," +
                        varCharColumn(subscription.get_type().toString()) + "," +
                        varCharColumn(subscription.get_offer().get_type().toString()) + "," +
                        dateTimeColumn(subscription.get_offer().get_expires()) + "," +
                        subscription.get_offer().get_effect() + "," +
                        subscription.get_cardId() +
                    ") " +
                    "ON DUPLICATE KEY UPDATE " +
                        "`status` = " + varCharColumn(subscription.get_status().toString()) + "," +
                        "subscription_type = " + varCharColumn(subscription.get_type().toString()) + "," +
                        "offer_type = " + varCharColumn(subscription.get_offer().get_type().toString()) + "," +
                        "offer_expires = " + dateTimeColumn(subscription.get_offer().get_expires()) + "," +
                        "offer_effect = " + subscription.get_offer().get_effect() + "," +
                        "card_id = " + subscription.get_cardId() +
                    ";");
        return false;
    }

    /**
     * Upserts a Rating.
     * It will insert the values of the Rating if it doesn't exist,
     * otherwise it will update the value of the Rating.
     * Will not close connection.
     * @param rating The Rating that will have influence on the database table.
     * @return True if it is a success.
     */
    public boolean upsert(Rating rating) {
        return edit("INSERT INTO ratings(" +
                    "appointed_id," +
                    "judge_id," +
                    "`value`," +
                    "`timestamp`" +
                ") " +
                "VALUES(" +
                    rating.get_appointed().get_primaryId() + "," +
                    rating.get_judge().get_primaryId() + "," +
                    rating.get_value() + "," +
                "NOW()) " +
                "ON DUPLICATE KEY UPDATE " +
                    "`value` = " + rating.get_value() +
                ";");
    }

    /**
     * Upserts an Album and its items.
     * It will insert the values of the Album and its items if they don't exist,
     * otherwise it will update the values instead.
     * In case it did insert, it will generate a key.
     * Will not close connection.
     * @param album The Album with its items that will have influence on the database table.
     * @return The generated key of Album.
     */
    public long upsert(Album album) {
        try {
            boolean idExists = album.get_primaryId() > 0;
            ResultSet set =  create("INSERT INTO albums(" +
                        (idExists ? "id," : "") +
                        "title," +
                        "author_id," +
                        "`timestamp`" +
                    ") " +
                    "VALUES(" +
                        (idExists ? album.get_primaryId()+"," : "") +
                        varCharColumn(album.get_title()) + "," +
                        album.get_author().get_primaryId() + "," +
                    "NOW()) " +
                    "ON DUPLICATE KEY UPDATE " +
                        "title = " + varCharColumn(album.get_title()) +
                    "; ").getGeneratedKeys();
            set.next();
            long id = set.getLong(1);

            edit(upsertAlbumItemsSql(album));

            return id;
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't upsert Album " + album.get_title() + "...",e);
        }
        return 0;
    }

    /**
     * Generates an Album item upsert SQL statement.
     * The SQL will insert the values of the items if they don't exist,
     * otherwise it will update the values instead.
     * @param album The Album that has the items.
     * @return The generated SQL string.
     */
    @SuppressWarnings("All")
    private String upsertAlbumItemsSql(Album album) {
        String sql = new String();
        for (AlbumItem item : album.get_items())
            sql += "INSERT IGNORE INTO album_items(" +
                        "title," +
                        "endpoint," +
                        "kind," +
                        "album_id," +
                        (item.get_event() != null ? "event_id," : "") +
                        "timestamp" +
                    ") VALUES(" +
                        varCharColumn(item.get_title()) + "," +
                        varCharColumn(item.get_endpoint()) + "," +
                        varCharColumn(item.get_kind().toString()) + "," +
                        album.get_primaryId() + "," +
                        (item.get_event() != null ? item.get_event().get_primaryId() + "," : "") +
                    "NOW()); " +
                        upsertTags(item);
        return sql;
    }

    /**
     * Generates a upsert SQL for tags of an Album item.
     * The SQL will insert the values of the tags if they don't exist,
     * otherwise it will update the values instead.
     * @param item The items that has the tags.
     * @return The generated SQL string.
     */
    @SuppressWarnings("All")
    private String upsertTags(AlbumItem item) {
        String sql = "";
        for (User tag : item.get_tags())
            sql += "INSERT IGNORE INTO tags(" +
                    "user_id," +
                    "item_endpoint" +
                    ") " +
                    "VALUES(" +
                        tag.get_primaryId() + "," +
                        varCharColumn(item.get_endpoint()) + "" +
                    "); ";
        return sql;
    }
}