package laustrup.bandwichpersistence.models.chats;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.utilities.Liszt;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @ToString
public class ChatRoom extends Model {

    @Getter
    private Liszt<Mail> _mails;
    @Getter
    private Liszt<User> _participants;

    public ChatRoom(long id, String title, Liszt<Mail> mail, Liszt<User> participants, LocalDateTime timestamp) {
        super(id, title, timestamp);
        _mails = mail;
        _participants = participants;
    }

    public ChatRoom(String title, Liszt<User> participants) {
        super(title);
        _mails = new Liszt<>();
        _participants = participants;
    }

    public List<Mail> add(Mail mail) {
        _mails.add(mail);
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
        for (int i = 0; i < _mails.size(); i++) {
            if (_mails.get(i).get_id() == mail.get_id()) {
                _mails.set(i, mail);
                return true;
            }
        }
        return false;
    }
}
