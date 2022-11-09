package laustrup.bandwichpersistence.models.chats;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.utilities.Liszt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @ToString
public class ChatRoom extends Model {

    @Getter
    private Liszt<Mail> _mails;
    @Getter
    private User _responsible;
    @Getter
    private Liszt<User> _participants;

    /**
     * The amount of time it takes, before the responsible have answered the chatroom,
     * measured from the first message.
     * Is calculated in minutes.
     */
    @Getter
    private Long _answeringTime;
    @Getter
    private boolean _answered;

    public ChatRoom(long id, String title, Liszt<Mail> mails, User responsible, Liszt<User> participants, LocalDateTime timestamp) {
        super(id, title, timestamp);
        _mails = mails;
        _responsible = responsible;
        _participants = participants;

        isTheChatRoomAnswered();
    }

    public ChatRoom(String title, User responsible, Liszt<User> participants) {
        super(title);
        _mails = new Liszt<>();
        _responsible = responsible;
        _participants = participants;
        isTheChatRoomAnswered();
    }

    public List<Mail> add(Mail mail) {
        _mails.add(mail);
        if (!_answered)
            isTheChatRoomAnswered();

        return _mails;
    }
    public List<User> add(User participant) {
        _participants.add(participant);
        return _participants;
    }

    public List<Mail> remove(Mail mail) {
        for (int i = 1; i <= _mails.size(); i++) {
            if (_mails.get(i).get_id() == mail.get_id()) {
                _mails.remove(_mails.get(i));
                break;
            }
        }
        return _mails;
    }
    public List<User> remove(User participant) {
        for (int i = 1; i <= _participants.size(); i++) {
            if (_participants.get(i).get_id() == participant.get_id()) {
                _participants.remove(_participants.get(i));
                break;
            }
        }
        return _participants;
    }

    public boolean edit(Mail mail) {
        for (int i = 1; i <= _mails.size(); i++) {
            if (_mails.get(i).get_id() == mail.get_id()) {
                _mails.set(i, mail);
                return true;
            }
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
    private boolean isTheChatRoomAnswered() {
        for (Mail mail : _mails) {
            if (mail.get_author().get_id() != _responsible.get_id()) {
                _answered = true;
                _answeringTime = calculateAnsweringTime();
                return true;
            }
        }

        _answered = false;
        return false;
    }

    /**
     * Calculates the time it took the responsible to answer.
     * Should be used only in local method isTheChatRoomAnswered().
     * @return The amount of hours it took the responsible to answer,
     * if ChatRoom is not answered, it will return null.
     */
    private Long calculateAnsweringTime() {
        if (_answered) {

            _answeringTime = Duration.between(_mails.get(1).get_timestamp(),
                    _mails.get(_responsible.toString()).get_timestamp()).toMinutes();
            return _answeringTime;
        }
        return null;
    }
}
