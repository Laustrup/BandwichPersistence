package laustrup.bandwichpersistence.core.models;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class Tag extends Model {

    private User _tagger;

    private User _target;

    private Model _model;

    public Tag(
            UUID id,
            User tagger,
            User target,
            Model model,
            Instant timestamp
    ) {
        super(id, tagger.get_title() + "-" + target.get_title(), timestamp);
        _tagger = tagger;
        _target = target;
        _model = model;
    }
}
