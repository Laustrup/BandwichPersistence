package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.models.chats.ChatRoom;
import laustrup.bandwichpersistence.core.models.users.Participant;
import laustrup.bandwichpersistence.core.utilities.collections.lists.Liszt;
import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import laustrup.bandwichpersistence.core.utilities.console.Printer;
import laustrup.bandwichpersistence.core.utilities.parameters.NotBoolean;
import laustrup.bandwichpersistence.core.models.chats.Request;
import laustrup.bandwichpersistence.core.models.chats.messages.Post;
import laustrup.bandwichpersistence.core.models.users.ContactInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

import static laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt.copy;
import static laustrup.bandwichpersistence.core.utilities.services.UtilityService.toSet;

/**
 * An Event is a place for gigs, where a venue is having bands playing at specific times.
 */
@Getter @FieldNameConstants
public class Event extends Model {

    /**
     * Is when the participants can enter the event,
     * not necessarily the same time as when the gigs start.
     * It must be before or same time as the gigs start.
     */
    private Instant _openDoors;

    /**
     * This DateTime is determining when the first gig will start.
     * Is being calculated automatically.
     */
    private Instant _start;

    /**
     * This DateTime is determining when the last gig will end.
     * Is being calculated automatically.
     */
    private Instant _end;

    /**
     * The amount of time it will take the gigs in total in milliseconds.
     * Is being calculated automatically.
     */
    private long _duration;

    /**
     * The description of the Event, that can be edited by Performers or Venue
     */
    @Setter
    private String _description;

    /**
     * This Event is paid or voluntary.
     */
    @Setter
    private NotBoolean _charity;

    /**
     * If this is a public Event, other Users can view and interact with it.
     * It is public if it is not null, otherwise it has the time that it became public.
     */
    private Instant _public;

    /**
     * Will be true, if this Event is cancelled.
     * Can only be cancelled by the Venue.
     * It is cancelled if it is not null, otherwise it has the time that it became cancelled.
     */
    private Instant _cancelled;

    /**
     * This is marked if there is no more tickets to sell.
     * It is sold out if it is not null, otherwise it has the time that it became sold out.
     */
    @Setter
    private Instant _soldOut;

    /**
     * This is the address or place, whether the Event will be held.
     * Will be used to be search at in Google Maps.
     */
    private ContactInfo.Address _location;

    private ZoneId _zoneId;

    /**
     * The options that are available for tickets to be bought or reserved.
     */
    private Seszt<Ticket.Option> _ticketOptions;

    /**
     * The tickets that have been bought or reserved.
     */
    private Seszt<Ticket> _tickets;

    /**
     * Different information of contacting.
     */
    private ContactInfo _contactInfo;

    /**
     * The gigs with times and acts of the Event.
     */
    private Seszt<Gig> _gigs;

    private Seszt<Organisation> _organisations;

    private ChatRoom _chatRoom;

    /**
     * This venue is the ones responsible for the Event,
     * perhaps even the place it is held, but not necessarily.
     */
    private Venue _venue;

    /**
     * These requests are needed to make sure, everyone wants to be a part of the Event.
     */
    private Seszt<Request> _requests;

    /**
     * The people that will participate in the Event,
     * not including venues or acts.
     */
    private Seszt<Participation> _participations;

    /**
     * Post from different people, that will mention contents.
     */
    private Seszt<Post> _posts;

    /**
     * An Album of images, that can be used to promote this Event.
     */
    @Setter
    private Seszt<Album> _albums;

    private History _history;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param event The transport object to be transformed.
     */
    public Event(DTO event) {
        this(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getOpenDoors(),
                new NotBoolean(event.getIsCharity()),
                event.getIsPublic(),
                event.getIsCancelled(),
                event.getIsSoldOut(),
                new ContactInfo.Address(event.getLocation()),
                event.getZoneId(),
                copy(event.getTicketOptions(), Ticket.Option::new),
                copy(event.getTickets(), Ticket::new),
                new ContactInfo(event.getContactInfo()),
                copy(event.getGigs(), Gig::new),
                copy(event.getOrganisations(), Organisation::new),
                new ChatRoom(event.getChatRoom()),
                new Venue(event.getVenue()),
                copy(event.getRequests(), Request::new),
                copy(event.getParticipations(), Participation::new),
                copy(event.getPosts(), Post::new),
                copy(event.getAlbums(), Album::new),
                event.getHistory(),
                event.getTimestamp()
        );
    }
    
    public Event(
            UUID id,
            String title,
            String description,
            Instant openDoors,
            NotBoolean isCharity,
            Instant isPublic,
            Instant isCancelled,
            Instant isSoldOut,
            ContactInfo.Address location,
            ZoneId zoneId,
            Seszt<Ticket.Option> ticketOptions,
            Seszt<Ticket> tickets,
            ContactInfo contactInfo,
            Seszt<Gig> gigs,
            Seszt<Organisation> organisations,
            ChatRoom chatRoom,
            Venue venue,
            Seszt<Request> requests,
            Seszt<Participation> participations,
            Seszt<Post> posts,
            Seszt<Album> albums,
            History history,
            Instant timestamp
    ) throws InputMismatchException {
        super(id, title == null || title.isEmpty() ? "Untitled event" : title, timestamp);

        _description = description;
        _gigs = gigs;
        if (!_gigs.isEmpty())
            try {
                calculateTime();
            } catch (InputMismatchException e) {
                Printer.print("End date is before beginning date of " + _title + "...", e);
            }
        else {
            _openDoors = openDoors;
            _start = openDoors;
        }

        _organisations = organisations;
        _chatRoom = chatRoom;

        if (_start != null && _end != null)
            if (Duration.between(openDoors, _start).toMinutes() >= 0)
                _openDoors = openDoors;
            else
                throw new InputMismatchException();

        _charity = isCharity;
        _public = isPublic;
        _cancelled = isCancelled;
        _soldOut = isSoldOut;
        _ticketOptions = ticketOptions;
        _tickets = tickets;
        _contactInfo = contactInfo;
        _venue = venue;

        set_location(location);
        _zoneId = zoneId;

        _requests = requests;
        _participations = participations;
        _posts = posts;
        _albums = albums;
        _history = history;
    }

    /**
     * Calculates the duration into minute format.
     * @return The calculated duration in minute format.
     */
    public double durationInMinutes() {
        return (double) (_duration / 10000)/60;
    }

    /**
     * Checks if it is public in the case that there is a time it became public.
     * @return True if there is a time recorded that it became public.
     */
    public boolean isPublic() {
        return _public != null;
    }

    /**
     * Checks if it is cancelled in the case that there is a time it became cancelled.
     * @return True if there is a time recorded that it became cancelled.
     */
    public boolean isCancelled() {
        return _cancelled != null;
    }

    /**
     * Checks if it is sold out in the case that there is a time it became sold out.
     * @return True if there is a time recorded that it became sold out.
     */
    public boolean isSoldOut() {
        return _soldOut != null;
    }

    /**
     * Sets the location.
     * In case that the input is null or empty, it will use the location of the Venue.
     * @param location The new location value.
     * @return The location value.
     */
    public ContactInfo.Address set_location(ContactInfo.Address location) {
        _location = location == null
                ? (
                _venue != null
                        ? (
                        _venue.get_location() != null
                                ? _venue.get_location()
                                : null
                        ) : null
                ) : location;

        return _location;
    }

    /**
     * Adds the given Participation to participations of this Event.
     * @param participation A User that will join this Event.
     * @return All the Participations of this Event.
     */
    public Seszt<Participation> add(Participation participation) {
        return _participations.Add(participation);
    }

    /**
     * Will remove any Requests of this Performer for this Event.
     * @param performer The Performer that should have the Request excluded.
     * @return The Requests of this Event.
     */
    private Seszt<Request> removeRequests(Band performer) {
        for (int i = 1; i <= _requests.size(); i++) {
            if (_requests.Get(i).get_user().get_id() == performer.get_id()) {
                _requests.Remove(i);
                break;
            }
        }

        return _requests;
    }

    /**
     * Adds the given Request to requests of current Event.
     * @param request Determines a specific Request, that is wished to be added.
     * @return All the Requests of the current Event.
     */
    public Seszt<Request> add(Request request) { return add(new Request[]{request}); }

    /**
     * Adds some given Requests to the Liszt of requests from current Event.
     * @param requests Determines some specific requests, that is wished to be added.
     * @return All the requests of the current Event.
     */
    public Seszt<Request> add(Request[] requests) {
        return add(new Liszt<>(requests));
    }

    /**
     * Adds some given Requests to the Liszt of requests from current Event.
     * @param requests Determines some specific requests, that is wished to be added.
     * @return All the requests of the current Event.
     */
    public Seszt<Request> add(Liszt<Request> requests) {
        for (Request request : requests)
            if (!_requests.contains(request))
                _requests.add(request);

        return _requests;
    }

    /**
     * Accepts the request by using a toString() to find the Request.
     * Afterwards using the approve() method to set approved to true.
     * @param request The Request that is wished to have its approved set to true.
     * @return The Request that is changed. If it is not changed, it returns null.
     */
    public Seszt<Request> accept(Request request) {
        if (!_requests.contains(request))
            return null;

        for (Request local : _requests) {
            if (local.get_userId().equals(request.get_userId())) {
                local.set_approved(Instant.now());
                return _requests;
            }
        }

        return null;
    }

    /**
     * Checks if the Venue has approved the Request.
     * @return True if the venue has approved.
     */
    public boolean venueHasApproved() {
        for (Request request : _requests)
            if (request.get_user().getClass() == Organisation.Employee.class && request.isApproved())
                return true;
        return false;
    }

    /**
     * Will cancel the event, if it is the Venue in the input.
     * @param venue Will check if this Venue has the same id as the Venue of this Event.
     * @return The isCancelled Plato value.
     */
    public Instant changeCancelledStatus(Venue venue) {
        if (venue.get_id() == _venue.get_id())
            _cancelled = _cancelled == null
                    ? null
                    : Instant.now();

        return _cancelled;
    }

    /**
     * Adds some given Participations to the Liszt of participations from current Event.
     * @param participations Determines some specific participants, that is wished to be added.
     * @return All the Participations of the current Event.
     */
    public Seszt<Participation> add(Participation[] participations) {
        return _participations.Add(participations);
    }

    /**
     * Removes the given Participation from participations of current Event.
     * @param participation Determines a specific participant, that is wished to be removed.
     * @return All the Participations of the current Event.
     */
    public Seszt<Participation> remove(Participation participation) {
        _participations.remove(participation);
        return _participations;
    }

    /**
     * Adds the given Bulletin to bulletins of this Event.
     * @param post A specific Bulletin, that is wished to be added.
     * @return All the Bulletins of this Event.
     */
    public Seszt<Post> add(Post post) {
        return _posts.Add(post);
    }

    /**
     * Adds the given Album to albums of this Event.
     * @param album A specific Album, that is wished to be added.
     * @return All the Albums of this Event.
     */
    public Seszt<Album> add(Album album) {
        return _albums.Add(album);
    }

    /**
     * Removes the given Bulletin from bulletins of current Event.
     * @param post Determines a specific bulletin, that is wished to be removed.
     * @return All the bulletins of the current Event.
     */
    public Seszt<Post> remove(Post post) {
        _posts.remove(post);
        return _posts;
    }

    /**
     * Sets a Participation's type, by comparing the participants' ids.
     * @param participation The Participation that is wished to be set.
     * @return If the Participation is set successfully, it will return the Participation, else it will return null.
     */
    public Participation set(Participation participation) {
        for (int i = 1; i <= _participations.size(); i++) {
            if (_participations.Get(i).get_participant().get_id() == participation.get_participant().get_id()) {
                _participations.Get(i).set_type(participation.get_type());
                return _participations.Get(i);
            }
        }

        return null;
    }

    /**
     * Sets a Bulletin, by comparing the two ids.
     * @param post The Bulletin that is wished to be set.
     * @return If the Bulletin is set successfully, it will return the Bulletin, else it will return null.
     */
    public Post set(Post post) {
        for (int i = 1; i <= _posts.size(); i++) {
            Post localPost = _posts.Get(i);

            if (localPost.get_id() == post.get_id()) {
                _posts.Set(i, post);
                return _posts.Get(i);
            }
        }

        return null;
    }

    /**
     * Sets a Request, by comparing the two ids of each Request.
     * @param request The Request that is wished to be set.
     * @return If the Request is set successfully, it will return the Request, else it will return null.
     */
    public Request set(Request request) {
        for (int i = 1; i <= _requests.size(); i++) {
            Request localRequest = _requests.Get(i);

            if (
                localRequest.get_userId() == request.get_userId()
                && Objects.equals(localRequest.get_eventId(), request.get_eventId())
            ) {
               return _requests.Set(i, request).Get(i);
            }
        }

        return null;
    }

    /**
     * Sets an Album, by comparing the two ids of each Album.
     * @param album The Album that is wished to be set.
     * @return If the Album is set successfully, it will return the Album, else it will return null.
     */
    public Album set(Album album) {
        for (int i = 1; i <= _albums.size(); i++) {
            Album localAlbum = _albums.Get(i);

            if (localAlbum.get_id() == album.get_id()) {;
                return _albums.Set(i, album).Get(i);
            }
        }

        return null;
    }

    /**
     * Sets the beginning and end of the event to match all the current gigs.
     * Important to use after each change of gigs.
     * @return The calculated length between first beginning and latest end in milliseconds.
     * @throws InputMismatchException In case that the end is before the beginning.
     */
    private long calculateTime() throws InputMismatchException {
        _start = _gigs.isEmpty() ? null : _gigs.Get(1).get_start();
        _end = _gigs.isEmpty() ? null : _gigs.Get(1).get_end();

        if ((_end != null && _start != null) && Duration.between(_end, _start).getSeconds() > 0)
            throw new InputMismatchException();

        if (_start != null && _end != null)
            if (_end.isAfter(_start)) {

                if (_gigs.size() > 1)
                    for (Gig gig : _gigs) {
                        if (gig.get_start().isBefore(_start)) _start = gig.get_start();
                        if (gig.get_end().isAfter(_end)) _end = gig.get_end();
                    }

                _duration = Duration.between(_start, _end).toMillis();
            }

        return _duration;
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[]{
                Model.Fields._id,
                Model.Fields._title,
                Fields._description,
                Model.Fields._timestamp
            },
            new String[]{
                String.valueOf(_id),
                _title,
                _description,
                String.valueOf(_timestamp)
            }
        );
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter
    public static class DTO extends ModelDTO {

        private ZoneId zoneId;

        /**
         * Is when the participants can enter the event,
         * not necessarily the same time as when the gigs start.
         * It must be before or same time as the gigs start.
         */
        private Instant openDoors;

        /**
         * This DateTime is determining when the first gig will start.
         * Is being calculated automatically.
         */
        private Instant start;

        /**
         * This DateTime is determining when the last gig will end.
         * Is being calculated automatically.
         */
        private Instant end;

        /**
         * The amount of time it will take the gigs in total.
         * Is being calculated automatically.
         */
        private long length;

        /**
         * The description of the Event, that can be edited by Performers or Venue
         * */
        private String description;

        /**
         * This Event is paid or voluntary.
         */
        private NotBoolean.Argument isCharity;

        /**
         * If this is a public Event, other Users can view and interact with it.
         */
        private Instant isPublic;

        /**
         * Will be true, if this Event is cancelled.
         * Can only be cancelled by the Venue.
         */
        private Instant isCancelled;

        /**
         * This is marked if there is no more tickets to sell.
         */
        private Instant isSoldOut;

        /**
         * This is the address or place, whether the Event will be held.
         * Will be used to be searched at in Google Maps.
         */
        private ContactInfo.Address.DTO location;

        /**
         * The options that are available for tickets to be bought or reserved.
         */
        private Set<Ticket.Option.DTO> ticketOptions;

        /**
         * The tickets that have been bought or reserved.
         */
        private Set<Ticket.DTO> tickets;

        /** Different information of contacting. */
        private ContactInfo.DTO contactInfo;

        /**
         * The gigs with times and acts of the Event.
         */
        private Set<Gig.DTO> gigs;

        private Set<Organisation.DTO> organisations;

        private ChatRoom.DTO chatRoom;

        /**
         * This venue is the ones responsible for the Event,
         * perhaps even the place it is held, but not necessarily.
         */
        private Venue.DTO venue;

        /**
         * These requests are needed to make sure, everyone wants to be a part of the Event.
         */
        private Set<Request.DTO> requests;

        /**
         * The people that will participate in the Event,
         * not including venues or acts.
         */
        private Set<Participation.DTO> participations;

        /**
         * Post from different people, that will mention contents.
         */
        private Set<Post.DTO> posts;

        /**
         * An Album of images, that can be used to promote this Event.
         */
        private Set<Album.DTO> albums;

        private History history;

        /**
         * Converts into this DTO Object.
         * @param event The Object to be converted.
         */
        public DTO(Event event) {
            super(event);
            description = event.get_description();
            gigs = Seszt.copy(event.get_gigs(), Gig.DTO::new);
            organisations = toSet(event.get_organisations(), Organisation.DTO::new);
            chatRoom = new ChatRoom.DTO(event.get_chatRoom());
            openDoors = event.get_openDoors();
            start = event.get_start();
            end = event.get_end();
            length = event.get_duration();
            isCharity = NotBoolean.ofNullable(event.get_charity()).get_argument();
            isPublic = event.get_public();
            isCancelled = event.get_cancelled();
            isSoldOut = event.get_soldOut();
            ticketOptions = toSet(event.get_ticketOptions(), Ticket.Option.DTO::new);
            tickets = toSet(event.get_tickets(), Ticket.DTO::new);
            contactInfo = new ContactInfo.DTO(event.get_contactInfo());
            venue = new Venue.DTO(event.get_venue());
            location = new ContactInfo.Address.DTO(event.get_location());
            zoneId = event.get_zoneId();
            requests = toSet(event.get_requests(), Request.DTO::new);
            participations = toSet(event.get_participations(), Participation.DTO::new);
            posts = toSet(event.get_posts(), Post.DTO::new);
            albums = toSet(event.get_albums(), Album.DTO::new);
            history = event.get_history();
        }
    }

    /**
     * Determines a specific gig of one band for a specific time.
     */
    @Getter @Setter @FieldNameConstants
    public static class Gig extends Model {

        /**
         * The Event of this Gig.
         */
        private Event _event;

        /**
         * This act is of a Gig and can both be assigned as artists or bands.
         */
        private Seszt<Band> _act;

        /**
         * The start of the Gig, where the act will begin.
         */
        private Instant _start;

        /**
         * The end of the Gig, where the act will end.
         */
        private Instant _end;

        /**
         * Will translate a transport object of this object into a construct of this object.
         * @param gig The transport object to be transformed.
         */
        public Gig(DTO gig) {
            this(
                    gig.getId(),
                    new Event(gig.getEvent()),
                    copy(gig.getAct(), Band::new),
                    gig.getStart(),
                    gig.getEnd(),
                    gig.getTimestamp()
            );
        }

        /**
         * A constructor with all the values of this Object.
         * @param id The primary id that identifies this unique Object.
         * @param event The Event of this Gig.
         * @param act This act is of a Gig and can both be assigned as artists or bands.
         * @param start The start of the Gig, where the act will begin.
         * @param end The end of the Gig, where the act will end.
         * @param timestamp The time this Object was created.
         */
        public Gig(
                UUID id,
                Event event,
                Seszt<Band> act,
                Instant start,
                Instant end,
                Instant timestamp
        ) {
            super(id, "Gig:" + id, timestamp);
            _event = event;
            _act = act;
            _start = start;
            _end = end;
        }

        /**
         * For generating a new Gig.
         * Timestamp will be now.
         * @param event The Event of this Gig.
         * @param act This act is of a Gig and can both be assigned as artists or bands.
         * @param start The start of the Gig, where the act will begin.
         * @param end The end of the Gig, where the act will end.
         */
        public Gig(Event event, Seszt<Band> act, Instant start, Instant end) {
            this(null, event, act, start, end, Instant.now());
        }

        /**
         * Checks if a Performer is a part of the act.
         * @param performer The Performer object that is wished to be checked.
         * @return True if the primary ids matches of the Performer and a Performer of the act,
         *         otherwise false.
         */
        public boolean contains(Band performer) {
            for (Band actor : _act)
                if (actor.get_id() == performer.get_id())
                    return true;

            return false;
        }

        /**
         * Will add a Performer to the act.
         * @param performance The Performer object that is wished to be added.
         * @return All the Performers of the act.
         */
        public Seszt<Band> add(Band performance) {
            return _act.Add(performance);
        }

        /**
         * Removes a performance from its act.
         * @param performance The performance that should be removed from the act.
         * @return The act of this Gig.
         */
        public Seszt<Band> remove(Band performance) {
            _act.remove(performance);
            return _act;
        }

        @Override
        public String toString() {
            return defineToString(
                    getClass().getSimpleName(),
                    new String[] {
                            Model.Fields._id,
                            Fields._start,
                            Fields._end
                    },
                    new String[] {
                            String.valueOf(get_id()),
                            String.valueOf(get_start()),
                            String.valueOf(get_end())
                    }
            );
        }

        /**
         * The Data Transfer Object.
         * Is meant to be used as having common fields and be the body of Requests and Responses.
         * Doesn't have any logic.
         */
        @Getter
        public static class DTO extends ModelDTO {

            /** The Event of this Gig. */
            private Event.DTO event;

            /** This act is of a Gig and can both be assigned as artists or bands. */
            private Set<Band.DTO> act;

            /** The start of the Gig, where the act will begin. */
            private Instant start;

            /** The end of the Gig, where the act will end. */
            private Instant end;

            /**
             * Converts into this DTO Object.
             * @param gig The Object to be converted.
             */
            public DTO(Gig gig) {
                super(gig);
                event = new Event.DTO(gig.get_event());
                act = copy(gig.get_act(), Band.DTO::new);
                start = gig.get_start();
                end = gig.get_end();
            }
        }
    }

    /**
     * Determines type of which a Participant is participating in an Event.
     */
    @Getter @FieldNameConstants
    public static class Participation extends ParticipationBase {

        /**
         * The Participant of the participation.
         */
        private Participant _participant;

        /**
         * Will translate a transport object of this object into a construct of this object.
         * @param participation The transport object to be transformed.
         */
        public Participation(DTO participation) {
            this(
                    new Participant(participation.getParticipant()),
                    participation.getType(),
                    participation.getTimestamp()
            );
        }

        /**
         * A constructor with all the values of this Object.
         * @param participant The Participant of the participation.
         * @param type The type of which participant is participating in the participation.
         */
        public Participation(Participant participant, Type type, Instant timestamp) {
            super(type, timestamp);
            _participant = participant;
        }

        /**
         * For creating a new Participation.
         * Timestamp will be of now.
         * @param participant The Participant of the participation.
         * @param type The type of which participant is participating in the participation.
         */
        public Participation(Participant participant, Type type) {
            this(participant, type, Instant.now());
        }


        @Override
        public String toString() {
            return String.format("""
                    %s(%s=%s,%s=%s)
                    """,
                    getClass().getSimpleName(),
                    Participant.class.getSimpleName(),
                    _participant.get_id(),
                    Type.class.getSimpleName(),
                    _type
            );
        }

        /**
         * The Data Transfer Object.
         * Is meant to be used as having common fields and be the body of Requests and Responses.
         * Doesn't have any logic.
         */
        @Getter @Setter
        public static class DTO extends ParticipationBase.DTO {

            /** The Participant of the participation. */
            private Participant.DTO participant;

            /**
             * Converts into this DTO Object.
             * @param participation The Object to be converted.
             */
            public DTO(Participation participation) {
                super(participation);
                participant = new Participant.DTO(participation.get_participant());
            }
        }
    }
}

