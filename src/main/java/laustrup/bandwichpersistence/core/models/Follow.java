package laustrup.bandwichpersistence.core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import laustrup.bandwichpersistence.core.models.users.User;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

public record Follow(@Table.Column("notify") boolean allow_notification, User.Id followerId, User.Id followedId) {

  public Follow(DTO follow) {
    this(follow.isNotify(), new User.Id(follow.getFollowerId()), new User.Id(follow.getFollowedId()));
  }

  @Table.Constructor
  public Follow {
  }

  @Getter
  @FieldNameConstants
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class DTO {

    private final boolean notify;

    private final UUID followerId;

    private final UUID followedId;

    public DTO(Follow follow) {
      notify = follow.allow_notification();
      followerId = follow.followerId().get_value();
      followedId = follow.followedId().get_value();
    }
  }
}
