package laustrup.bandwichpersistence.core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import laustrup.bandwichpersistence.core.models.users.User;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

@Getter
public class Follow {

  private final boolean _notify;

  private final User.Id _followerId;

  private final User.Id _followedId;

  public Follow(Follow.DTO follow) {
    this(follow.isNotify(), new User.Id(follow.getFollowerId()), new User.Id(follow.getFollowedId()));
  }

  public Follow(boolean notify, User.Id followerId, User.Id followedId) {
    _notify = notify;
    _followerId = followerId;
    _followedId = followedId;
  }

  @Getter
  @FieldNameConstants
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class DTO {

    private final boolean notify;

    private final UUID followerId;

    private final UUID followedId;

    public DTO(Follow follow) {
      notify = follow.is_notify();
      followerId = follow.get_followerId().get_value();
      followedId = follow.get_followedId().get_value();
    }
  }
}
