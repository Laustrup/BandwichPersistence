package laustrup.bandwichpersistence.core.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Contains the fundaments for tickets and their options available.
 */
@Getter @Setter
@FieldNameConstants
public abstract class TicketBase extends Model {

    /**
     * In case that this string is null, then it is a standing event.
     * Otherwise, it is the seat number.
     */
    protected String _seat;

    /**
     * The amount of money that the ticket costs.
     */
    protected BigDecimal _price;

    /**
     * The type of valuta that the ticket is in.
     */
    protected String _valuta;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param ticket The transport object to be transformed.
     */
    public TicketBase(TicketBase.DTO ticket) {
        this(
                ticket.getPrimaryId(),
                ticket.getName(),
                ticket.getSeat(),
                ticket.getPrice(),
                ticket.getValuta(),
                ticket.getHistory(),
                ticket.getTimestamp()
        );
    }

    /**
     * Constructor with all values.
     * @param id A hex decimal value identifying this item uniquely.
     * @param title A title describing this entity internally.
     * @param seat In case that this string is null, then it is a standing event.
     *             Otherwise, it is the seat number.
     * @param price The amount of money that the ticket costs.
     * @param valuta The type of valuta that the ticket is in.
     * @param history A collection of events that has occurred.
     * @param timestamp Specifies the time this entity was created.
     */
    public TicketBase(
            UUID id,
            String title,
            String seat,
            BigDecimal price,
            String valuta,
            History history,
            Instant timestamp
    ) {
        super(id, title, history, timestamp);
        _seat = seat;
        _price = price;
        _valuta = valuta;
    }

    /**
     * Constructor with all values.
     * Is meant to be for a bought ticket, hence the two ids.
     * @param participantId A hex decimal value identifying this item uniquely as the participant.
     * @param eventId A hex decimal value identifying this item uniquely as the event.
     * @param title A title describing this entity internally.
     * @param seat In case that this string is null, then it is a standing event.
     *             Otherwise, it is the seat number.
     * @param price The amount of money that the ticket costs.
     * @param valuta The type of valuta that the ticket is in.
     * @param history A collection of events that has occurred.
     * @param timestamp Specifies the time this entity was created.
     */
    public TicketBase(
            UUID participantId,
            UUID eventId,
            String title,
            String seat,
            BigDecimal price,
            String valuta,
            History history,
            Instant timestamp
    ) {
        super(participantId, eventId, title, history, timestamp);
        _seat = seat;
        _price = price;
        _valuta = valuta;
    }
    
    /**
     * For a new Ticket Option.
     * Sets the timestamp to now.
     * @param title A title describing this entity internally.
     * @param seat In case that this string is null, then it is a standing event.
     *             Otherwise, it is the seat number.
     * @param price The amount of money that the ticket costs.
     * @param valuta The type of valuta that the ticket is in.
     */
    public TicketBase(String title, String seat, BigDecimal price, String valuta) {
        super(title);
        _seat = seat;
        _price = price;
        _valuta = valuta;
    }


    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter
    @FieldNameConstants
    public abstract static class DTO extends ModelDTO {

        /**
         * Is it a ticket for sitting or standing?
         */
        protected String seat;

        /**
         * The amount of money that the ticket costs.
         */
        protected BigDecimal price;

        /**
         * The type of valuta that the ticket is in.
         */
        protected String valuta;

        /**
         * Converts into this DTO Object.
         * @param ticket The Object to be converted.
         */
        public DTO(TicketBase ticket) {
            super(ticket);
            seat = ticket.get_seat();
            price = ticket.get_price();
            valuta = ticket.get_valuta();
        }
    }
}
