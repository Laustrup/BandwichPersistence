package laustrup.bandwichpersistence.crud;

import laustrup.bandwichpersistence.JTest;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.services.persistence_services.assembling_services.Assembly;
import laustrup.bandwichpersistence.services.persistence_services.entity_services.sub_entity_services.ChatPersistenceService;
import laustrup.bandwichpersistence.utilities.Liszt;

import org.junit.jupiter.api.Test;

public class ChatCRUDTests extends JTest {

    @Test
    void canCRUDChatRoomAndMail() {
        //ARRANGE
        String prevTitle,
                postTitle = "New chat room",
                prevContent = "This is test content",
                postContent = "Changed content";

        User chatter = _items.get_artsy(), responsible = _items.get_carlos();
        ChatRoom expectedChatRoom = new ChatRoom(false, null,
                new Liszt<>(new User[]{responsible,chatter}), responsible);

        //ACT
        ChatRoom actualChatRoom = canInsert(expectedChatRoom);

        //POST ARRANGE
        expectedChatRoom = new ChatRoom(actualChatRoom.get_primaryId(),expectedChatRoom.is_local(),
                expectedChatRoom.get_title(),expectedChatRoom.get_mails(),expectedChatRoom.get_chatters(),
                expectedChatRoom.get_responsible(),actualChatRoom.get_timestamp());
        prevTitle = expectedChatRoom.get_title();

        //ASSERT
        assertChatRooms(new Liszt<>(new ChatRoom[]{expectedChatRoom}), new Liszt<>(new ChatRoom[]{actualChatRoom}));

        //PRE ARRANGE
        Mail expectedMail = new Mail(expectedChatRoom,chatter);

        //ACT
        actualChatRoom = canInsert(expectedMail);

        //POST ARRANGE
        Mail actualMail = actualChatRoom.get_mails().getLast();
        expectedChatRoom = new ChatRoom(actualChatRoom.get_primaryId(),expectedChatRoom.is_local(),
                expectedChatRoom.get_title(),actualChatRoom.get_mails(),expectedChatRoom.get_chatters(),
                expectedChatRoom.get_responsible(),actualChatRoom.get_timestamp());
        expectedMail = new Mail(actualMail.get_primaryId(),expectedMail.get_chatRoom(),
                expectedMail.get_author(),expectedMail.get_content(),expectedMail.is_sent(),
                expectedMail.get_edited(),expectedMail.is_public(),actualMail.get_timestamp());

        //ASSERT
        assertMails(new Liszt<>(new Mail[]{expectedMail}), new Liszt<>(new Mail[]{actualMail}));

        //PRE ARRANGE
        expectedChatRoom.set_title(postTitle);

        //ACT
        actualChatRoom = canUpdate(expectedChatRoom);
        expectedChatRoom = new ChatRoom(expectedChatRoom.get_primaryId(), expectedChatRoom.is_local(),
                expectedChatRoom.get_title(),actualChatRoom.get_mails(),expectedChatRoom.get_chatters(),
                expectedChatRoom.get_responsible(),expectedChatRoom.get_timestamp());

        //ASSERT
        assertChatRooms(new Liszt<>(new ChatRoom[]{expectedChatRoom}), new Liszt<>(new ChatRoom[]{actualChatRoom}));

        //RE ARRANGE
        expectedChatRoom.set_title(prevTitle);
        actualChatRoom = canUpdate(expectedChatRoom);

        //PRE ARRANGE
        expectedMail.set_content(postContent);

        //ACT
        actualChatRoom = canUpdate(expectedMail);

        //ASSERT
        assertMails(new Liszt<>(new Mail[]{expectedMail}), new Liszt<>(new Mail[]{actualChatRoom.get_mails().getLast()}));

        //RE ARRANGE
        expectedMail.set_content(prevContent);
        actualChatRoom = canUpdate(expectedMail);

        //ACT ASSERT
        asserting(canDelete(expectedChatRoom));
    }

    /**
     * Acts the upsert of ChatRoom for the insert purpose.
     * @param expected The ChatRoom that will be used for the upsert method.
     * @return The actual ChatRoom of the database.
     */
    private ChatRoom canInsert(ChatRoom expected) {
        begin();
        ChatRoom actual = ChatPersistenceService.get_instance().upsert(expected);
        calculatePerformance("inserting chat room");
        return actual;
    }

    /**
     * Acts the upsert of Mail for the insert purpose.
     * @param expected The Mail that will be used for the upsert method.
     * @return The ChatRoom that contains the actual Mail of insert.
     */
    private ChatRoom canInsert(Mail expected) {
        begin();
        ChatRoom chatRoom = ChatPersistenceService.get_instance().upsert(expected);
        calculatePerformance("insert mail");
        return chatRoom;
    }

    /**
     * Acts the upsert of ChatRoom for the Update purpose.
     * @param expected The ChatRoom that will be used for the upsert method.
     * @return The actual ChatRoom of the database.
     */
    private ChatRoom canUpdate(ChatRoom expected) {
        begin();
        ChatRoom actual = ChatPersistenceService.get_instance().upsert(expected);
        calculatePerformance("update chat room");
        return actual;
    }

    /**
     * Acts the upsert of Mail for the Update purpose.
     * @param expected The Mail that will be used for the upsert method.
     * @return The ChatRoom that contains the actual Mail of insert.
     */
    private ChatRoom canUpdate(Mail expected) {
        begin();
        ChatRoom actual = ChatPersistenceService.get_instance().upsert(expected);
        calculatePerformance("upsert update mail");
        return actual;
    }

    /**
     * Acts the delete of ChatRoom for the delete purpose.
     * @param chatRoom The ChatRoom that will be used for the delete method.
     * @return The result of the act, where the Plato argument is as a boolean truth.
     */
    private boolean canDelete(ChatRoom chatRoom) {
        begin();
        boolean result = ChatPersistenceService.get_instance().deleteChatRoom(chatRoom.get_primaryId()).get_truth();
        calculatePerformance("Deleting " + chatRoom.get_title());
        return result;
    }

    @Test
    void canUpsertBulletin() {
        //ARRANGE
        Bulletin expected = _items.generateBulletins(Assembly.get_instance().getEvent(1))[0];

        //ACT
        begin();
        User user = ChatPersistenceService.get_instance().upsert(expected);
        calculatePerformance("upsert insert bulletin");

        //ASSERT
        assertBulletins(new Liszt<>(new Bulletin[]{expected}), new Liszt<>(new Bulletin[]{user.get_bulletins().getLast()}));

        //ARRANGE
        expected.set_content("This is new content");

        //ACT
        begin();
        user = ChatPersistenceService.get_instance().upsert(expected);
        calculatePerformance("upsert update bulletin");

        //ASSERT
        assertBulletins(new Liszt<>(new Bulletin[]{expected}), new Liszt<>(new Bulletin[]{user.get_bulletins().getLast()}));
    }
}
