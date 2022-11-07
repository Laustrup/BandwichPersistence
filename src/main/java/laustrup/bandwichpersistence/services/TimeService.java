package laustrup.bandwichpersistence.services;

import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.utilities.Liszt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;

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
     * Calculates the amount of hours, that it has taken for the first message to be answered.
     * @param chatRooms The specific ChatRooms, that is wished to be calculated.
     * @return The amount of hours from the first answer of ChatRooms.
     * In case a ChatRoom is not answered, it will count from timestamp to now.
     */
    public double getAnswerTimes(Liszt<ChatRoom> chatRooms) {
        ArrayList<Double> answeringTimes = new ArrayList<>();

        for (ChatRoom room : chatRooms) {
            if (room.is_answered()) answeringTimes.add(room.get_answeringTime());
            else answeringTimes.add(calculateDateFromNowInHours(room.get_mails().get(1).get_timestamp()));
        }

        return calculateAnsweringTime(answeringTimes);
    }

    /**
     * Sums up the values that are in the input.
     * @param answeringTimes An ArrayList of Doubles, that is calculated as hours for the responsibles of ChatRooms to answer.
     * @return The sums of the ArrayList in the input. If it's empty, it will return 0.
     */
    private double calculateAnsweringTime(ArrayList<Double> answeringTimes) {
        double total = 0;
        for (double answeringTime : answeringTimes) { total += answeringTime; }

        return total;
    }

    /**
     * Will measure how long it has been, since the given input from now.
     * @param dateTime An specific input wished to be measured from now.
     * @return The interval in hours from the input and now.
     */
    public double calculateDateFromNowInHours(LocalDateTime dateTime) {
        return convertIntoHourFormat();
    }

    /**
     * Will first check if last is after first, afterwards translate these two input dates to hours.
     * @param first The lowest date value to compare.
     * @param last the highest date value to compare.
     * @return The amount of hours of the difference between the two inputs in double type.
     * @throws InputMismatchException Will be thrown, if fist is later than last.
     */
    public double calculateDateDifferences(LocalDateTime first, LocalDateTime last) throws InputMismatchException {
        if (last.isAfter(first)) {
            double difference = 0;


            return convertIntoHourFormat(difference);
        }
        throw new InputMismatchException();
    }

    //TODO Tests this convertion
    /**
     * A calculation expression, that will set the value into hours, meant as 60 minutes in decimals.
     * @param value Some kind of double, that will be converted.
     * @return The value, where decimals is in minutes, with 60 as its highest decimal value.
     */
    public double convertIntoHourFormat(double value) { return Math.floor(value) + ((value % 1)/0.6); }
}
