package laustrup.bandwichpersistence.models.chats;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.dtos.chats.ChatRoomDTO;
import laustrup.bandwichpersistence.models.dtos.chats.messages.MailDTO;
import laustrup.bandwichpersistence.models.dtos.users.UserDTO;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Artist;
import laustrup.bandwichpersistence.models.users.sub_users.bands.Band;
import laustrup.bandwichpersistence.services.DTOService;
import laustrup.bandwichpersistence.utilities.collections.Liszt;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This is used for multiple Users to communicate with each other through Mails.
 */
public class ChatRoom extends Model {

    /**
     * Will determine if this ChatRoom is local,
     * meaning there will not be any responsible,
     * but will only contain band members as chatters.
     */
    @Getter
    private boolean _local;

    /**
     * All the Mails that has been sent will be stored here.
     */
    @Getter
    private Liszt<Mail> _mails;

    /**
     * The Users, except the responsible, that can write with each other.
     */
    @Getter
    private Liszt<User> _chatters;

    /**
     * This responsible are being calculated for answeringTime.
     */
    @Getter
    private User _responsible;

    /**
     * The amount of time it takes, before the responsible have answered the chatroom,
     * measured from the first message.
     * Is calculated in minutes.
     */
    @Getter
    private Long _answeringTime;

    /**
     * Is true if the responsible has answered with a message.
     */
    @Getter
    private boolean _answered;

    /**
     * Converts a Data Transport Object into this object.
     * @param chatRoom The Data Transport Object that will be converted.
     */
    public ChatRoom(ChatRoomDTO chatRoom) {
        _mails = new Liszt<>();
        convert(chatRoom.getMails());
        _chatters = new Liszt<>();
        convert(chatRoom.getChatters());
        _responsible = DTOService.get_instance().convertFromDTO(chatRoom.getResponsible());

        isTheChatRoomAnswered();
    }

    /**
     * Converts a Data Transport Object into Mails.
     * @param mails The Data Transport Object that will be converted.
     * @return The converted Mails.
     */
    private Liszt<Mail> convert(MailDTO[] mails) {
        for (MailDTO mail : mails)
            _mails.add(new Mail(mail));
        return _mails;
    }

    /**
     * Converts a Data Transport Object into Chatters.
     * @param chatters The Data Transport Object that will be converted.
     * @return The converted Chatters.
     */
    private Liszt<User> convert(UserDTO[] chatters) {
        for (UserDTO chatter : chatters)
            _chatters.add(DTOService.get_instance().convertFromDTO(chatter));
        return _chatters;
    }

    /**
     * Containing all attributes of this object.
     * Purpose is for assembling.
     * Will set assembling to true.
     * Checks if the ChatRoom has been answered.
     * @param id The primary id.
     * @param isLocal Is this a Band ChatRoom without a responsible or not
     * @param title The title of the ChatRoom, if it is null or empty, it will be the usernames of the chatters.
     * @param mails The Mails with relations to this ChatRoom.
     * @param chatters The chatters that are members of this ChatRoom.
     * @param responsible The intended receiver of this ChatRoom.
     * @param timestamp The time this ChatRoom was created.
     */
    public ChatRoom(long id, boolean isLocal, String title, Liszt<Mail> mails, Liszt<User> chatters,
                    User responsible, LocalDateTime timestamp) {
        super(id, title, timestamp);
        _chatters = chatters;
        _responsible = responsible;
        _title = determineChatRoomTitle(_title);
        _local = isLocal;
        _mails = mails;
        _assembling = true;

        setChatRoomOfMails();
        isTheChatRoomAnswered();
    }

    /**
     * A primitive constructor, with lesser values.
     * Purpose is to use for assembling.
     * Will set assembling to true.
     * @param id The primary id.
     * @param title The title of the ChatRoom, if it is null or empty, it will be the usernames of the chatters.
     * @param timestamp The time this ChatRoom was created.
     */
    public ChatRoom(long id, boolean isLocal, String title, LocalDateTime timestamp) {
        super(id, title, timestamp);
        _local = isLocal;
        _chatters = new Liszt<>();
        _title = determineChatRoomTitle(_title);

        _mails = new Liszt<>();
        _assembling = true;
    }

    /**
     * Will be used for creating a new ChatRoom, without an id.
     * Checks if the ChatRoom has been answered.
     * @param title The title of the ChatRoom, if it is null or empty, it will be the usernames of the chatters.
     * @param chatters The chatters that are members of this ChatRoom.
     * @param responsible The intended receiver of this ChatRoom.
     */
    public ChatRoom(boolean isLocal, String title, Liszt<User> chatters, User responsible) {
        super(title);
        _local = isLocal;
        _chatters = chatters;
        _responsible = responsible;
        _title = determineChatRoomTitle(_title);
        _mails = new Liszt<>();

        isTheChatRoomAnswered();
    }

    /**
     * Will make the title of this ChatRoom be of custom title or chatters' usernames.
     * @param title The custom title.
     * @return The determined title.
     */
    private String determineChatRoomTitle(String title) {
        if (title == null || title.isEmpty()) {
            StringBuilder usernames = new StringBuilder();

            for (int i = 1; i <= _chatters.size(); i++)
                usernames.append(_chatters.get(i).get_username()).append(i < _chatters.size() ? ", " : "");

            return usernames.toString();
        }
        else
            return title;
    }

    /**
     * Can only be used, if it is being assembled.
     * Will set each Mail's ChatRoom to this object.
     * @return All Mails.
     */
    private Liszt<Mail> setChatRoomOfMails(){
        if (_assembling) {
            for (int i = 1; i <= _mails.size(); i++)
                _mails.get(i).set_chatRoom(this);

            _assembling = false;
        }
        return _mails;
    }

    /**
     * Adds a Mail to the ChatRoom, if the author of the Mail is a chatter of the ChatRoom.
     * If the responsible haven't answered yet, it will check if it now is answered.
     * @param mail A Mail object, that is wished to be added.
     * @return All the Mails of this ChatRoom.
     */
    public List<Mail> add(Mail mail) {
        if (chatterExists(mail.get_author())) {
            if (_mails.add(mail)) if (mail.doSend()) edit(mail);
            if (!_answered) isTheChatRoomAnswered();
        }

        return _mails;
    }

    /**
     * It will add a chatter, if it isn't already added.
     * If the chatter is a Band, it will try to add all the members of the Band,
     * unless some already is a chatter.
     * @param chatter A user that is wished to be added as a chatter of the ChatRoom.
     * @return All the chatters of the ChatRoom.
     */
    public List<User> add(User chatter) {
        if (chatter.getClass() == Band.class) {
            for (Artist artist : ((Band) chatter).get_members()) {
                if (!chatterExists(artist))
                    _chatters.add(chatter);
            }
        }
        else if (!chatterExists(chatter)) _chatters.add(chatter);

        return _chatters;
    }

    /**
     * Checks if a chatter exists in the ChatRoom.
     * @param chatter A User, that should be checked, if it already exists in the ChatRoom.
     * @return True if the chatter exists in the ChatRoom.
     */
    public boolean chatterExists(User chatter) {
        for (User user : _chatters) {
            if (user.getClass() == chatter.getClass() && user.get_primaryId() == chatter.get_primaryId()) return true;
        }
        return false;
    }

    /**
     * Will remove a Mail from the ChatRoom.
     * @param mail The Mail object that is wished to be removed.
     * @return All the Mails of this ChatRoom.
     */
    public List<Mail> remove(Mail mail) {
        for (int i = 1; i <= _mails.size(); i++) {
            if (_mails.get(i).get_primaryId() == mail.get_primaryId()) {
                _mails.remove(_mails.get(i));
                break;
            }
        }
        return _mails;
    }

    /**
     * Will remove a chatter from the ChatRoom.
     * @param chatter A user object that is wished to be removed.
     * @return All the chatters of this ChatRoom.
     */
    public List<User> remove(User chatter) {
        for (int i = 1; i <= _chatters.size(); i++) {
            if (_chatters.get(i).get_primaryId() == chatter.get_primaryId()) {
                _chatters.remove(_chatters.get(i));
                break;
            }
        }
        return _chatters;
    }

    /**
     * Edits a Mail of the ChatRoom.
     * @param mail The Mail that is an updated version of a previous Mail, which will be updated.
     * @return True if it will be edited correctly.
     */
    public boolean edit(Mail mail) {
        for (int i = 1; i <= _mails.size(); i++) {
            if (_mails.get(i).get_primaryId() == mail.get_primaryId())
                return mail == _mails.set(i, mail);
        }
        return false;
    }

    /**
     * Checks if the ChatRoom is answered by the responsible, by a foreach loop through _mails.
     * Needs to be used each time a message is added, if the ChatRoom isn't already answered by the responsible.
     * Also use in constructor of use from database.
     * In case return is true, it will also calculate answering time.
     * @return The boolean answer of whether the ChatRoom has been answered or not
     */
    private boolean isTheChatRoomAnswered() { return findResponsibleAnswer()!=null; }

    /**
     * Calculates the time it took the responsible to answer.
     * Should be used only in local method isTheChatRoomAnswered().
     * @return The amount of hours it took the responsible to answer,
     * if ChatRoom is not answered, it will return null.
     */
    private Long calculateAnsweringTime() {
        if (_answered) {
            _answeringTime = Duration.between(_mails.get(1).get_timestamp(),
                    findResponsibleAnswer().get_timestamp()).toMinutes();
            return _answeringTime;
        }
        return null;
    }

    /**
     * Searches through the Mails and checks if the responsible have answered,
     * @return If the responsible have answered, it will return that Mail, otherwise null.
     */
    private Mail findResponsibleAnswer() {
        for (Mail mail : _mails)
            if (mail.get_author().get_primaryId() == _responsible.get_primaryId())
                return mail;

        return null;
    }

    @Override
    public String toString() {
        return "ChatRoom(" +
                    "id:" + _primaryId +
                    ",title:" + _title +
                    ",timestamp:" + _timestamp +
                ")";
    }
}
