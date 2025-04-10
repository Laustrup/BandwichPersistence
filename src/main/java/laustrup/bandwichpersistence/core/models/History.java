package laustrup.bandwichpersistence.core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.*;

@Getter
public class History {

    private Seszt<Story> _stories;

    private JoinTableDetails _storyTable;

    public History(JoinTableDetails storyTable) {
        this(new Seszt<>(), storyTable);
    }

    public History(Seszt<Story> stories, JoinTableDetails storyTable) {
        if (stories == null)
            stories = new Seszt<>();

        List<UUID> ownerIds = stories.stream()
                .map(Story::get_ownerId)
                .toList();

        if (!ownerIds.isEmpty() && ownerIds.stream().allMatch(id -> Collections.frequency(ownerIds, id) == ownerIds.size()))
            throw new IllegalArgumentException("Owner Ids not unique for history with stories: " + stories);

        _stories = stories;
        _storyTable = storyTable;
    }

    public UUID get_ownerId() {
        return _stories.getFirst().get_ownerId();
    }

    @Getter
    public static class Story {

        private UUID _id;

        private UUID _ownerId;

        private String _title;

        private Seszt<String> _details;

        private Instant _timestamp;

        public Story(DTO story) {
            _id = story.getId();
            _ownerId = story.getOwnerId();
            _title = story.getTitle();
            _details = new Seszt<>(story.getDetails().stream());
            _timestamp = story.getTimestamp();
        }

        public Story(
                UUID id,
                String title,
                Seszt<String> details,
                Instant timestamp
        ) {

            _id = id;
            _title = title;
            _details = details;
            _timestamp = timestamp;
        }

        public Story(String title) {
            this(title, new Seszt<>());
        }

        public Story(
                String title,
                Seszt<String> details
        ) {
            this(
                null,
                title,
                details,
                null
            );
        }

        @Getter @FieldNameConstants
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DTO {

            private UUID id;

            private UUID ownerId;

            private String title;

            private Set<String> details;

            private Instant timestamp;

            public DTO(Story story) {
                id = story.get_id();
                ownerId = story.get_ownerId();
                title = story.get_title();
                details = story.get_details().asSet();
                timestamp = story.get_timestamp();
            }
        }
    }

    @Getter @AllArgsConstructor
    public enum JoinTableDetails {
        ARTIST("artist_history", "artist_id"),
        ORGANISATION_EMPLOYEE("organisation_employee_history", "organisation_employee_id"),
        EVENT("event_history", "event_id"),;

        private String _table;

        private String _idColumn;
    }
}
