package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static laustrup.bandwichpersistence.core.services.ModelService.defineToString;
import static laustrup.bandwichpersistence.core.utilities.collections.sets.Seszt.copy;

/**
 * The ticket that have been bought for an event from an option of the event.
 */
@Getter
@FieldNameConstants
public class Ticket extends TicketBase {

    private UUID _userId;

    private UUID _eventId;

    private String _seat;

    /**
     * Indicates the time that the participant has arrived to the event.
     * If it is null, the participant has not had his ticket scanned yet.
     */
    @Setter
    private LocalDateTime _arrived;

    /**
     * The option that this ticket was created from.
     */
    private UUID _optionId;

    /**
     * Converts a Data Transport Object into this object.
     * @param ticket The Data Transport Object that will be converted.
     */
    public Ticket(DTO ticket) {
        this(
                ticket.getUserId(),
                ticket.getEventId(),
                ticket.getSeat(),
                ticket.getPrice(),
                ticket.getValuta(),
                ticket.getArrived(),
                ticket.isSitting(),
                copy(ticket.getAreas(), area -> area),
                ticket.getOptionId(),
                ticket.getTimestamp()
        );
    }

    public Ticket(
            UUID userId,
            UUID eventId,
            String seat,
            BigDecimal price,
            String valuta,
            LocalDateTime arrived,
            boolean isSitting,
            Seszt<String> areas,
            UUID optionId,
            Instant timestamp
    ) {
        super(
                price,
                valuta,
                isSitting,
                areas,
                timestamp
        );
        _seat = seat;
        _userId = userId;
        _eventId = eventId;
        _arrived = arrived;
        _optionId = optionId;
    }

    @Override
    public String toString() {
        return defineToString(
                getClass().getSimpleName(),
                get_userId(),
                get_eventId(),
                new String[]{
                        "userId",
                        "eventId",
                        TicketBase.Fields._price,
                        Ticket.Fields._arrived,
                        Model.Fields._timestamp
                },
                new String[]{
                        String.valueOf(_userId),
                        String.valueOf(_eventId),
                        String.valueOf(get_price()),
                        String.valueOf(get_arrived()),
                        String.valueOf(_timestamp)
                }
        );
    }

    @Getter @Setter
    @FieldNameConstants
    public static class DTO extends TicketBase.DTO {

        private UUID userId;

        private UUID eventId;

        private String seat;

        /**
         * Indicates the time that the participant has arrived to the event.
         * If it is null, the participant has not had his ticket scanned yet.
         */
        private LocalDateTime arrived;

        /**
         * The option that this ticket was created from.
         */
        private UUID optionId;

        /**
         * Converts into this DTO Object.
         * @param ticket The Object to be converted.
         */
        public DTO(Ticket ticket) {
            super(ticket);
            seat = ticket.get_seat();
            userId = ticket.get_userId();
            eventId = ticket.get_eventId();
            arrived = ticket.get_arrived();
            optionId = ticket.get_optionId();
        }
    }

    /**
     * The options for tickets that are available to be bought for an Event.
     */
    @Getter
    @FieldNameConstants
    public static class Option extends TicketBase {

        private UUID _id;

        private UUID _eventId;

        private String _title;

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
                    ticketOption.getId(),
                    ticketOption.getEventId(),
                    ticketOption.getVenueId(),
                    ticketOption.getTitle(),
                    ticketOption.getPrice(),
                    ticketOption.getValuta(),
                    ticketOption.isSitting(),
                    copy(ticketOption.getAreas(),area -> area),
                    ticketOption.getTimestamp()
            );
        }

        public Option(
                UUID id,
                UUID eventId,
                UUID venueId,
                String title,
                BigDecimal price,
                String valuta,
                boolean sitting,
                Seszt<String> areas,
                Instant timestamp
        ) {
            super(
                    price,
                    valuta,
                    sitting,
                    areas,
                    timestamp
            );
            _id = id;
            _eventId = eventId;
            _venueId = venueId;
            _title = title;
        }

        public Ticket toTicket(UUID userId, String seat) {
            return new Ticket(
                    userId,
                    get_eventId(),
                    seat,
                    get_price(),
                    get_valuta(),
                    null,
                    is_sitting(),
                    get_areas(),
                    get_id(),
                    get_timestamp()
            );
        }

        @Override
        public String toString() {
            return defineToString(
                    getClass().getSimpleName(),
                    get_id(),
                    new String[]{
                            Model.Fields._id,
                            Model.Fields._title,
                            TicketBase.Fields._price,
                            Model.Fields._timestamp
                    },
                    new String[]{
                            String.valueOf(_id),
                            get_title(),
                            String.valueOf(get_price()),
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

            private UUID id;

            /**
             * The events that this is configured for.
             */
            private UUID eventId;

            /**
             * The venue that is the owner of this option and can reuse them for events.
             */
            private UUID venueId;


            private String title;

            /**
             * Converts into this DTO Object.
             * @param ticketOption The Object to be converted.
             */
            public DTO(Ticket.Option ticketOption) {
                super(ticketOption);
                eventId = ticketOption.get_eventId();
                venueId = ticketOption.get_venueId();
                title = ticketOption.get_title();
            }
        }

        @Getter
        public static class Template extends TicketBase {

            /**
             * The events that this is configured for.
             */
            private Seszt<UUID> _eventIds;

            private String _title;

            public Template(DTO template) {
                this(
                        copy(template.getEventIds(), id -> id),
                        template.getTitle(),
                        template.getPrice(),
                        template.getValuta(),
                        template.isSitting(),
                        copy(template.getAreas(), id -> id),
                        template.getTimestamp()
                );
            }

            public Template(
                    Seszt<UUID> eventIds,
                    String title,
                    BigDecimal price,
                    String valuta,
                    boolean isSitting,
                    Seszt<String> areas,
                    Instant timestamp
            ) {
                super(price, valuta, isSitting, areas, timestamp);
                _eventIds = eventIds;
                _title = title;
            }

            @Getter
            public static class DTO extends TicketBase.DTO {

                private Set<UUID> eventIds;

                private String title;

                public DTO(Template template) {
                    super(template);
                    eventIds = template.get_eventIds().asSet();
                    title = template.get_title();
                }
            }
        }
    }
}
