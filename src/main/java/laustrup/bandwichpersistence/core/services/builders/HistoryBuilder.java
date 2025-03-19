package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.History;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.builders.BuilderService.printError;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class HistoryBuilder {

    private static final Logger _logger = Logger.getLogger(HistoryBuilder.class.getSimpleName());

    public static History.Story buildStory(ResultSet resultSet, History history) {
        AtomicReference<UUID> id = new AtomicReference<>();

        if (history != null && history.get_stories().stream().anyMatch(story -> story.get_id().equals(id.get()))) {
            Optional<History.Story> storyFound = history.get_stories().stream()
                    .filter(story -> story.get_id().equals(id.get()))
                    .findFirst();

            storyFound.ifPresent(story ->
                    story.get_details().add(getString(history.get_storyTable().get_table().toLowerCase() + ".content"))
            );

            if (storyFound.isPresent()) {
                return storyFound.get();
            }
        }

        try {
            AtomicReference<String> title = new AtomicReference<>();
            AtomicReference<String> detail = new AtomicReference<>();
            AtomicReference<Instant> timestamp = new AtomicReference<>();

            JDBCService.build(
                    resultSet,
                    () -> {
                        id.set(getUUID(Model.ModelDTO.Fields.id));
                        title.set(getString(Model.ModelDTO.Fields.title));
                        detail.set(getString(history.get_storyTable().get_table().toLowerCase() + ".content"));
                        timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                    },
                    id.get()
            );

            return new History.Story(
                    id.get(),
                    title.get(),
                    new Seszt<>(detail.get()),
                    timestamp.get()
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
}
