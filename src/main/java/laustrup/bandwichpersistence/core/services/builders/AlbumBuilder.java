package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.Album;
import laustrup.bandwichpersistence.core.models.Model;
import laustrup.bandwichpersistence.core.persistence.DatabaseField;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.getInstant;
import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.set;

public class AlbumBuilder extends BuilderService<Album> {

    private static final Logger _logger = Logger.getLogger(AlbumBuilder.class.getName());

    private static AlbumBuilder _instance;

    public static AlbumBuilder get_instance() {
        if (_instance == null)
            _instance = new AlbumBuilder();

        return _instance;
    }

    private AlbumBuilder() {
        super(Album.class, _logger);
    }

    @Override
    protected void completion(Album reference, Album object) {
        combine(reference.get_media(), object.get_media());
    }

    @Override
    protected Function<Function<String, DatabaseField>, Album> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID> id = new AtomicReference<>();
            AtomicReference<String> title = new AtomicReference<>();
            Seszt<Album.Media> media = new Seszt<>();
            AtomicReference<Instant> timestamp = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Model.ModelDTO.Fields.id));
                        set(title, table.apply(Model.ModelDTO.Fields.title));
                        combine(media, AlbumMediaBuilder.get_instance().build(resultSet));
                        timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                    },
                    id
            );

            return new Album(
                    new Album.Id(id.get()),
                    title.get(),
                    media,
                    timestamp.get()
            );
        };
    }
}
