package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.services.persistence.JDBCService;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class BandBuilder {

    private static final Logger _logger = Logger.getLogger(AlbumBuilder.class.getSimpleName());

    public static Band build(ResultSet resultset) {
        AtomicReference<UUID> id = new AtomicReference<>();
        AtomicReference<Subscription> subscription = new AtomicReference<>();
        AtomicReference<Instant> timestamp = new AtomicReference<>();
        AtomicReference<String>
                name = new AtomicReference<>(),
                description = new AtomicReference<>(),
                runner = new AtomicReference<>();

        Seszt<Album> albums;
        Seszt<Event> events;
        Seszt<Post> posts;
        Seszt<User> fans;

        try {
            JDBCService.build(
                    resultset,
                    () -> {
                        id.set(get(
                                column -> getUUID(resultset, column),
                                Model.ModelDTO.Fields.id
                        ));
                        name.set(get(
                                column -> getString(resultset, column),
                                Band.DTO.Fields.name
                        ));
                        description.set(get(
                                column -> getString(resultset, column),
                                Band.DTO.Fields.description
                        ));
                        albums.add(AlbumBuilder.build(resultset)
                        ));


                    }
            )
        }

        return Response.ifNull(
                new Band(
                        id,
                        name,

                        )
        );
    }
}
