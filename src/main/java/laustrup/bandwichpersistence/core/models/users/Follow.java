package laustrup.bandwichpersistence.core.models.users;

import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

@Getter
public class Follow {

    private boolean _notify;

    private UUID _followerId;

    private UUID _followedId;

    public Follow(Follow.DTO follow) {
        _notify = follow.isNotify();
        _followerId = follow.getFollowerId();
        _followedId = follow.getFollowedId();
    }

    public Follow(boolean notify, UUID followerId, UUID followedId) {
        _notify = notify;
        _followerId = followerId;
        _followedId = followedId;
    }

    @Getter @FieldNameConstants
    public static class DTO {

        private boolean notify;

        private UUID followerId;

        private UUID followedId;

        public DTO(Follow follow) {
            notify = follow.is_notify();
            followerId = follow.get_followerId();
            followedId = follow.get_followedId();
        }
    }
}
