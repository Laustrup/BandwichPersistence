package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The ticket that have been bought for an event from an option of the event.
 */
@Getter
@FieldNameConstants
public class Ticket extends TicketBase {

    /**
     * Indicates the time that the participant has arrived to the event.
     * If it is null, the participant has not had his ticket scanned yet.
     */
    @Setter
    private LocalDateTime _arrived;

    /**
     * The option that this ticket was created from.
     */
    private Option _option;

    /**
     * Converts a Data Transport Object into this object.
     * @param ticket The Data Transport Object that will be converted.
     */
    public Ticket(DTO ticket) {
        super(ticket);
        _arrived = ticket.getArrived();
        _option = new Option(ticket.getOption());
    }

    /**
     * Constructor with all values.
     * In order for the ticket to be created, it also needs all the values.
     * @param participantId A hex decimal value identifying this item uniquely of the participant.
     * @param eventId A hex decimal value identifying this item uniquely of the event.
     * @param title A title describing this entity internally.
     * @param seat In case that this string is null, then it is a standing event.
     *             Otherwise, it is the seat number.
     * @param price The amount of money that the ticket costs.
     * @param valuta The type of valuta that the ticket is in.
     * @param arrived Indicates the time that the participant has arrived to the event.
     *                If it is null, the participant has not had his ticket scanned yet.
     * @param option The option that this ticket was created from.
     * @param history A collection of events that has occurred.
     * @param timestamp Specifies the time this entity was created.
     */
    public Ticket(
            UUID participantId,
            UUID eventId,
            String title,
            String seat,
            BigDecimal price,
            String valuta,
            LocalDateTime arrived,
            Option option,
            History history,
            Instant timestamp
    ) {
        super(
                participantId,
                eventId,
                title,
                seat,
                price,
                valuta,
                history,
                timestamp
        );
        _arrived = arrived;
        _option = option;
    }

    @Override
    public String toString() {
        return defineToString(
                getClass().getSimpleName(),
                new String[]{
                        Model.Fields._id,
                        Model.Fields._secondaryId,
                        Model.Fields._title,
                        TicketBase.Fields._price,
                        TicketBase.Fields._seat,
                        Ticket.Fields._arrived,
                        Model.Fields._timestamp
                },
                new String[]{
                        String.valueOf(_id),
                        String.valueOf(_secondaryId),
                        get_name(),
                        String.valueOf(get_price()),
                        get_seat(),
                        String.valueOf(get_arrived()),
                        String.valueOf(_timestamp)
                }
        );
    }

    @Getter @Setter
    @FieldNameConstants
    public static class DTO extends TicketBase.DTO {

        /**
         * Indicates the time that the participant has arrived to the event.
         * If it is null, the participant has not had his ticket scanned yet.
         */
        private LocalDateTime arrived;

        /**
         * The option that this ticket was created from.
         */
        private Ticket.Option.DTO option;

        /**
         * Converts into this DTO Object.
         * @param ticket The Object to be converted.
         */
        public DTO(Ticket ticket) {
            super(ticket);
            arrived = ticket.get_arrived();
            option = new Option.DTO(ticket.get_option());
        }
    }

    /**
     * The options for tickets that are available to be bought for an Event.
     */
    @Getter
    @FieldNameConstants
    public static class Option extends TicketBase {

        /**
         * The events that this is configured for.
         */
        private Seszt<UUID> _eventIds;

        /**
         * This venue is the owner of this option and can reuse them for events.
         */
        private UUID _venueId;

        /**
         * Will translate a transport object of this object into a construct of this object.
         * @param ticketOption The transport object to be transformed.
         */
        public Option(DTO ticketOption) {
            this(
                    ticketOption.getPrimaryId(),
                    ticketOption.getEventIds(),
                    ticketOption.getVenueId(),
                    ticketOption.getName(),
                    ticketOption.getSeat(),
                    ticketOption.getPrice(),
                    ticketOption.getValuta(),
                    ticketOption.getHistory(),
                    ticketOption.getTimestamp()
            );
        }

        /**
         * Constructor with all values.
         * @param id A hex decimal value identifying this item uniquely.
         * @param eventIds The events that this is configured for.
         * @param venueId The venue that is the owner of this option and can reuse them for events.
         * @param title A title describing this entity internally.
         * @param seat In case that this string is null, then it is a standing event.
         *             Otherwise, it is the seat number.
         * @param price The amount of money that the ticket costs.
         * @param valuta The type of valuta that the ticket is in.
         * @param history A collection of events that has occurred.
         * @param timestamp Specifies the time this entity was created.
         */
        public Option(
                UUID id,
                Seszt<UUID> eventIds,
                UUID venueId,
                String title,
                String seat,
                BigDecimal price,
                String valuta,
                History history,
                Instant timestamp
        ) {
            super(
                    id,
                    title,
                    seat,
                    price,
                    valuta,
                    history,
                    timestamp
            );
            _eventIds = eventIds;
            _venueId = venueId;
        }

        /**
         * For a new Ticket Option.
         * Sets the timestamp to now.
         * @param eventIds The events that this is configured for.
         * @param venueId The venue that is the owner of this option and can reuse them for events.
         * @param title A title describing this entity internally.
         * @param seat In case that this string is null, then it is a standing event.
         *             Otherwise, it is the seat number.
         * @param price The amount of money that the ticket costs.
         * @param valuta The type of valuta that the ticket is in.
         */
        public Option(
                Seszt<UUID> eventIds,
                UUID venueId,
                String title,
                String seat,
                BigDecimal price,
                String valuta
        ) {
            super(title, seat, price, valuta);
            _eventIds = eventIds;
            _venueId = venueId;
        }

        public Ticket toTicket(UUID participantId, UUID eventId) {
            return new Ticket(
                    participantId,
                    eventId,
                    get_name(),
                    get_seat(),
                    get_price(),
                    get_valuta(),
                    null,
                    this,
                    get_history(),
                    get_timestamp()
            );
        }

        @Override
        public String toString() {
            return defineToString(
                    getClass().getSimpleName(),
                    new String[]{
                            Model.Fields._id,
                            Model.Fields._title,
                            TicketBase.Fields._price,
                            TicketBase.Fields._seat,
                            Model.Fields._timestamp
                    },
                    new String[]{
                            String.valueOf(_id),
                            get_name(),
                            String.valueOf(get_price()),
                            get_seat(),
                            String.valueOf(_timestamp)
                    }
            );
        }

        /**
         * The Data Transfer Object.
         * Is meant to be used as having common fields and be the body of Requests and Responses.
         * Doesn't have any logic.
         */
        @Getter @Setter
        @FieldNameConstants
        public static class DTO extends TicketBase.DTO {

            /**
             * The events that this is configured for.
             */
            private Seszt<UUID> eventIds;

            /**
             * The venue that is the owner of this option and can reuse them for events.
             */
            private UUID venueId;

            /**
             * Converts into this DTO Object.
             * @param ticketOption The Object to be converted.
             */
            public DTO(Ticket.Option ticketOption) {
                super(ticketOption);
                eventIds = ticketOption.get_eventIds();
            }
        }
    }
}
