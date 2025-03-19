package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.builders.BuilderService.printError;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class AlbumBuilder {

    private static final Logger _logger = Logger.getLogger(AlbumBuilder.class.getSimpleName());

    public static Album build(ResultSet resultset) {
        AtomicReference<UUID> id = new AtomicReference<>();
        AtomicReference<String> title = new AtomicReference<>();
        Seszt<Album.Media> media = new Seszt<>();
        AtomicReference<Instant> timestamp = new AtomicReference<>();

        try {
            JDBCService.build(
                    resultset,
                    () -> {
                        id.set(get(
                                JDBCService::getUUID,
                                Model.ModelDTO.Fields.id
                        ));
                        title.set(get(
                                JDBCService::getString,
                                Band.DTO.Fields.name
                        ));
                        media.add(buildMedia(resultset));
                        timestamp.set(get(
                                column -> getTimestamp(column, Timestamp::toInstant),
                                Model.ModelDTO.Fields.timestamp
                        ));
                    },
                    primary -> !get(
                            JDBCService::getUUID,
                            Model.ModelDTO.Fields.id
                    ).equals(primary),
                    id.get()
            );
        } catch (SQLException exception) {
            printError(
                    AlbumBuilder.class,
                    id.get(),
                    exception,
                    _logger
            );
        }

        return new Album(
                id.get(),
                title.get(),
                media,
                timestamp.get()
        );
    }

    public static Album.Media buildMedia(ResultSet resultset) {
        AtomicReference<UUID> id = new AtomicReference<>();
        AtomicReference<Album.Media.Kind> kind = new AtomicReference<>();
        AtomicReference<String>
                title = new AtomicReference<>(),
                endpoint = new AtomicReference<>();
        AtomicReference<Instant> timestamp = new AtomicReference<>();

        try {
            JDBCService.build(
                    resultset,
                    () -> {
                        id.set(get(
                                JDBCService::getUUID,
                                Model.ModelDTO.Fields.id
                        ));
                        title.set(get(
                                JDBCService::getString,
                                Model.ModelDTO.Fields.title
                        ));
                        endpoint.set(get(
                                JDBCService::getString,
                                Album.Media.DTO.Fields.endpoint
                        ));
                        kind.set(Album.Media.Kind.valueOf(get(
                                JDBCService::getString,
                                Album.Media.DTO.Fields.kind
                        )));
                        timestamp.set(get(
                                column -> getTimestamp(column, Timestamp::toInstant),
                                Model.ModelDTO.Fields.timestamp
                        ));
                    },
                    primary -> !get(
                            JDBCService::getUUID,
                            Model.ModelDTO.Fields.id
                    ).equals(primary),
                    id.get()
            );
        } catch (SQLException exception) {
            printError(
                    AlbumBuilder.class,
                    id.get(),
                    exception,
                    _logger
            );
        }

        return new Album.Media(
                id.get(),
                title.get(),
                endpoint.get(),
                kind.get(),
                timestamp.get()
        );
    }
}
