package laustrup.bandwichpersistence.models;

import laustrup.bandwichpersistence.Tester;
import laustrup.bandwichpersistence.models.chats.ChatRoom;

import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.utilities.Liszt;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class ChatRoomTests extends Tester {

    @ParameterizedTest
    @CsvSource(value = {"null","empty"})
    void canDefineTitle(String title) {
        //ARRANGE
        Liszt<User> chatters = new Liszt<>(new User[]{_items.get_carlos(),_items.get_tir()});
        String expected = title.equals("null") ? "Carlos, Tir Treufeldt" : "Melanges";

        //ACT
        begin();
        String actual = new ChatRoom(_random.nextBoolean(),expected,chatters,null).get_title();
        calculatePerformance();

        //ASSERT
        assertEquals(expected,actual);
    }
}
