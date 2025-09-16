package laustrup.bandwichpersistence.core.services.builders;

import laustrup.bandwichpersistence.core.models.*;
import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.chats.Request;
import laustrup.bandwichpersistence.core.models.users.Artist;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import org.apache.logging.log4j.message.Message;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class BandwichBuilderServiceCollection implements BuilderServiceCollection {

    private static BandwichBuilderServiceCollection _instance;

    private static final Map<Class<?>, BuilderService<?>> BUILDER_SERVICES = generateBuilderServiceMap(
            MapSection.of(ContactInfo.class, ContactInfoBuilder.get_instance()),
            MapSection.of(ContactInfo.Address.class, AddressBuilder.get_instance()),
            MapSection.of(ContactInfo.Phone.class, PhoneBuilder.get_instance()),
            MapSection.of(ContactInfo.Country.class, CountryBuilder.get_instance()),
            MapSection.of(Album.class, AlbumBuilder.get_instance()),
            MapSection.of(Album.Media.class, AlbumMediaBuilder.get_instance()),
            MapSection.of(Artist.class, ArtistBuilder.get_instance()),
            MapSection.of(Band.class, BandBuilder.get_instance()),
            MapSection.of(ChatRoom.class, ChatRoomBuilder.get_instance()),
            MapSection.of(ChatRoom.Template.class, ChatRoomTemplateBuilder.get_instance()),
            MapSection.of(Event.class, EventBuilder.get_instance()),
            MapSection.of(Event.Gig.class, GigBuilder.get_instance()),
            MapSection.of(Login.class, LoginBuilder.get_instance()),
            MapSection.of(Message.class, MessageBuilder.get_instance()),
            MapSection.of(Organisation.class, OrganisationBuilder.get_instance()),
            MapSection.of(Organisation.Employee.class, OrganisationEmployeeBuilder.get_instance()),
            MapSection.of(PostBuilder.class, PostBuilder.get_instance()),
            MapSection.of(Rating.class, RatingBuilder.get_instance()),
            MapSection.of(Request.class, RatingBuilder.get_instance()),
            MapSection.of(Subscription.class, SubscriptionBuilder.get_instance()),
            MapSection.of(Ticket.class, TicketBuilder.get_instance()),
            MapSection.of(Ticket.Option.class, TicketOptionBuilder.get_instance()),
            MapSection.of(Venue.class, VenueBuilder.get_instance()),
            MapSection.of(Venue.Rating.class, VenueRatingBuilder.get_instance())
    );

    public static BandwichBuilderServiceCollection getInstance() {
        if(_instance == null)
            _instance = new BandwichBuilderServiceCollection();

        return _instance;
    }

    private BandwichBuilderServiceCollection() {

    }

    private static Map<Class<?>, BuilderService<?>> generateBuilderServiceMap(MapSection... mapSections) {
        return Arrays.stream(mapSections)
                .map(MapSection::toEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<Class<?>, BuilderService<?>> get_builderServices() {
        return BUILDER_SERVICES;
    }

    public BuilderService<?> get_builderService(Class<?> clazz) {
        return get_builderServices().get(clazz);
    }

    private record MapSection(Class<?> key, BuilderService<?> service) {

        public AbstractMap.SimpleImmutableEntry<Class<?>, BuilderService<?>> toEntry() {
            return new AbstractMap.SimpleImmutableEntry<>(key, service);
        }

        public static MapSection of(Class<?> key, BuilderService<?> service) {
            return new MapSection(key, service);
        }
    }
}
