package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import static laustrup.bandwichpersistence.core.utilities.collections.Seszt.copy;

/**
 * Contains the fundaments for tickets and their options available.
 */
@Getter
@FieldNameConstants
public abstract class TicketBase {

  /**
   * The amount of money that the ticket costs.
   */
  protected BigDecimal _price;

  /**
   * The type of valuta that the ticket is in.
   */
  protected String _valuta;

  @Table.Column("is_sitting")
  private final boolean _sitting;

  private final Seszt<String> _areas;

  protected Instant _timestamp;

  /**
   * Will translate a transport object of this object into a construct of this object.
   *
   * @param ticket The transport object to be transformed.
   */
  public TicketBase(TicketBase.DTO ticket) {
    this(
        ticket.getPrice(),
        ticket.getValuta(),
        ticket.isSitting(),
        copy(ticket.getAreas(), area -> area),
        ticket.getTimestamp()
    );
  }

  public TicketBase(
      BigDecimal price,
      String valuta,
      boolean isSitting,
      Seszt<String> areas,
      Instant timestamp
  ) {
    _price = price;
    _valuta = valuta;
    _sitting = isSitting;
    _areas = areas;
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
     * The amount of money that the ticket costs.
     */
    protected BigDecimal price;

    /**
     * The type of valuta that the ticket is in.
     */
    protected String valuta;

    private final boolean isSitting;

    private final Set<String> areas;

    protected Instant timestamp;

    public DTO(
        BigDecimal price,
        String valuta,
        boolean isSitting,
        Set<String> areas,
        Instant timestamp
    ) {
      this.price = price;
      this.valuta = valuta;
      this.isSitting = isSitting;
      this.areas = areas;
      this.timestamp = timestamp;
    }

    /**
     * Converts into this DTO Object.
     *
     * @param ticket The Object to be converted.
     */
    public DTO(TicketBase ticket) {
      this(
          ticket.get_price(),
          ticket.get_valuta(),
          ticket.is_sitting(),
          ticket.get_areas().asSet(),
          ticket.get_timestamp()
      );
    }
  }
}
