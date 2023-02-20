package laustrup.bandwichpersistence.repositories.sub_repositories;

import laustrup.bandwichpersistence.models.users.Login;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.subscriptions.Card;
import laustrup.bandwichpersistence.models.users.subscriptions.Subscription;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.repositories.Repository;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Are handling Repository actions for User's common uses.
 * Extends the abstract Repository class,
 * that handles database connections and
 * uses JDBC to perform SQLs created by this class.
 */
public class UserRepository extends Repository {

    /**
     * Singleton instance of the Repository.
     */
    private static UserRepository _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static UserRepository get_instance() {
        if (_instance == null) _instance = new UserRepository();
        return _instance;
    }

    private UserRepository() {}

    /**
     * Will collect a JDBC ResultSet of all Users from the database, by using a SQL statement.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet get() {
        return get("");
    }

    /**
     * Will collect a JDBC ResultSet of a User from the database, by using a SQL statement.
     * Checks if the username is an email, but if username is email, it needs to be checked if it is null,
     * since it is declared to be needing an email to log in with an email.
     * @param login An object containing username and password.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet get(Login login) {
        return login.usernameIsEmailKind() ?
                get("WHERE users.email = '" + login.get_username() +
                        "' AND users.`password` = " + login.get_password() + "';")
                : get("WHERE users.username = '" + login.get_username() +
                        "' AND users.`password` = '" + login.get_password() + "';");
    }

    /**
     * Will collect a JDBC ResultSet of a User from the database, by using a SQL statement.
     * @param id The id of the User, that is wished to be found.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet get(long id) {
        return get("WHERE users.id = " + id);
    }

    /**
     * Will collect a JDBC ResultSet of several Users from the database, by using a SQL statement.
     * @param ids The ids of the Users, that is wished to be found.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet get(Liszt<Long> ids) {
        if (ids.isEmpty())
            return null;

        StringBuilder where = new StringBuilder("WHERE ");

        for (int i = 1; i <= ids.size(); i++) {
            where.append("users.id = ").append(ids.get(i));
            if (i < ids.size())
                where.append(" OR ");
        }

        return get(where.toString());
    }

    /**
     * Will collect a JDBC ResultSet of all Users that has something in common with a search query from the database,
     * by using a SQL statement.
     * It will compare columns of usernames, firstnames, lastnames and descriptions.
     * Doesn't order them by relevance at the moment.
     * @param query The search query, that is a line that should have something in common with columns.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet search(String query) {
        query = query.replaceAll("%","");
        return get("WHERE users.username LIKE '%" + query + "%' OR " +
                "users.first_name LIKE '%" + query + "%' OR " +
                "users.last_name LIKE '%" + query + "%' OR " +
                "users.`description` LIKE '%" + query + "%'");
    }

    /**
     * This is the method that can through a SQL statement find and collect the User.
     * Both from an id/ids, login or a search query, that is given from other public methods.
     * @param where The where statement, that decides what information that is being looked for.
     * @return The collected JDBC ResultSet.
     */
    private ResultSet get(String where) {
        return read("SELECT * FROM users " +
                "LEFT JOIN band_members ON band_members.artist_id = users.id OR band_members.band_id = users.id " +
                "LEFT JOIN gear ON gear.user_id = users.id " +
                "LEFT JOIN venues ON venues.user_id = users.id " +
                "LEFT JOIN `events` ON `events`.venue_id = users.id " +
                "LEFT JOIN gigs ON gigs.event_id = `events`.id " +
                "LEFT JOIN acts ON acts.gig_id = gigs.id OR acts.user_id = users.id " +
                "LEFT JOIN participations ON participations.event_id = `events`.id " +
                "LEFT JOIN followings ON followings.fan_id = users.id OR followings.idol_id = users.id " +
                "LEFT JOIN chatters ON chatters.user_id = users.id " +
                "LEFT JOIN chat_rooms ON chatters.chat_room_id = chat_rooms.id " +
                "LEFT JOIN bulletins ON users.id = bulletins.user_id " +
                "LEFT JOIN requests ON users.id = requests.user_id " +
                "LEFT JOIN ratings ON users.id = ratings.appointed_id " +
                "LEFT JOIN albums ON users.id = albums.author_id " +
                "LEFT JOIN album_items ON albums.id = album_items.album_id " +
                "INNER JOIN subscriptions ON users.id = subscriptions.user_id " +
                "INNER JOIN contact_informations ON users.id = contact_informations.user_id " +
                where + ";");
    }

    /**
     * Will insert both ContactInformation and a generated Subscription.
     * Is meant to be used, when a User will be inserted
     * @param user The User that has been inserted.
     * @return True if any rows has been affected.
     */
    public boolean createSubscriptionAndContactInfo(User user) {
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
                    user.get_primaryId() +
                    ",'ACCEPTED'" +
                    ",'FREEMIUM'" +
                    ",NULL" +
                    ",NULL" +
                    ",NULL" +
                    ",NULL" +
                "); " +
                "INSERT INTO contact_informations(" +
                    "user_id," +
                    "first_digits," +
                    "phone_number," +
                    "phone_is_mobile," +
                    "street," +
                    "floor," +
                    "postal," +
                    "city," +
                    "country_title," +
                    "country_indexes" +
                ") " +
                "VALUES (" +
                    user.get_primaryId() + "," +
                    user.get_contactInfo().get_phone().get_country().get_firstPhoneNumberDigits() + "," +
                    user.get_contactInfo().get_phone().get_numbers() + "," +
                    user.get_contactInfo().get_phone().is_mobile() + ",'" +
                    user.get_contactInfo().get_address().get_street() + "','" +
                    user.get_contactInfo().get_address().get_floor() + "','" +
                    user.get_contactInfo().get_address().get_postal() + "','" +
                    user.get_contactInfo().get_address().get_city() + "','" +
                    user.get_contactInfo().get_country().get_title() + "','" +
                    user.get_contactInfo().get_country().get_indexes() + "'" +
                ");");
    }

    /**
     * Will delete User by its id and all child tables with its foreign key cascade.
     * Closes connection.
     * @param user The User that should be deleted.
     * @return True if connection is closed and the User doesn't exist.
     */
    public boolean delete(User user) {
        return delete(user.get_primaryId(), "users", "id", true);
    }

    /**
     * Inserts a new following between a fan and idol.
     * Doesn't close connection.
     * @param fan The User that is following an idol.
     * @param idol The User that is followed by a fan.
     * @return True if it is a success.
     */
    public boolean insert(User fan, User idol) {
        return edit("INSERT IGNORE INTO followings(" +
                    "fan_id," +
                    "fan_kind," +
                    "idol_id," +
                    "idol_kind" +
                ") " +
                "VALUES(" +
                    fan.get_primaryId() + ",'" +
                    determineClass(fan) + "'," +
                    idol.get_primaryId() + ",'" +
                    determineClass(idol) + "'" +
                ");");
    }

    /**
     * Will determine which class the User is of.
     * @param user The specific User of determination.
     * @return A String of the class name.
     */
    private String determineClass(User user) {
        return (user.getClass() == Band.class ? "BAND" :
                    user.getClass() == Artist.class ? "ARTIST" :
                            user.getClass() == Venue.class ? "VENUE" :
                                    user.getClass() == Participant.class ? "PARTICIPANT" : "");
    }

    /**
     * Will remove a following relation.
     * Doesn't closes Connection.
     * @param fan The User that is following an idol.
     * @param idol The User that is followed by a fan.
     * @return True if it is a success.
     */
    public boolean remove(User fan, User idol) {
        return delete("followings", fan.get_primaryId(), "fan_id",
                idol.get_primaryId(), "idol_id", false);
    }

    /**
     * Updates a User table of values username, first_name, last_name and description.
     * Doesn't close connection.
     * @param user The User with values that will be updated and an id.
     * @param login Is needed to insure the User has access to this update.
     * @param password A password that will be changed.
     * @return True if it is a success.
     */
    public boolean update(User user, Login login, String password) {
        return edit("UPDATE users SET " +
                    "username = '" + user.get_username() + "' " +
                    ",`password` = '" + password + "' " +
                    ",first_name = '" + user.get_firstName() + "' " +
                    ",last_name = '" + user.get_lastName() + "' " +
                    ",`description` = '" + user.get_description() + "' " +
                "WHERE " +
                    "id = " + user.get_primaryId() + " AND " +
                    "`password` = '" + login.get_password() +
                "'; " +
                        (user.getClass() == Artist.class
                                || user.getClass() == Band.class
                                || user.getClass() == Venue.class ? updateGearSQL(user) : "") +
                        (user.getClass() == Venue.class ? updateVenueSQL((Venue) user) : "") +
                        (user.get_contactInfo() != null ? updateContactInfoSQL(user) : "")
        );
    }

    /**
     * Generates a SQL statement for updating contact informations such as
     * first_digits, phone_number, phone_is_mobile, street, floor, postal
     * city, country_title, and country indexes.
     * @param user The User with the values to update in database.
     * @return The generated SQL statement.
     */
    private String updateContactInfoSQL(User user) {
        ContactInfo info = user.get_contactInfo();
        return "UPDATE contact_informations SET " +
                    "first_digits = " + info.get_country().get_firstPhoneNumberDigits() + ", " +
                    "phone_number = " + info.get_phone().get_numbers() + ", " +
                    "phone_is_mobile = " + info.get_phone().is_mobile() + ", " +
                    "street = '" + info.get_address().get_street() + "', " +
                    "floor = '" + info.get_address().get_floor() + "', " +
                    "postal = '" + info.get_address().get_postal() + "', " +
                    "city = '" + info.get_address().get_city() + "', " +
                    "country_title = '" + info.get_country().get_title() + "', " +
                    "country_indexes = '" + info.get_country().get_indexes() + "' " +
                "WHERE " +
                    "user_id = " + user.get_primaryId() + "; " +
                "UPDATE users SET " +
                    "email = '" + info.get_email() + "' " +
                "WHERE " +
                    "users.id = " + user.get_primaryId() + "; ";
    }

    /**
     * Generates a SQL statement for updating gear descriptions of a User.
     * @param user The User with the gear description to update in database.
     * @return The generated SQL statement.
     */
    private String updateGearSQL(User user) {
        return "UPDATE gear SET " +
                    "`description` = '" +
                        (user.getClass() == Artist.class ? ((Artist) user).get_runner()
                            : user.getClass() == Band.class ? ((Band) user).get_runner()
                                : user.getClass() == Venue.class ? ((Venue) user).get_gearDescription() : "") + "' " +
                "WHERE user_id = " + user.get_primaryId() + "; ";
    }

    /**
     * Generates a SQL statement for updating a Venue of values size and location.
     * @param venue The Venue with the values to update in database.
     * @return The generated SQL statement.
     */
    private String updateVenueSQL(Venue venue) {
        return "UPDATE venues SET " +
                    "`size` = " + venue.get_size() + ", " +
                    "location = '" + venue.get_location() + "' " +
                "WHERE user_id = " + venue.get_primaryId() +"; ";
    }

    /**
     * Upserts a Card, if it doesn't exist, it will insert it,
     * otherwise update it.
     * Doesn't close Connection.
     * @param card The Card with the values to update in Database.
     * @return A ResultSet with generated values, if they are genereated. Otherwise, null.
     */
    public ResultSet upsert(Card card) {
        try {
            return create("INSERT INTO cards(" +
                        (card.get_id() > 0 ? "id," : "") +
                        "`type`," +
                        "`owner`," +
                        "numbers," +
                        "expiration_month," +
                        "expiration_year," +
                        "cvv" +
                    ") " +
                    "VALUES(" +
                        (card.get_id() > 0 ? card.get_id()+",'" : "'") +
                        card.get_type() + "','" +
                        card.get_owner() + "'," +
                        card.get_cardNumbers() + "," +
                        card.get_expirationMonth() + "," +
                        card.get_expirationMonth() + "," +
                        card.get_cVV() +
                    ");" +
                    "ON DUPLICATE KEY UPDATE " +
                        "`type` = '" + card.get_type() + "', " +
                        "`owner` = '" + card.get_owner() + "', " +
                        "numbers = " + card.get_cardNumbers() + ", " +
                        "expiration_month = " + card.get_expirationMonth() + ", " +
                        "expiration_year = " + card.get_expirationYear() + ", " +
                        "cvv = " + card.get_cVV() +
                    "; ").getGeneratedKeys();
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't generate key for card...", e);
        }

        return null;
    }

    /**
     * Upserts a Subscription, if it doesn't exist, it will insert it,
     * otherwise update it.
     * Doesn't close Connection.
     * @param subscription The Subscription with the values to update in database.
     * @return True if it is a success.
     */
    public boolean upsert(Subscription subscription) {
        return edit("INSERT INTO subscriptions(" +
                    (subscription.get_primaryId() > 0 ? "user_id," : "") +
                    "`status`," +
                    "subscription_type," +
                    "offer_type," +
                    "offer_expires," +
                    "offer_effect," +
                    "card_id" +
                ") " +
                "VALUES(" +
                    (subscription.get_primaryId() > 0 ? subscription.get_user().get_primaryId()+",'" : "'") +
                    subscription.get_situation() + "','" +
                    subscription.get_type() + "','" +
                    subscription.get_offer().get_type() + "','" +
                    subscription.get_offer().get_expires() + "'," +
                    subscription.get_offer().get_effect() + "," +
                    subscription.get_cardId() +
                ") " +
                "ON DUPLICATE KEY UPDATE " +
                    "`status` = '" + subscription.get_situation() + "', " +
                    "subscription_type = '" + subscription.get_type() + "', " +
                    "offer_type = '" + subscription.get_offer().get_type() + "', " +
                    "offer_expires = '" + subscription.get_offer().get_expires() + "', " +
                    "offer_effect = " + subscription.get_offer().get_effect() + ", " +
                    "card_id = " + subscription.get_cardId() +
                "; ");
    }

    public String insertUserSQL(User user, String password) {
        return "INSERT INTO users(" +
                    "username," +
                    "email," +
                    "`password`," +
                    "first_name,last_name," +
                    "`description`," +
                    "kind," +
                    "`timestamp`)" +
                "VALUES (" +
                    varCharColumn(user.get_username()) + "," +
                    varCharColumn(user.get_contactInfo().get_email()) + "," +
                    varCharColumn(password) + "," +
                    varCharColumn(user.get_firstName()) +"," +
                    varCharColumn(user.get_lastName()) +"," +
                    varCharColumn(user.get_description()) +"," +
                    varCharColumn(user.get_authority().toString()) +
                ",NOW());";
    }
}
