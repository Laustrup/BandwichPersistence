package laustrup.bandwichpersistence.core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.models.identification.CommonIdentity;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.Participant;
import laustrup.bandwichpersistence.core.models.users.User;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import laustrup.bandwichpersistence.core.services.UserService;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static laustrup.bandwichpersistence.core.services.ModelService.toStringify;

/**
 * Extends performer and contains Artists as members
 */
@Getter
@FieldNameConstants
@Table
public class Band extends Model<Band.Id, Signature.UUID> {

  private final String _description;

  private final Subscription _subscription;

  private final Seszt<Album> _albums;

  private final Seszt<Event> _events;

  private final Seszt<User<? extends User.Id>> _fans;

  private final Seszt<Post> _posts;

  private final String _name;

  private final String _runner;

  /**
   * Will translate a transport object of this object into a construct of this object.
   *
   * @param band The transport object to be transformed.
   */
  public Band(Band.DTO band) {
    this(
        new Id(band.getId()),
        band.getName(),
        band.getDescription(),
        new Seszt<>(band.getAlbums().stream().map(Album::new)),
        new Seszt<>(band.getEvents().stream().map(Event::new)),
        new Subscription(band.getSubscription()),
        new Seszt<>(band.getPosts().stream().map(Post::new)),
        band.getRunner(),
        new Seszt<>(band.getFans().stream().map(UserService::from)),
        band.getTimestamp()
    );
  }

  @Table.Constructor
  public Band(
      Id id,
      String name,
      String description,
      Seszt<Album> albums,
      Seszt<Event> events,
      Subscription subscription,
      Seszt<Post> posts,
      String runner,
      Seszt<User<? extends User.Id>> fans,
      Instant timestamp
  ) {
    super(id, timestamp);
    _name = name;
    _description = description;
    _albums = albums;
    _events = events;
    _fans = fans;
    _subscription = subscription;
    _posts = posts;
    _runner = runner;
  }

  /**
   * Removes a Fan of the Liszt of fans.
   *
   * @param fan An object of Fan, that is wished to be removed.
   * @return The whole Liszt of fans.
   */
  public Seszt<User<? extends User.Id>> remove(Participant fan) {
    return _fans.remove(new Participant[]{fan});
  }

  public static class Id extends CommonIdentity<Signature.UUID> {

    public Id(Signature.UUID signature) {
      super(signature);
    }

    public Id(java.util.UUID signature) {
      super(new Signature.UUID(signature));
    }

    @Override
    public Class<?> getOwnerClassType() {
      return Band.class;
    }
  }

  @Override
  public String toString() {
    return toStringify(this);
  }

  /**
   * The Data Transfer Object.
   * Is meant to be used as having common fields and be the body of Requests and Responses.
   * Doesn't have any logic.
   */
  @Getter
  @FieldNameConstants
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class DTO extends ModelDTO<Band.Id, Signature.UUID, java.util.UUID> {

    private final String description;

    private final Subscription.DTO subscription;

    private final Set<Album.DTO> albums;

    private final Set<Event.DTO> events;

    private final Set<User.UserDTO<? extends User.Id>> fans;

    private final Set<Post.DTO> posts;

    private final String name;

    private final String runner;

    /**
     * Converts into this DTO Object.
     *
     * @param band The Object to be converted.
     */
    public DTO(Band band) {
      super(band);
      description = band.get_description();
      subscription = new Subscription.DTO(band.get_subscription());
      albums = Arrays.stream(band.get_albums().get_data())
          .map(Album.DTO::new)
          .collect(Collectors.toSet());
      events = Arrays.stream(band.get_events().get_data())
          .map(Event.DTO::new)
          .collect(Collectors.toSet());
      fans = Arrays.stream(band.get_fans().get_data())
          .map(UserService::from)
          .collect(Collectors.toSet());
      posts = Arrays.stream(band.get_posts().get_data())
          .map(Post.DTO::new)
          .collect(Collectors.toSet());

      name = band.get_name();
      runner = band.get_runner();
    }
  }

  @Getter
  @Table(value = "band_memberships")
  public static class Membership {

    private final Artist _member;

    private final Association _association;

    public Membership(DTO membership) {
      _member = new Artist(membership.getMember());
      _association = membership.getAssociation();
    }

    public Membership(Artist member, Association association) {
      _member = member;
      _association = association;
    }

    public enum Association {
      SESSION,
      OWNER
    }

    @Getter
    @FieldNameConstants
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DTO {

      private final Artist.DTO member;

      private final Association association;

      public DTO(Membership membership) {
        member = new Artist.DTO(membership.get_member());
        association = membership.get_association();
      }
    }
  }
}
