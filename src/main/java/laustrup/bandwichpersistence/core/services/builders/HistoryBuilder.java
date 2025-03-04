package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.History;
import laustrup.bandwichpersistence.core.models.Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class HistoryBuilder {

//    public static History.Story buildStory(ResultSet resultSet) {
//        try {
//            if (get(
//                    column -> getUUID(resultSet, column),
//                    History.Story.DatabaseColumn.story_id.name()
//            ) == null)
//                return null;
//
//            return new History.Story(
//                    get(
//                            column -> getUUID(resultSet, column),
//                            Model.DatabaseColumn.id.name()
//                    ),
//                    get(
//                            column -> getString(resultSet, column),
//                            History.Story.DatabaseColumn.title.name()
//                    ),
//                    getCollection(
//                            resultSet,
//                            History.Story.DatabaseColumn.story_id.name(),
//                            set -> get(
//                                    column -> getString(resultSet, column),
//                                    History.Story.DatabaseColumn.content.name()
//                            )
//                    ),
//                    get(
//                            column -> getTimestamp(resultSet, column, Timestamp::toInstant)
//                    )
//            );
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static Stream<History.Story> getStoriesOfOwner(Map<UUID, History.Story> collection, UUID ownerId) {
        return collection.values().stream()
                .filter(story ->
                        story != null
                                &&
                        story.get_ownerId() != null
                                &&
                        story.get_ownerId().equals(ownerId)
                );
    }
}
