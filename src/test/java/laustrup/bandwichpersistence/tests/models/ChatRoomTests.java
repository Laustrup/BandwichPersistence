package laustrup.bandwichpersistence.tests.models;

import laustrup.bandwichpersistence.tests.Tester;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.utilities.collections.Liszt;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ChatRoomTests extends Tester<Liszt<User>, Object> {

    @ParameterizedTest
    @CsvSource(value = {"null","empty"})
    void canDefineTitle(String title) {
        test(t -> {
            Liszt<User> arranged = (Liszt<User>) arrange(e -> {
                Liszt<User> chatters = new Liszt<>(new User[]{_items.get_carlos(),_items.get_tir()});
                _expected = title.equals("null") ? "Carlos, Tir Treufeldt" : "Melanges";
                return chatters;
            });

            String actual = act(arranged, e ->
                new ChatRoom(_random.nextBoolean(), _expected.toString(),e,null).get_title()
            ).toString();

            asserting(_expected,actual);

            return true;
        });
    }
}
