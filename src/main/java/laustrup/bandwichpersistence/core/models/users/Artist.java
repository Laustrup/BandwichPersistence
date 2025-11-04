package laustrup.bandwichpersistence.core.models.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.Request;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Junction;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static laustrup.bandwichpersistence.core.services.ModelService.toStringify;

@Getter @FieldNameConstants @Table
public class Artist extends BusinessUser<Artist.Id> {

  /**
   * The Bands that the Artist is a member of.
   */
  private final Seszt<Membership> _bandMemberships;

  /**
   * The Requests requested for this Artist.
   */
  private final Seszt<Request> _requests;

  private final Seszt<Album> _albums;

  private final Seszt<Follow> _follows;

  private final Seszt<Event.Gig> _gigs;

  private final Seszt<Rating> _ratings;

  private final Seszt<Authority> _authorities;

  /**
   * A description of the gear, that the Artist possesses and what they require for an Event.
   */
  @Setter
  private String _runner;

  /**
   * Will translate a transport object of this object into a construct of this object.
   * @param artist The transport object to be transformed.
   */
  public Artist(DTO artist) {
    this(
        new Artist.Id(artist.getId()),
        artist.getUsername(),
        artist.getFirstName(),
        artist.getLastName(),
        artist.getDescription(),
        new ContactInfo(artist.getContactInfo()),
        new Seszt<>(artist.getAlbums().stream().map(Album::new)),
        new Subscription(artist.getSubscription()),
        new Seszt<>(artist.getAuthorities().stream()),
        new Seszt<>(artist.getChatRooms().stream().map(ChatRoom::new)),
        new Seszt<>(artist.getParticipations().stream().map(Participation::new)),
        new Seszt<>(artist.getBandMemberships().stream().map(Artist.Membership::new)),
        new Seszt<>(artist.getGigs().stream().map(Event.Gig::new)),
        artist.getRunner(),
              new Seszt<>(artist.getFollows().stream().map(Follow::new)),
                new Seszt<>(artist.getRequests().stream().map(Request::new)),
                new Seszt<>(artist.getRatings().stream().map(Rating::new)),
                artist.getTimestamp()
        );
    }

    public Artist(
            Artist.Id id,
            String username,
            String firstName,
            String lastName,
            String description,
            ContactInfo contactInfo,
            Seszt<Album> albums,
            Subscription subscription,
            Seszt<Authority> authorities,
            Seszt<ChatRoom> chatRooms,
            Seszt<Participation> participations,
            Seszt<Membership> bandMemberships,
            Seszt<Event.Gig> gigs,
            String runner,
            Seszt<Follow> follows,
            Seszt<Request> requests,
            Seszt<Rating> ratings,
            Instant timestamp
    ) {
        super(
                id,
                username,
                firstName,
                lastName,
                description,
                contactInfo,
                subscription,
                chatRooms,
                participations,
                timestamp
        );
        _albums = albums;
        _bandMemberships = bandMemberships;
        _requests = requests;
        _ratings = ratings;
        _runner = runner;
        _gigs = gigs;
        _follows = follows;
        _authorities = authorities;
    }

    /**
     * Adds a Request to the Liszt of Requests.
     * @param request An object of Request, that is wished to be added.
     * @return The whole Liszt of Requests.
     */
    public Seszt<Request> add(Request request) {
        return _requests.Add(request);
    }

    /**
     * Removes a Request of the Liszt of Requests.
     * @param request An object of Request, that is wished to be removed.
     * @return The whole Liszt of Requests.
     */
    public Seszt<Request> remove(Request request) {
        return _requests.remove(new Request[]{request});
    }

    public static class Id extends User.Id {

        public Id(Signature.UUID identifier) {
            super(identifier);
        }

        public Id(java.util.UUID identifier) {
            super(identifier);
        }

        @Override
        public Class<?> getOwnerClassType() {
            return User.Id.class;
        }
    }

    @Override
    public String toString() {
        return toStringify(this);
    }

    @Junction(
        title = "artist_authorities",
        entityColumns = {
            @Table.Column(value = "artist_id"),
            @Table.Column(value = "authority_id")
        }
    )
    public enum Authority implements laustrup.bandwichpersistence.core.models.identification.Authority {
        STANDARD,
        ADMIN
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter @FieldNameConstants
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DTO extends BusinessUserDTO<Artist.Id> {

        /**
         * The Bands that the Artist is a member of.
         */
        private Set<Artist.Membership.DTO> bandMemberships;

        /**
         * The Requests requested for this Artist.
         */
        private Set<Request.DTO> requests;

        /**
         * Describes all the gigs, that the Performer is a part of an act.
         */
        protected Set<Event.Gig.DTO> gigs;

        /**
         * All the participants that are following this Performer, is included here.
         */
        protected Set<Follow.DTO> follows;

        protected Set<Album.DTO> albums;

        protected Set<Rating.DTO> ratings;

        protected Set<Authority> authorities;

        /**
         * A description of the gear, that the Artist possesses and what they require for an Event.
         */
        @Setter
        private String runner;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public DTO(
                @JsonProperty java.util.UUID id,
                @JsonProperty String username,
                @JsonProperty String firstName,
                @JsonProperty String lastName,
                @JsonProperty String description,
                @JsonProperty ContactInfo.DTO contactInfo,
                @JsonProperty Set<Participation.DTO> participations,
                @JsonProperty Subscription.DTO subscription,
                @JsonProperty Set<ChatRoom.DTO> chatRooms,
                @JsonProperty Set<Authority> authorities,
                @JsonProperty Instant timestamp,
                @JsonProperty Set<Membership.DTO> bandMemberships,
                @JsonProperty Set<Request.DTO> requests,
                @JsonProperty Set<Event.Gig.DTO> gigs,
                @JsonProperty Set<Follow.DTO> follows,
                @JsonProperty Set<Album.DTO> albums,
                @JsonProperty Set<Rating.DTO> ratings,
                @JsonProperty String runner
        ) {
            super(
                    id,
                    username,
                    firstName,
                    lastName,
                    description,
                    contactInfo,
                    participations,
                    subscription,
                    chatRooms,
                    timestamp
            );
            this.bandMemberships = bandMemberships;
            this.requests = requests;
            this.gigs = gigs;
            this.follows = follows;
            this.albums = albums;
            this.ratings = ratings;
            this.runner = runner;
            this.authorities = authorities;
        }

        /**
         * Converts into this DTO Object.
         * @param artist The Object to be converted.
         */
        public DTO(Artist artist) {
            super(artist);
            bandMemberships = artist.get_bandMemberships().stream().map(Artist.Membership.DTO::new).collect(Collectors.toSet());
            requests = artist.get_requests().stream().map(Request.DTO::new).collect(Collectors.toSet());
            gigs = artist.get_gigs().stream().map(Event.Gig.DTO::new).collect(Collectors.toSet());
            follows = artist.get_follows().stream().map(Follow.DTO::new).collect(Collectors.toSet());
            albums = artist.get_albums().stream().map(Album.DTO::new).collect(Collectors.toSet());
            ratings = artist.get_ratings().stream().map(Rating.DTO::new).collect(Collectors.toSet());
            runner = artist.get_runner();
            authorities = new HashSet<>(artist.get_authorities());
        }
    }

    @Getter @Table(value = "band_memberships") @Table.Target
    public static class Membership {

        private final Band _band;

        private final Artist.Membership.Association _association;

        public Membership(Artist.Membership.DTO membership) {
            _band = new Band(membership.getMember());
            _association = membership.getAssociation();
        }

        public Membership(Band band, Artist.Membership.Association association) {
            _band = band;
            _association = association;
        }

        public enum Association {
            SESSION,
            OWNER
        }

        @Getter @FieldNameConstants
        public static class DTO {

            private final Band.DTO member;

            private final Artist.Membership.Association association;

            public DTO(Artist.Membership membership) {
                member = new Band.DTO(membership.get_band());
                association = membership.get_association();
            }
        }
    }
}
