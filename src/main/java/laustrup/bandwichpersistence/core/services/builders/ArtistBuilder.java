package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.Request;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import laustrup.bandwichpersistence.core.models.users.Follow;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static laustrup.bandwichpersistence.core.services.persistence.JDBCService.*;

public class ArtistBuilder extends BuilderService<Artist> {

    private final AlbumBuilder _albumBuilder = new AlbumBuilder();

    private final BandBuilder _bandBuilder = new BandBuilder();

    private final ChatRoomBuilder _chatRoomBuilder = new ChatRoomBuilder();

    private final ContactInfoBuilder _contactInfoBuilder = new ContactInfoBuilder();

    private final SubscriptionBuilder _subscriptionBuilder = new SubscriptionBuilder();

    private final GigBuilder _gigBuilder = new GigBuilder();

    private final VenueRatingBuilder _venueRatingBuilder = new VenueRatingBuilder();

    private final RequestBuilder _requestBuilder = new RequestBuilder();

    public ArtistBuilder() {
        super(Artist.class, ArtistBuilder.class);
    }

    @Override
    protected void completion(Artist reference, Artist object) {
        combine(reference.get_albums(), object.get_albums());
        combine(reference.get_authorities(), object.get_authorities());
        combine(reference.get_chatRooms(), object.get_chatRooms());
        combine(reference.get_bandMemberships(), object.get_bandMemberships());
        combine(reference.get_gigs(), object.get_gigs());
        combine(reference.get_follows(), object.get_follows());
        combine(reference.get_requests(), object.get_requests());
        combine(reference.get_ratings(), object.get_ratings());
        combine(reference.get_history().get_stories() , object.get_history().get_stories());
    }

    @Override
    protected Function<Function<String, Field>, Artist> logic(ResultSet resultSet) {
        return table -> {
            AtomicReference<UUID> id = new AtomicReference<>();
            AtomicReference<String>
                    username = new AtomicReference<>(),
                    firstName = new AtomicReference<>(),
                    lastName = new AtomicReference<>(),
                    description = new AtomicReference<>();
            AtomicReference<ContactInfo> contactInfo = new AtomicReference<>();
            Seszt<Album> albums = new Seszt<>();
            AtomicReference<Subscription> subscription = new AtomicReference<>();
            Seszt<User.Authority> authorities = new Seszt<>();
            Seszt<ChatRoom> chatRooms = new Seszt<>();
            Seszt<Artist.Membership> memberships = new Seszt<>();
            Seszt<Event.Gig> gigs = new Seszt<>();
            AtomicReference<String> runner = new AtomicReference<>();
            Seszt<Follow> follows = new Seszt<>();
            Seszt<Request> requests = new Seszt<>();
            Seszt<Rating> ratings = new Seszt<>();
            AtomicReference<History> history = new AtomicReference<>(new History(History.JoinTableDetails.ARTIST));
            AtomicReference<Instant> timestamp = new AtomicReference<>();

            interaction(
                    resultSet,
                    () -> {
                        set(id, table.apply(Model.ModelDTO.Fields.id));
                        set(username, table.apply(User.UserDTO.Fields.username));
                        set(firstName, table.apply(User.UserDTO.Fields.firstName));
                        set(lastName, table.apply(User.UserDTO.Fields.lastName));
                        set(description, table.apply(User.UserDTO.Fields.description));
                        _contactInfoBuilder.complete(contactInfo, resultSet);
                        combine(albums, _albumBuilder.build(resultSet));
                        _subscriptionBuilder.complete(subscription, resultSet);
                        combine(authorities, User.Authority.valueOf(getString(User.UserDTO.Fields.authorities)));
                        combine(chatRooms, _chatRoomBuilder.build(resultSet));
                        combine(
                                memberships,
                                new Artist.Membership(
                                        _bandBuilder.build(resultSet),
                                        Artist.Membership.Association.valueOf(getString(Artist.Membership.DTO.Fields.association))
                                )
                        );
                        combine(gigs, _gigBuilder.build(resultSet));
                        set(runner, table.apply(Artist.DTO.Fields.runner));
                        combine(follows, new Follow(
                                getBoolean(Follow.DTO.Fields.notify),
                                getUUID(Follow.DTO.Fields.followerId),
                                getUUID(Follow.DTO.Fields.followedId)
                        ));
                        combine(requests, _requestBuilder.build(resultSet));
                        combine(ratings, _venueRatingBuilder.build(resultSet));
                        combine(history.get().get_stories(), HistoryBuilder.buildStory(resultSet, history.get()));
                        timestamp.set(getInstant(Model.ModelDTO.Fields.timestamp));
                    },
                    id
            );

            return new Artist(
                    id.get(),
                    username.get(),
                    firstName.get(),
                    lastName.get(),
                    description.get(),
                    contactInfo.get(),
                    albums,
                    subscription.get(),
                    authorities,
                    chatRooms,
                    new Seszt<>(),
                    memberships,
                    gigs,
                    runner.get(),
                    follows,
                    requests,
                    ratings,
                    history.get(),
                    timestamp.get()
            );
        };
    }
}
