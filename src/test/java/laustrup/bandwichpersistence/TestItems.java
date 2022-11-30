package laustrup.bandwichpersistence;

import laustrup.bandwichpersistence.models.Model;
import laustrup.bandwichpersistence.models.events.Event;
import laustrup.bandwichpersistence.models.Rating;
import laustrup.bandwichpersistence.models.albums.Album;
import laustrup.bandwichpersistence.models.chats.ChatRoom;
import laustrup.bandwichpersistence.models.chats.Request;
import laustrup.bandwichpersistence.models.chats.messages.Bulletin;
import laustrup.bandwichpersistence.models.chats.messages.Mail;
import laustrup.bandwichpersistence.models.events.Gig;
import laustrup.bandwichpersistence.models.events.Participation;
import laustrup.bandwichpersistence.models.users.User;
import laustrup.bandwichpersistence.models.users.contact_infos.Address;
import laustrup.bandwichpersistence.models.users.contact_infos.ContactInfo;
import laustrup.bandwichpersistence.models.users.contact_infos.Country;
import laustrup.bandwichpersistence.models.users.contact_infos.Phone;
import laustrup.bandwichpersistence.models.users.sub_users.Performer;
import laustrup.bandwichpersistence.models.users.sub_users.bands.*;
import laustrup.bandwichpersistence.models.users.sub_users.participants.Participant;
import laustrup.bandwichpersistence.models.users.sub_users.subscriptions.Subscription;
import laustrup.bandwichpersistence.models.users.sub_users.subscriptions.SubscriptionOffer;
import laustrup.bandwichpersistence.models.users.sub_users.venues.Venue;
import laustrup.bandwichpersistence.services.TimeService;
import laustrup.bandwichpersistence.utilities.Liszt;
import laustrup.bandwichpersistence.utilities.Plato;
import laustrup.bandwichpersistence.utilities.Printer;

import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains different attributes that imitates models.
 * Primary intended to be used for tests, involving models.
 */
public class TestItems extends JTest {

    /**
     * Will be used to create values for attributes.
     */
    private Random _random = new Random();

    /**
     * Length determine of array collection.
     */
    @Getter
    private int
            _ratingAmount,
            _participantAmount,
            _artistAmount,
            _bandAmount,
            _venueAmount,
            _eventAmount,
            _albumAmount,
            _addressAmount,
            _phoneNumberAmount,
            _contactInfoAmount,
            _chatRoomAmount;

    @Getter
    private Participant[] _participants;

    @Getter
    private Artist[] _artists;

    @Getter
    private Band[] _bands;

    @Getter
    private Venue[] _venues;

    @Getter
    private Event[] _events;

    @Getter
    private Country[] _countries;

    @Getter
    private Phone[] _phones;

    @Getter
    private Address[] _addresses;

    @Getter
    private ContactInfo[] _contactInfo;

    @Getter
    private Album[] _albums;

    @Getter
    private Rating[] _ratings;

    @Getter
    private ChatRoom[] _chatRooms;

    public TestItems() { resetItems(); }

    public void resetItems() {
        // Lengths of collections
        _ratingAmount = 100;
        _participantAmount = 10;
        _artistAmount = 15;
        _bandAmount = 10;
        _venueAmount = 3;
        _eventAmount = 8;
        _albumAmount = (_artistAmount + _bandAmount + _participantAmount) * 2;
        _addressAmount = _artistAmount + _participantAmount + _venueAmount + 5;
        _phoneNumberAmount = _artistAmount + _participantAmount + _venueAmount + 5;
        _contactInfoAmount = _albumAmount;
        _chatRoomAmount = _bandAmount * _venueAmount * _artistAmount;

        // Contact Info
        setupCountries();
        setupPhoneNumbers();
        setupaddresses();
        setupContactInfo();

        // Misc
        setupAlbums();
        setupRatings();

        // Users
        setupParticipants();
        setupArtists();
        setupBands();
        setupVenues();
        setupChatRooms();

        // Events
        setupEvents();
    }

    @Test
    public void itemTest() {
        for (int i = 0; i < 1; i++) {
            try {
                // ARRANGE
                begin();

                // ACT
                resetItems();
                calculatePerformance();

                // ASSERT
                assertTrue(true);
            } catch (Exception e) {
                Printer.get_instance().print("Test items caught an Exception...", e);

                // ASSERT
                assertTrue(false);
            }
        }
    }

    private void setupCountries() {
        _countries = new Country[3];
        _countries[0] = new Country("Denmark", Country.CountryIndexes.DK, 45);
        _countries[1] = new Country("Sverige", Country.CountryIndexes.SE, 46);
        _countries[2] = new Country("Tyskland", Country.CountryIndexes.DE, 49);
    }

    private void setupPhoneNumbers() {
        _phones = new Phone[_phoneNumberAmount];

        for (int i = 0; i < _phones.length; i++)
            _phones[i] = new Phone(_countries[_random.nextInt(_countries.length)],
                    _random.nextInt(89999999)+10000000, _random.nextBoolean());
    }

    private void setupaddresses() {
        _addresses = new Address[_addressAmount];

        for (int i = 0; i < _addresses.length; i++)
            _addresses[i] = new Address("Nørrevang " + _random.nextInt(100),
                    _random.nextInt(10) + (_random.nextBoolean() ? ". tv." : ". th."),
                    String.valueOf(_random.nextInt(8999)+1000), "Holbæk");
    }

    private void setupContactInfo() {
        _contactInfo = new ContactInfo[_contactInfoAmount];

        for (int i = 0; i < _contactInfo.length; i++)
            _contactInfo[i] = new ContactInfo("cool@gmail.com",
                    _phones[_random.nextInt(_phones.length)],
                    _addresses[_random.nextInt(_addresses.length)],
                    _countries[_random.nextInt(_countries.length)]);
    }

    private void setupAlbums() {
        _albums = new Album[_albumAmount];

        for (int i = 0; i < _albums.length; i++) {
            Album.Kind kind = _random.nextBoolean() ? Album.Kind.MUSIC : Album.Kind.IMAGE;
            if (i == 0)
                kind = Album.Kind.MUSIC;
            if (i == 1)
                kind = Album.Kind.IMAGE;

            _albums[i] = new Album(i+1, kind == Album.Kind.MUSIC ? "Debut album" : "Gig photos",
                    new Liszt<>(new String[]{kind == Album.Kind.MUSIC ? "MusicEndpoint" : "PhotoEndpoint"},false),
                    new Participant(), new Liszt<>(), kind, LocalDateTime.now());
        }
    }

    private void setupRatings() {
        _ratings = new Rating[_ratingAmount];

        for (int i = 0; i < _ratings.length; i++)
            _ratings[i] = new Rating(_random.nextInt(5)+1,new Band(), new Participant());
    }

    private void setupParticipants() {
        _participants = new Participant[_participantAmount];
        Liszt<Album> images = sortImageAlbums();

        for (int i = 0; i < _participants.length; i++) {
            int id = i+1;
            boolean gender = _random.nextBoolean();
            _participants[i] = new Participant(id, gender ? "Hansinator "+id : "Ursulanator "+id,
                    gender ? "Hans "+id : "Ursula "+id, "Hansen "+id, "Description "+id,
                    _contactInfo[_random.nextInt(_contactInfo.length)], images.get(_random.nextInt(images.size())+1),
                    randomizeRatings(), new Liszt<>(), new Liszt<>(), Subscription.Status.ACCEPTED,
                    new SubscriptionOffer(TimeService.get_instance().generateRandom(),
                            _random.nextBoolean() ? SubscriptionOffer.Type.SALE : SubscriptionOffer.Type.FREE_TRIAL,
                            _random.nextDouble(1)), new Liszt<>(), LocalDateTime.now(), new Liszt<>());
        }
    }

    private void setupArtists() {
        _artists = new Artist[_artistAmount];
        Liszt<Album> images = sortImageAlbums();

        for (int i = 0; i < _artists.length; i++) {
            int id = i+1;
            boolean gender = _random.nextBoolean();
            _artists[i] = new Artist(id, gender ? "Hansinator "+id : "Ursulanator "+id,
                    gender ? "Hans "+id : "Ursula "+id, "Hansen "+id, "Description "+id,
                    _contactInfo[_random.nextInt(_contactInfo.length)], images.get(_random.nextInt(images.size())+1),
                    randomizeRatings(), new Liszt<>(), new Liszt<>(), new Liszt<>(), setupSubscription(new Artist()), new Liszt<>(),
                    LocalDateTime.now(), sortMusicAlbums(),new Liszt<>(), "Gear "+id, new Liszt<>(), new Liszt<>(),
                    new Liszt<>());
        }
    }

    private void setupBands() {
        _bands = new Band[_bandAmount];
        Liszt<Album> images = sortImageAlbums();

        for (int i = 0; i < _bands.length; i++) {
            int id = i+1;
            Liszt<Artist> members = new Liszt<>();
            int memberAmount = _random.nextInt(_artists.length-1)+1;
            Set<Integer> alreadyTakenIndexes = new HashSet<>();

            for (int j = 0; j < memberAmount; j++) {
                int index = _random.nextInt(_artists.length);

                while (alreadyTakenIndexes.contains(index)) index = _random.nextInt(_artists.length);
                alreadyTakenIndexes.add(index);

                members.add(_artists[index]);
            }

            Liszt<User> fans = new Liszt<>();
            int fanAmount = _random.nextInt(_participants.length);
            alreadyTakenIndexes = new HashSet<>();

            for (int j = 0; j < fanAmount; j++) {
                int index = _random.nextInt(_participants.length);

                while (alreadyTakenIndexes.contains(index)) index = _random.nextInt(_participants.length);
                alreadyTakenIndexes.add(index);

                fans.add(_participants[index]);
            }

            _bands[i] = new Band(id, "Band "+id, "Description "+id,
                    _contactInfo[_random.nextInt(_contactInfo.length)], images.get(_random.nextInt(images.size())+1),
                    randomizeRatings(), new Liszt<>(), new Liszt<>(), new Liszt<>(), setupSubscription(new Band()), new Liszt<>(),
                    LocalDateTime.now(), sortMusicAlbums(), members, "Gear "+id,fans, new Liszt<>());

            for (Artist member : _bands[i].get_members()) _artists[(int) (member.get_primaryId()-1)].addBand(_bands[i]);
            for (User fan : _bands[i].get_fans()) _participants[(int) (fan.get_primaryId() - 1)].add(_bands[i]);
        }
    }

    private Subscription setupSubscription(User user) {
        Subscription.Type type = _random.nextBoolean() ? Subscription.Type.PREMIUM_ARTIST : Subscription.Type.PREMIUM_BAND;
        type = _random.nextBoolean() ? type : Subscription.Type.FREEMIUM;
        return new Subscription(user, type, Subscription.Status.ACCEPTED, new SubscriptionOffer(TimeService.get_instance().generateRandom(),
                _random.nextBoolean() ? SubscriptionOffer.Type.SALE : SubscriptionOffer.Type.FREE_TRIAL,
                _random.nextDouble(1)), _random.nextBoolean() ? _random.nextLong(101) : null);
    }

    private void setupVenues() {
        _venues = new Venue[_venueAmount];
        Liszt<Album> images = sortImageAlbums();

        for (int i = 0; i < _venues.length; i++) {
            int id = i+1;
            _venues[i] = new Venue(id, "Venue "+id, "Description "+id,
                    _contactInfo[_random.nextInt(_contactInfo.length)], images.get(_random.nextInt(images.size())+1),
                    randomizeRatings(), new Liszt<>(), new Liszt<>(), LocalDateTime.now(), "Location "+id,
                    "Gear "+id, Subscription.Status.ACCEPTED,
                    new SubscriptionOffer(TimeService.get_instance().generateRandom(),
                            _random.nextBoolean() ? SubscriptionOffer.Type.SALE : SubscriptionOffer.Type.FREE_TRIAL,
                            _random.nextDouble(1)), new Liszt<>(), _random.nextInt(101), new Liszt<>());
        }
    }

    private void setupEvents() {
        _events = new Event[_eventAmount];
        Liszt<Album> images = sortImageAlbums();

        for (int i = 0; i < _events.length; i++) {
            int id = i+1;
            int gigAmount = _random.nextInt(5)+1;
            int gigLengths = _random.nextInt(35)+11;
            LocalDateTime startOfLatestGig = TimeService.get_instance().generateRandom();

            _events[i] = new Event(id,"Event title " + id, "Event description " + id,
                    startOfLatestGig.minusMinutes(gigAmount*gigAmount).minusHours(5),
                    generatePlato(), generatePlato(), generatePlato(), generatePlato(), "Location " + id,
                    _random.nextDouble(498)+1, "https://www.Billetlugen.dk/"+id,
                    _contactInfo[_random.nextInt(_contactInfo.length)],
                    generateGigs(startOfLatestGig, gigAmount, gigLengths),
                    _venues[_random.nextInt(_venues.length)], new Liszt<>(), generateParticipations(), new Liszt<>(),
                    images.get(_random.nextInt(images.size())+1), LocalDateTime.now());

            for (Gig gig : _events[i].get_gigs())
                _events[i].add(generateRequests(gig.get_act(), _events[i]));

            _events[i].add(generateBulletins(_events[i]));

            setPerformersForEvents(_events[i]);
        }
    }

    public Liszt<Gig> generateGigs(LocalDateTime latestGig, int amount, int gigLengths) {
        Liszt<Gig> gigs = new Liszt<>();
        LocalDateTime start = latestGig;

        for (int i = 0; i < amount; i++) {
            Performer[] act = generateAct();
            LocalDateTime end = start.plusMinutes(gigLengths);

            gigs.add(new Gig(gigs.size()+i+1, act, start, end, LocalDateTime.now()));

            start = start.minusMinutes(gigLengths);
        }

        return gigs;
    }

    private void setPerformersForEvents(Event event) {
        for (Gig gig : event.get_gigs()) {
            for (Performer performer : gig.get_act()) {
                if (performer.getClass() == Band.class) {
                    _bands[(int) performer.get_primaryId()-1].add(event);
                    _bands[(int) performer.get_primaryId()-1].add(gig);
                    for (Artist artist : _bands[(int) performer.get_primaryId()-1].get_members()) {
                        _artists[(int) artist.get_primaryId()-1].add(event);
                        _artists[(int) artist.get_primaryId()-1].add(gig);
                    }
                }
                else if (performer.getClass() == Artist.class)
                    _artists[(int) performer.get_primaryId()-1].add(event);
                _artists[(int) performer.get_primaryId()-1].add(gig);
            }
        }
        for (Request request : event.get_requests()) {
            User user = request.get_user();

            if (user.getClass() == Venue.class)
                _venues[(int) user.get_primaryId()-1].add(request);
            else if (user.getClass() == Artist.class)
                _artists[(int) user.get_primaryId()-1].add(request);
            else if (user.getClass() == Band.class)
                for (Artist artist : ((Band) user).get_members())
                    _artists[(int) artist.get_primaryId()-1].add(request);
        }
    }

    private Performer[] generateAct() {
        Performer[] performers = new Performer[_random.nextInt(3)+1];
        Set<Performer> set = new HashSet<>();

        for (int i = 0; i < performers.length; i++) {
            Performer performer = _random.nextBoolean() ? _bands[_random.nextInt(_bands.length)] :
                    _artists[_random.nextInt(_artists.length)];
            while (set.contains(performer)) performer = _random.nextBoolean() ? _bands[_random.nextInt(_bands.length)] :
                    _artists[_random.nextInt(_artists.length)];
            set.add(performer);
            performers[i] = performer;
        }

        return performers;
    }

    private Request[] generateRequests(Performer[] performers, Event event) {
        Request[] requests = new Request[performers.length];
        for (int i = 0; i < performers.length; i++)
            requests[i] = new Request(performers[i], event, generatePlato());

        return requests;
    }

    public Liszt<Participation> generateParticipations() {
        Liszt<Participation> participations = new Liszt<>();
        Set<Participant> set = new HashSet<>();
        int amount = _random.nextInt(_artistAmount+_participantAmount);

        for (int i = 0; i < amount; i++) {
            Participant participant = _random.nextBoolean() ? _artists[_random.nextInt(_artists.length)] :
                    _participants[_random.nextInt(_participantAmount)];
            while (set.contains(participant)) participant = _random.nextBoolean() ? _artists[_random.nextInt(_artists.length)] :
                    _participants[_random.nextInt(_participantAmount)];
            set.add(participant);

            participations.add(new Participation(participant,generateParticipationType()));
        }

        return participations;
    }

    public Participation.ParticipationType generateParticipationType() {
        return switch (_random.nextInt(4) + 1) {
            case 1 -> Participation.ParticipationType.ACCEPTED;
            case 2 -> Participation.ParticipationType.IN_DOUBT;
            case 3 -> Participation.ParticipationType.CANCELED;
            default -> Participation.ParticipationType.INVITED;
        };
    }

    public Bulletin[] generateBulletins(Model model) {
        Bulletin[] bulletins = new Bulletin[_random.nextInt(101)];

        for (int i = 0; i < bulletins.length; i++) {
            long id = i+1;
            bulletins[i] = new Bulletin(id, generateUser(), model, "Content "+id, _random.nextBoolean(),
                    _random.nextBoolean(), _random.nextBoolean(), LocalDateTime.now());
        }

        return bulletins;
    }

    public User generateUser() {
        return switch (_random.nextInt(3) + 1) {
            case 1 -> _participants[_random.nextInt(_participants.length)];
            case 2 -> _artists[_random.nextInt(_artists.length)];
            default -> _venues[_random.nextInt(_venues.length)];
        };
    }

    private Plato generatePlato() { return _random.nextBoolean() ? new Plato(_random.nextBoolean()) : new Plato(); }

    private void setupChatRooms() {
        _chatRooms = new ChatRoom[_chatRoomAmount];

        for (int i = 0; i < _chatRooms.length; i++) {
            int id = i+1;
            Liszt<User> members = new Liszt<>();
            int memberAmount = _random.nextInt(_venues.length+_artists.length - 1) + 1;
            Set<User> memberSet = new HashSet<>();

            for (int j = 0; j < memberAmount; j++) {
                User user;

                do { user = generateUser(); } while (memberSet.contains(user));
                memberSet.add(user);

                members.add(user);
            }

            _chatRooms[i] = new ChatRoom(id, "Chatroom "+id,
                    generateMails(members), members,
                    members.get(_random.nextInt(members.size())+1), LocalDateTime.now()
            );
        }
    }

    private Liszt<Mail> generateMails(Liszt<User> members) {
        Liszt<Mail> mails = new Liszt<>();
        int amount = _random.nextInt(101);

        for (int i = 0; i < amount; i++) {
            String content = new String();
            int contentAmount = _random.nextInt(101);

            for (int j = 0; j < contentAmount; j++) content += "Test ";

            mails.add(new Mail(i+1, new ChatRoom(), members.get(_random.nextInt(members.size())+1),
                    content, _random.nextBoolean(), _random.nextBoolean(), _random.nextBoolean(), LocalDateTime.now()));
        }

        return mails;
    }

    private Liszt<Rating> randomizeRatings() {
        Liszt<Rating> ratings = new Liszt<>();
        int amount = _random.nextInt(_ratings.length)+1;

        for (int i = 0; i < amount; i++) ratings.add(_ratings[_random.nextInt(_ratings.length)]);

        return ratings;
    }

    private Liszt<Album> sortMusicAlbums() {
        Liszt<Album> music = new Liszt<>();
        for (Album album : _albums)
            if (album.get_kind() == Album.Kind.MUSIC) music.add(album);

        return music;
    }

    private Liszt<Album> sortImageAlbums() {
        Liszt<Album> images = new Liszt<>();
        for (Album album : _albums)
            if (album.get_kind() == Album.Kind.IMAGE)
                images.add(album);

        return images;
    }
}
