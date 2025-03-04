package laustrup.bandwichpersistence.core.models;

import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Contains the fundaments for tickets and their options available.
 */
@Getter
@FieldNameConstants
public abstract class TicketBase {

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

    protected Instant _timestamp;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param ticket The transport object to be transformed.
     */
    public TicketBase(TicketBase.DTO ticket) {
        this(
                ticket.getSeat(),
                ticket.getPrice(),
                ticket.getValuta(),
                ticket.getTimestamp()
        );
    }

    public TicketBase(
            String seat,
            BigDecimal price,
            String valuta,
            Instant timestamp
    ) {
        _seat = seat;
        _price = price;
        _valuta = valuta;
        _timestamp = timestamp;
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter
    @FieldNameConstants
    public abstract static class DTO {

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

        protected Instant timestamp;

        /**
         * Converts into this DTO Object.
         * @param ticket The Object to be converted.
         */
        public DTO(TicketBase ticket) {
            seat = ticket.get_seat();
            price = ticket.get_price();
            valuta = ticket.get_valuta();
            timestamp = ticket.get_timestamp();
        }
    }
}
