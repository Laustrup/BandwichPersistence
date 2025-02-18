package laustrup.bandwichpersistence.core.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This service is created for the purposes of managing time issues.
 * An important issue is the answering time of users, which it can handle.
 * Is meant as a singleton.
 */
public class TimeService extends Service {

    /**
     * Will create a random DateTime from between 2020 -> 2030.
     * @return A LocalDateTime that has been randomly generated.
     */
    public static LocalDateTime generateRandom() {
        return LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(
                LocalDate.of(2020, 1, 1).toEpochDay(),
                LocalDate.of(2029, 12, 31).toEpochDay()))
                .atStartOfDay();
    }

    /**
     * Sums up the values that are in the input.
     * @param answeringTimes An ArrayList of Longs, that is calculated as minutes for the responsibles of ChatRooms to answer.
     * @return The sums of the ArrayList in the input. If it's empty, it will return 0.
     */
    private static Long calculateAnsweringTime(ArrayList<Long> answeringTimes) {
        Long total = 0L;
        for (Long answeringTime : answeringTimes) { total += answeringTime; }

        return total;
    }

    /**
     * Will convert a DateTime row from the database into a java LocalDateTime.
     * @param set The ResultSet from the database.
     * @param row The row column title of the DataTime.
     * @return If the row value is null, it will return null, otherwise the converted java LocalDateTime.
     * @throws SQLException The exception from the ResultSet, if it occurs.
     */
    public static LocalDateTime convertFromDatabase(ResultSet set, String row) throws SQLException {
        return set.getTimestamp(row) == null ? null : set.getTimestamp(row).toLocalDateTime();
    }
}
