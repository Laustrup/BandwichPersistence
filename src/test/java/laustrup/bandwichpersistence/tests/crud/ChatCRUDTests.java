package laustrup.bandwichpersistence.tests.crud;

import laustrup.bandwichpersistence.tests.PersistenceTester;
import laustrup.models.chats.ChatRoom;
import laustrup.models.chats.messages.Bulletin;
import laustrup.models.chats.messages.Mail;
import laustrup.models.users.User;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services.ChatPersistenceService;
import laustrup.utilities.collections.lists.Liszt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChatCRUDTests extends PersistenceTester<Object> {

    private String _prevTitle, _postTitle, _prevContent, _postContent;

    private User _chatter, _responsible;
    private ChatRoom _expectedChatRoom, _actualChatRoom;

    private Mail _expectedMail, _actualMail;


    @BeforeEach
    void beforeEach() {
        _prevTitle = new String();
        _postTitle = new String();
        _prevContent = new String();
        _postContent = new String();

        _chatter = null;
        _responsible = null;

        _expectedChatRoom = null;
        _actualChatRoom = null;

        _expectedMail = null;
        _actualMail = null;
    }

    @Test // TODO ChatRoom no longer has responsible
    void canCRUDChatRoomAndMail() {
        test(() -> {
            arrange(() -> {
                _postTitle = "New chat room";
                _prevContent = "This is test content";
                _postContent = "Changed content";

                _chatter = get_artsy();
                _responsible = get_carlos();
                _expectedChatRoom = new ChatRoom(false, null,
                        new Liszt<>(new User[]{_responsible, _chatter}), _responsible);

                return null;
            });

            _actualChatRoom = (ChatRoom) act(() -> canInsert(_expectedChatRoom));

            arrange(() -> {
                _expectedChatRoom = new ChatRoom(_actualChatRoom.get_primaryId(), _expectedChatRoom.is_local(),
                        _expectedChatRoom.get_title(), _expectedChatRoom.get_mails(), _expectedChatRoom.get_chatters(),
                        _expectedChatRoom.get_responsible(),_actualChatRoom.get_timestamp());
                _prevTitle = _expectedChatRoom.get_title();

                return _expectedChatRoom;
            });

            assertChatRooms(new Liszt<>(new ChatRoom[]{_expectedChatRoom}), new Liszt<>(new ChatRoom[]{_actualChatRoom}));
            _expectedMail = (Mail) arrange(() -> new Mail(_expectedChatRoom, _chatter));
            _actualChatRoom = (ChatRoom) act(() -> canInsert(_expectedMail));

            arrange(() -> {
                _actualMail = _actualChatRoom.get_mails().getLast();
                _expectedChatRoom = new ChatRoom(_actualChatRoom.get_primaryId(), _expectedChatRoom.is_local(),
                        _expectedChatRoom.get_title(), _actualChatRoom.get_mails(), _expectedChatRoom.get_chatters(),
                        _expectedChatRoom.get_responsible(), _actualChatRoom.get_timestamp());
                _expectedMail = new Mail(_actualMail.get_primaryId(), _expectedMail.get_chatRoom(),
                        _expectedMail.get_author(), _expectedMail.get_content(), _expectedMail.is_sent(),
                        _expectedMail.get_edited(), _expectedMail.is_public(), _actualMail.get_timestamp());

                return null;
            });

            assertMails(new Liszt<>(new Mail[]{_expectedMail}), new Liszt<>(new Mail[]{_actualMail}));

            arrange(() -> {
                _expectedChatRoom.set_title(_postTitle);
                return null;
            });

            _actualChatRoom = canUpdate(_expectedChatRoom);
            _expectedChatRoom = new ChatRoom(_expectedChatRoom.get_primaryId(), _expectedChatRoom.is_local(),
                    _expectedChatRoom.get_title(), _actualChatRoom.get_mails(), _expectedChatRoom.get_chatters(),
                    _expectedChatRoom.get_responsible(), _expectedChatRoom.get_timestamp());

            assertChatRooms(new Liszt<>(new ChatRoom[]{_expectedChatRoom}), new Liszt<>(new ChatRoom[]{_actualChatRoom}));

            arrange(() -> {
                _expectedChatRoom.set_title(_prevTitle);
                return null;
            });
            _actualChatRoom = canUpdate(_expectedChatRoom);

            arrange(() -> {
                _expectedMail.set_content(_postContent);
                return null;
            });

            _actualChatRoom = canUpdate(_expectedMail);
            assertMails(new Liszt<>(new Mail[]{_expectedMail}), new Liszt<>(new Mail[]{_actualChatRoom.get_mails().getLast()}));

            arrange(() -> {
                _expectedMail.set_content(_prevContent);
                return null;
            });

            _actualChatRoom = canUpdate(_expectedMail);
            asserting(canDelete(_expectedChatRoom));
        });
    }

    /**
     * Acts the upsert of ChatRoom for the insert purpose.
     * @param expected The ChatRoom that will be used for the upsert method.
     * @return The actual ChatRoom of the database.
     */
    private ChatRoom canInsert(ChatRoom expected) {
        return (ChatRoom) act(expected, e -> ChatPersistenceService.get_instance().upsert((ChatRoom) e),
                "inserting " + expected.get_title());
    }

    /**
     * Acts the upsert of Mail for the insert purpose.
     * @param expected The Mail that will be used for the upsert method.
     * @return The ChatRoom that contains the actual Mail of insert.
     */
    private ChatRoom canInsert(Mail expected) {
        return (ChatRoom) act(expected, e -> ChatPersistenceService.get_instance().upsert((Mail) e),
                "inserting " + expected.get_title());
    }

    /**
     * Acts the upsert of ChatRoom for the Update purpose.
     * @param expected The ChatRoom that will be used for the upsert method.
     * @return The actual ChatRoom of the database.
     */
    private ChatRoom canUpdate(ChatRoom expected) {
        return (ChatRoom) act(expected, e -> ChatPersistenceService.get_instance().upsert((ChatRoom) e),
                "updating " + expected.get_title());
    }

    /**
     * Acts the upsert of Mail for the Update purpose.
     * @param expected The Mail that will be used for the upsert method.
     * @return The ChatRoom that contains the actual Mail of insert.
     */
    private ChatRoom canUpdate(Mail expected) {
        return (ChatRoom) act(expected, e -> ChatPersistenceService.get_instance().upsert((Mail) e),
                "updating " + expected.get_title());
    }

    /**
     * Acts the delete of ChatRoom for the delete purpose.
     * @param chatRoom The ChatRoom that will be used for the delete method.
     * @return The result of the act, where the Plato argument is as a boolean truth.
     */
    private boolean canDelete(ChatRoom chatRoom) {
        return (boolean) act(chatRoom, e ->
                ChatPersistenceService.get_instance().deleteChatRoom(((ChatRoom) e).get_primaryId()).get_truth(),
                "Deleting " + chatRoom.get_title());
    }

    @Test
    void canCrudBulletin() {
        test(() -> {
            Bulletin expected = (Bulletin) arrange(() -> _items.generateBulletins(Assembly.get_instance().getEvent(1))[0]);

            User user = canInsert(expected);
            Bulletin actual = user.get_bulletins().getLast();

            assertBulletins(new Liszt<>(new Bulletin[]{expected}), new Liszt<>(new Bulletin[]{actual}));

            expected = (Bulletin) arrange(expected, e -> {
                ((Bulletin) e).set_content("This is new content");
                return e;
            });

            actual = canUpdate(expected).get_bulletins().getLast();

            assertBulletins(new Liszt<>(new Bulletin[]{expected}), new Liszt<>(new Bulletin[]{actual}));

            asserting(canDelete(expected));
        });

    }

    /**
     * Acts the upsert of Bulletin for the insert purpose.
     * @param expected The Bulletin that will be used for the upsert method.
     * @return The User that will receive the Bulletin.
     */
    private User canInsert(Bulletin expected) {
        return (User) act(expected, e -> ChatPersistenceService.get_instance().upsert((Bulletin) e));
    }

    /**
     * Acts the upsert of Bulletin for the update purpose.
     * @param bulletin The Bulletin that will be used for the upsert method.
     * @return The User that will receive the Bulletin.
     */
    private User canUpdate(Bulletin bulletin) {
        return (User) act(bulletin, e -> ChatPersistenceService.get_instance().upsert((Bulletin) e));
    }

    /**
     * Acts delete of Bulletin for the delete purpose.
     * @param bulletin The Bulletin that will be used for the delete method.
     * @return The result of the act, where the Plato argument is as a boolean truth.
     */
    private boolean canDelete(Bulletin bulletin) {
        return (boolean) act(bulletin, e ->
                ChatPersistenceService.get_instance().deleteBulletin(((Bulletin) e).get_primaryId()).get_truth());
    }
}
