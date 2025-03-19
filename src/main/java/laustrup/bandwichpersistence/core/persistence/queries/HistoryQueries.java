package laustrup.bandwichpersistence.core.persistence.queries;

import laustrup.bandwichpersistence.core.models.History;
import laustrup.bandwichpersistence.core.persistence.Query;
import laustrup.bandwichpersistence.core.services.persistence.queries.FormatQueryService;
import laustrup.bandwichpersistence.core.services.persistence.queries.JoinTableScriptService;
import lombok.Getter;

import java.util.List;

public class HistoryQueries {

    private static final String _storyId =  "@story_id";

    public static Query insertIntoStoriesQuery(int index) {
        return new Query(
                FormatQueryService.formatQuery(/*language=mysql*/ """
                        set %5 = uuid_to_bin(uuid());
                        
                        insert ignore into stories(
                            id,
                            title
                        ) values (
                            %5,
                            %s
                        );
                        
                        """,
                        _storyId,
                        _storyId
                ),
                new Query.Parameter(index, Parameter.STORY_TITLE.get_key())
        );
    }

    public static Query insertIntoStoryDetailsQuery(int index) {
        return new Query(
                FormatQueryService.formatQuery(/*language=mysql*/ """
                        insert ignore into story_details(
                            story_id,
                            content
                        ) values (
                            %5,
                            %s
                        );
                        """,
                        _storyId
                ),
                new Query.Parameter(index, Parameter.STORY_DETAIL_CONTENT.get_key())
        );
    }

    @Getter
    public enum Parameter {
        STORY_ID("story_id"),
        STORY_TITLE("story_title"),
        STORY_DETAIL_CONTENT("story_content");

        private final String _key;

        Parameter(String key) {
            _key = Query.formatKey(key);
        }
    }
}
