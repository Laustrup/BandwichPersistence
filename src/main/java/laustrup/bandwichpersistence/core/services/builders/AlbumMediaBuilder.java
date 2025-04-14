package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Album;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService.Field;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class AlbumMediaBuilder extends BuilderService<Album.Media> {

    private static final Logger _logger = Logger.getLogger(AlbumMediaBuilder.class.getName());

    private static AlbumMediaBuilder _instance;

    public static AlbumMediaBuilder get_instance() {
        if (_instance == null)
            _instance = new AlbumMediaBuilder();

        return _instance;
    }

    private AlbumMediaBuilder() {
        super(Album.Media.class, _logger);
    }

    @Override
    protected void completion(Album.Media reference, Album.Media object) {

    }

    @Override
    protected Function<Function<String, Field>, Album.Media> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID> id = new AtomicReference<>();
            AtomicReference<Album.Media.Kind> kind = new AtomicReference<>();
            AtomicReference<String>
                    title = new AtomicReference<>(),
                    endpoint = new AtomicReference<>();
            AtomicReference<Instant> timestamp = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Model.ModelDTO.Fields.id));
                        set(title, table.apply(Model.ModelDTO.Fields.title));
                        set(endpoint, table.apply(Album.Media.DTO.Fields.endpoint));
                        set(kind, table.apply(Album.Media.DTO.Fields.kind));
                        timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                    },
                    id
            );

            return new Album.Media(
                    id.get(),
                    title.get(),
                    endpoint.get(),
                    kind.get(),
                    timestamp.get()
            );
        };
    }
}
