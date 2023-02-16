package laustrup.bandwichpersistence.repositories.sub_repositories;

import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.repositories.Repository;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Plato;
import laustrup.bandwichpersistence.utilities.Printer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatRepository extends Repository {

    /**
     * Singleton instance of the Repository.
     */
    private static ChatRepository _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static ChatRepository get_instance() {
        if (_instance == null) _instance = new ChatRepository();
        return _instance;
    }

    private ChatRepository() {}

    /**
     * Will collect a JDBC ResultSet of a ChatRooms from the database, by using a SQL statement.
     * @param ids The ids of the ChatRooms.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet chatRooms(Liszt<Long> ids) {
        if (ids.isEmpty())
            return null;

        StringBuilder where = new StringBuilder("WHERE ");

        for (int i = 1; i <= ids.size(); i++) {
            where.append("chat_rooms.id = ").append(ids.get(i));
            if (i < ids.size())
                where.append(" OR ");
        }

        return read("SELECT * FROM chat_rooms " +
                "INNER JOIN chatters ON chat_rooms.id = chatters.chat_room_id " +
                "LEFT JOIN mails ON chat_rooms.id = mails.chat_room_id " +
                where + ";");
    }

    /**
     * Will collect a JDBC ResultSet of a Bulletins from the database, by using a SQL statement.
     * @param ids The ids of the Bulletins.
     * @return The collected JDBC ResultSet.
     */
    public ResultSet bulletins(Liszt<Long> ids, boolean isUser) {
        StringBuilder where = new StringBuilder("WHERE ");

        for (int i = 1; i <= ids.size(); i++) {
            where.append("bulletins.id = ").append(ids.get(i));
            if (i < ids.size())
                where.append(" OR ");
        }

        String table = (isUser ? "bulletins " : "bulletins ");
        return read("SELECT * FROM " + table +
                "INNER JOIN users ON " + table +".author_id = users.id OR " + table + ".receiver_id = users.id " +
                where + ";");
    }

    /**
     * Upserts Bulletin depending on the id.
     * This means it will insert the values of the Bulletin if they don't exist,
     * otherwise it will update them to the values of the Bulletin.
     * Will not close connection.
     * @param bulletin The Bulletin that will have influence on the database table.
     * @return True if any rows have been affected.
     */
    public boolean upsert(Bulletin bulletin) {
        boolean idExists = bulletin.get_primaryId() > 0;
        return edit("INSERT INTO bulletins(" +
                    (idExists ? "id," : "") +
                    "author_id," +
                    "content," +
                    "is_sent," +
                    "is_edited," +
                    "is_public," +
                    "receiver_id," +
                    "`timestamp`" +
                ") " +
                "VALUES(" +
                    (idExists ? bulletin.get_primaryId()+"," : "") +
                    bulletin.get_author().get_primaryId() + "," +
                    varCharColumn(bulletin.get_content()) + "," +
                    bulletin.is_sent() + "," +
                    platoColumn(bulletin.get_edited()) + "," +
                    bulletin.is_public() + "," +
                    bulletin.get_receiver().get_primaryId() + "," +
                    "NOW()) " +
                "ON DUPLICATE KEY UPDATE " +
                    "content = " + varCharColumn(bulletin.get_content()) + "," +
                    "is_sent = " + bulletin.is_sent() + "," +
                    "is_edited = " + platoColumn(bulletin.get_edited()) + "," +
                    "is_public = " + bulletin.is_public() +
                ";");
    }

    /**
     * Upserts Mail, might generate an id if there isn't any in the Mail.
     * It will insert the values of the Mail if they don't exist,
     * otherwise it will update them to the values of the Mail.
     * Will not close connection.
     * @param mail The Mail that will have influence on the database table.
     * @return A ResultSet of the created values with, if any, the generated keys. If there's an SQLException, it returns null.
     */
    public ResultSet upsert(Mail mail) {
        try {
            return create("INSERT INTO mails(" +
                    (mail.get_primaryId() > 0 ? "id," : "") +
                    "author_id," +
                    "content," +
                    "is_sent," +
                    "is_edited," +
                    "is_public," +
                    "chat_room_id," +
                    "`timestamp`) " +
                    "VALUES (" +
                    (mail.get_primaryId() > 0 ? mail.get_primaryId() + "," : "") +
                    mail.get_author().get_primaryId() + "," +
                    varCharColumn(mail.get_content()) + "," +
                    mail.is_sent() + "," +
                    platoColumn(mail.get_edited()) + "," +
                    mail.is_public() + "," +
                    mail.get_chatRoom().get_primaryId() + "," +
                    (mail.get_primaryId() > 0 ? varCharColumn(mail.get_timestamp().toString()) : "NOW()") +
                    ") " +
                    "ON DUPLICATE KEY UPDATE " +
                    "content = " + varCharColumn(mail.get_content()) + "," +
                    "is_sent = " + mail.is_sent() + "," +
                    "is_edited = " + platoColumn(mail.get_edited()) + "," +
                    "is_public = " + mail.is_public() +
                    ";").getGeneratedKeys();
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't get generated keys of Mail...",e);
        }

        return null;
    }

    /**
     * Upserts ChatRoom, might generate an id if there isn't any in the ChatRoom.
     * It will insert the values of the ChatRoom if they don't exist,
     * otherwise it will update them to the values of the ChatRoom.
     * Will not close connection.
     * @param chatRoom The ChatRoom that will have influence on the database table.
     * @return A ResultSet of the created values with, if any, the generated keys. If there's an SQLException, it returns null.
     */
    public Long upsert(ChatRoom chatRoom) {
        boolean idExists = chatRoom.get_primaryId() > 0;
        try {
            ResultSet set = create("INSERT INTO chat_rooms(" +
                    (idExists ? "id," : "") +
                    "is_local," +
                    "title," +
                    "responsible_id," +
                    "`timestamp`" +
                    ") " +
                    "VALUES(" +
                    (idExists ? chatRoom.get_primaryId()+"," : "") +
                    chatRoom.is_local() + "," +
                    varCharColumn(chatRoom.get_title()) + "," +
                    chatRoom.get_responsible().get_primaryId() + "," +
                    (idExists ? timestampColumn(chatRoom.get_timestamp()) : "NOW()") +
                    ") " +
                    "ON DUPLICATE KEY UPDATE " +
                    "title = " + varCharColumn(chatRoom.get_title()) +
                    ";").getGeneratedKeys();

            if (set.isBeforeFirst())
                set.next();

            long id = idExists ? chatRoom.get_primaryId() : set.getLong(1);

            if (chatRoom.get_chatters().size() > 0)
                if (insertChattersOf(new ChatRoom(
                        id, chatRoom.is_local(),
                        chatRoom.get_title(),chatRoom.get_mails(),
                        chatRoom.get_chatters(),chatRoom.get_responsible(),
                        chatRoom.get_timestamp())
                )) return id;
            return id;
        } catch (SQLException e) {
            Printer.get_instance().print("Couldn't get generated keys of upserting ChatRoom...",e);
        }
        return null;
    }

    /**
     * Inserts the chatters of a ChatRoom.
     * If they already exist, it will just ignore the SQL statement.
     * Doesn't close the connection.
     * @param chatRoom The ChatRoom that contains the chatters to insert.
     * @return True if it is a success.
     */
    @SuppressWarnings("All")
    public boolean insertChattersOf(ChatRoom chatRoom) {
        String sql = new String();

        for (User chatter : chatRoom.get_chatters())
            sql += "INSERT IGNORE INTO chatters(" +
                    "chat_room_id," +
                    "user_id" +
                    ") " +
                    "VALUES (" +
                    chatRoom.get_primaryId() + "," +
                    chatter.get_primaryId() +
                    "); ";

        return edit(sql);
    }

    /**
     * Will delete ChatRooms, where the id matches.
     * Will also delete the Mails in the ChatRoom.
     * Closes connection.
     * @param id The id of the ChatRoom that will be deleted.
     * @return A Plato object with the boolean result of the act.
     */
    public Plato deleteChatRoom(long id) {
        return new Plato(delete(id, "chat_rooms", "id", true));
    }

    /**
     * Will delete Bulletins, where the id matches.
     * Closes connection.
     * @param id The id of the Bulletin that will be deleted.
     * @return A Plato object with the boolean result of the act.
     */
    public Plato deleteBulletin(long id) {
        return new Plato(delete(id, "bulletins","id",true));
    }
}
