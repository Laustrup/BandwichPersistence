package laustrup.bandwichpersistence.services;

import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.utilities.Liszt;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This service is created for the purposes of managing time issues.
 * An important issue is the answering time of users, which it can handle.
 * Is meant as a singleton.
 */
public class TimeService {

    /**
     * Singleton instance of the Service.
     */
    private static TimeService _instance = null;

    /**
     * Checks first if instance is null, otherwise will create a new instance of the object.
     * Created as a lazyfetch.
     * @return The instance of the object, as meant as a singleton.
     */
    public static TimeService get_instance() {
        if (_instance == null) _instance = new TimeService();
        return _instance;
    }

    /**
     * Calculates the amount of minutes, that it has taken for the first message to be answered.
     * @param chatRooms The specific ChatRooms, that is wished to be calculated.
     * @return The amount of minutes from the first answer of ChatRooms.
     * In case a ChatRoom is not answered, it will count from timestamp to now.
     */
    public Long getTotalAnswerTimes(Liszt<ChatRoom> chatRooms) {
        ArrayList<Long> answeringTimes = new ArrayList<>();

        for (ChatRoom room : chatRooms) {
            if (room.is_answered()) answeringTimes.add(room.get_answeringTime());
            else answeringTimes.add(Duration.between(room.get_mails().get(1).get_timestamp(),
                    LocalDateTime.now()).toMinutes());
        }

        return calculateAnsweringTime(answeringTimes);
    }

    /**
     * Will create a random DateTime from between 2020 -> 2030.
     * @return A LocalDateTime that has been randomly generated.
     */
    public LocalDateTime generateRandom() {
        return LocalDateTime.from(LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(
                LocalDate.of(2020, 1, 1).toEpochDay(),
                LocalDate.of(2029, 12, 31).toEpochDay())));
    }

    /**
     * Sums up the values that are in the input.
     * @param answeringTimes An ArrayList of Longs, that is calculated as minutes for the responsibles of ChatRooms to answer.
     * @return The sums of the ArrayList in the input. If it's empty, it will return 0.
     */
    private Long calculateAnsweringTime(ArrayList<Long> answeringTimes) {
        Long total = 0L;
        for (Long answeringTime : answeringTimes) { total += answeringTime; }

        return total;
    }

}
