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
                                column -> getUUID(resultset, column),
                                Model.ModelDTO.Fields.id
                        ));
                        title.set(get(
                                column -> getString(resultset, column),
                                Band.DTO.Fields.name
                        ));
                        media.add(buildMedia(resultset));
                        timestamp.set(get(
                                column -> getTimestamp(resultset, column, Timestamp::toInstant),
                                Model.ModelDTO.Fields.timestamp
                        ));
                    },
                    primary -> !get(
                            column -> getUUID(resultset, column),
                            Model.ModelDTO.Fields.id
                    ).equals(primary),
                    id.get()
            );
        } catch (SQLException e) {
            _logger.log(
                    Level.WARNING,
                    String.format(
                            "Couldn't build Album with id %s, message is:\n\n%s",
                            id.get(),
                            e.getMessage()
                    ),
                    e
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
                                column -> getUUID(resultset, column),
                                Model.ModelDTO.Fields.id
                        ));
                        title.set(get(
                                column -> getString(resultset, column),
                                Model.ModelDTO.Fields.title
                        ));
                        endpoint.set(get(
                                column -> getString(resultset, column),
                                Album.Media.DTO.Fields.endpoint
                        ));
                        kind.set(Album.Media.Kind.valueOf(get(
                                column -> getString(resultset, column),
                                Album.Media.DTO.Fields.kind
                        )));
                        timestamp.set(get(
                                column -> getTimestamp(resultset, column, Timestamp::toInstant),
                                Model.ModelDTO.Fields.timestamp
                        ));
                    },
                    primary -> !get(
                            column -> getUUID(resultset, column),
                            Model.ModelDTO.Fields.id
                    ).equals(primary),
                    id.get()
            );
        } catch (SQLException e) {
            _logger.log(
                    Level.WARNING,
                    String.format(
                            "Couldn't build Album with id %s, message is:\n\n%s",
                            id.get(),
                            e.getMessage()
                    ),
                    e
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
