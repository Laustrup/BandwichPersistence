package laustrup.bandwichpersistence.models.chats;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor @ToString
public class ChatRoom extends Model {

    @Getter
    private List<Mail> _mails;
    @Getter
    private List<User> _participants;

    // Constructor for from database
    public ChatRoom(long id, String title, List<Mail> mail, List<User> participants, LocalDateTime timestamp) {
        super(id, title, timestamp);
        _mails = mail;
        _participants = participants;
    }

    // Constructor add to database
    public ChatRoom(String title, List<User> participants) {
        super(title);
        _mails = new LinkedList<>();
        _participants = participants;
    }

    // Add methods
    public List<Mail> add(Mail mail) {
        _mails.add(mail);
        return _mails;
    }
    public List<User> add(User participant) {
        _participants.add(participant);
        return _participants;
    }

    // Remove methods
    public List<Mail> remove(Mail mail) {
        for (int i = 0; i < _mails.size(); i++) {
            if (_mails.get(i).get_id() == mail.get_id()) {
                _mails.remove(_mails.get(i));
                break;
            }
        }
        return _mails;
    }
    public List<User> remove(User participant) {
        for (int i = 0; i < _participants.size(); i++) {
            if (_participants.get(i).get_id() == participant.get_id()) {
                _participants.remove(_participants.get(i));
                break;
            }
        }
        return _participants;
    }

    // Edit methods
    public boolean edit(Mail mail) {
        for (int i = 0; i < _mails.size(); i++) {
            if (_mails.get(i).get_id() == mail.get_id()) {
                _mails.set(i, mail);
                return true;
            }
        }
        return false;
    }
}
