package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.History;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static laustrup.bandwichpersistence.core.services.builders.BuilderService.printError;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class HistoryBuilder {

    private static final Logger _logger = Logger.getLogger(HistoryBuilder.class.getSimpleName());

    public static History.Story buildStory(ResultSet resultSet, History history) {
        AtomicReference<UUID> id = new AtomicReference<>();

        try {
            AtomicReference<String> title = new AtomicReference<>();

            JDBCService.build(
                    resultSet,
                    () -> {
                        id.set(getUUID(Model.ModelDTO.Fields.id));
                        title.set(getString(Model.ModelDTO.Fields.title));
                    },
                    id.get()
            );

            return new History.Story(
                    id.get(),
                    title.get(),
                    getCollection(
                            resultSet,
                            History.Story.DatabaseColumn.story_id.name(),
                            set -> getString(History.Story.DatabaseColumn.content.name())
                    ),
                    get(
                            column -> getTimestamp(column, Timestamp::toInstant)
                    )
            );
        } catch (SQLException exception) {
            printError(
                    AlbumBuilder.class,
                    id.get(),
                    exception,
                    _logger
            );
            return null;
        }
    }

    public static Stream<History.Story> getStoriesOfOwner(Map<UUID, History.Story> collection, UUID ownerId) {
        return collection.values().stream()
                .filter(story ->
                        story != null &&
                        story.get_ownerId() != null &&
                        story.get_ownerId().equals(ownerId)
                );
    }
}
