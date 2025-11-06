package laustrup.bandwichpersistence.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import laustrup.bandwichpersistence.core.models.identification.CommonIdentity;
import laustrup.bandwichpersistence.core.models.identification.Identity;
import laustrup.bandwichpersistence.core.models.identification.Signature;
import laustrup.bandwichpersistence.core.models.users.User;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import laustrup.bandwichpersistence.core.utilities.collections.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

import static laustrup.bandwichpersistence.core.services.ModelService.toStringify;
import static laustrup.bandwichpersistence.core.utilities.collections.Seszt.copy;

/**
 * The ticket that have been bought for an event from an option of the event.
 */
@Getter
@FieldNameConstants
@Table
public class Ticket extends TicketBase {

  private final User.Id _userId;

  private final Event.Id _eventId;

  private final String _seat;

  /**
   * Indicates the time that the participant has arrived to the event.
   * If it is null, the participant has not had his ticket scanned yet.
   */
  @Setter
  @Table.Column("is_arrived")
  private LocalDateTime _arrived;

  /**
   * The option that this ticket was created from.
   */
  private final Identity<Signature.UUID> _optionId;

  /**
   * Converts a Data Transport Object into this object.
   *
   * @param ticket The Data Transport Object that will be converted.
   */
  public Ticket(DTO ticket) {
    this(
        new User.Id(ticket.getUserId()),
        new Event.Id(ticket.getEventId()),
        ticket.getSeat(),
        ticket.getPrice(),
        ticket.getValuta(),
        ticket.getArrived(),
        ticket.isSitting(),
        copy(ticket.getAreas(), area -> area),
        new Option.Id(ticket.getOptionId()),
        ticket.getTimestamp()
    );
  }

  @Table.Constructor
  public Ticket(
      User.Id userId,
      Event.Id eventId,
      String seat,
      BigDecimal price,
      String valuta,
      LocalDateTime arrived,
      boolean isSitting,
      Seszt<String> areas,
      Identity<Signature.UUID> optionId,
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
    return toStringify(this);
  }

  @Getter
  @FieldNameConstants
  public static class DTO extends TicketBase.DTO {

    private final java.util.UUID userId;

    private final java.util.UUID eventId;

    private final String seat;

    /**
     * Indicates the time that the participant has arrived to the event.
     * If it is null, the participant has not had his ticket scanned yet.
     */
    private final LocalDateTime arrived;

    /**
     * The option that this ticket was created from.
     */
    private final java.util.UUID optionId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DTO(
        @JsonProperty BigDecimal price,
        @JsonProperty String valuta,
        @JsonProperty boolean isSitting,
        @JsonProperty Set<String> areas,
        @JsonProperty Instant timestamp,
        @JsonProperty java.util.UUID userId,
        @JsonProperty java.util.UUID eventId,
        @JsonProperty String seat,
        @JsonProperty LocalDateTime arrived,
        @JsonProperty java.util.UUID optionId
    ) {
      super(price, valuta, isSitting, areas, timestamp);
      this.userId = userId;
      this.eventId = eventId;
      this.seat = seat;
      this.arrived = arrived;
      this.optionId = optionId;
    }

    /**
     * Converts into this DTO Object.
     *
     * @param ticket The Object to be converted.
     */
    public DTO(Ticket ticket) {
      super(ticket);
      seat = ticket.get_seat();
      userId = ticket.get_userId().get_value();
      eventId = ticket.get_eventId().get_value();
      arrived = ticket.get_arrived();
      optionId = ticket.get_optionId().get_value();
    }
  }

  /**
   * The options for tickets that are available to be bought for an Event.
   */
  @Getter
  @FieldNameConstants
  @Table("ticket_options")
  public static class Option extends TicketBase {

    private final Id _id;

    private final Event.Id _eventId;

    private final String _title;

    /**
     * This venue is the owner of this option and can reuse them for events.
     */
    private final Venue.Id _venueId;

    /**
     * Will translate a transport object of this object into a construct of this object.
     *
     * @param ticketOption The transport object to be transformed.
     */
    public Option(DTO ticketOption) {
      this(
          new Id(ticketOption.getId()),
          new Event.Id(ticketOption.getEventId()),
          new Venue.Id(ticketOption.getVenueId()),
          ticketOption.getTitle(),
          ticketOption.getPrice(),
          ticketOption.getValuta(),
          ticketOption.isSitting(),
          copy(ticketOption.getAreas(), area -> area),
          ticketOption.getTimestamp()
      );
    }

    public Option(
        Id id,
        Event.Id eventId,
        Venue.Id venueId,
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

    public Ticket toTicket(User.Id userId, String seat) {
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

    public static class Id extends CommonIdentity<Signature.UUID> {

      public Id(Signature.UUID signature) {
        super(signature);
      }

      public Id(java.util.UUID signature) {
        super(new Signature.UUID(signature));
      }

      @Override
      public Class<Option> getOwnerClassType() {
        return Option.class;
      }
    }

    @Override
    public String toString() {
      return toStringify(this);
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter
    @Setter
    @FieldNameConstants
    public static class DTO extends TicketBase.DTO {

      private java.util.UUID id;

      /**
       * The events that this is configured for.
       */
      private java.util.UUID eventId;

      /**
       * The venue that is the owner of this option and can reuse them for events.
       */
      private java.util.UUID venueId;

      private String title;

      @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
      public DTO(
          @JsonProperty BigDecimal price,
          @JsonProperty String valuta,
          @JsonProperty boolean isSitting,
          @JsonProperty Seszt<String> areas,
          @JsonProperty java.util.UUID id,
          @JsonProperty java.util.UUID eventId,
          @JsonProperty java.util.UUID venueId,
          @JsonProperty String title,
          @JsonProperty Instant timestamp
      ) {
        super(
            price,
            valuta,
            isSitting,
            areas,
            timestamp
        );
        this.id = id;
        this.eventId = eventId;
        this.venueId = venueId;
        this.title = title;
      }

      /**
       * Converts into this DTO Object.
       *
       * @param ticketOption The Object to be converted.
       */
      public DTO(Ticket.Option ticketOption) {
        super(ticketOption);
        eventId = ticketOption.get_eventId().get_value();
        venueId = ticketOption.get_venueId().get_value();
        title = ticketOption.get_title();
      }
    }

    @Getter
    @Table("ticket_option_templates")
    public static class Template extends TicketBase {

      /**
       * The events that this is configured for.
       */
      private final Seszt<Event.Id> _eventIds;

      private final String _title;

      public Template(DTO template) {
        this(
            copy(template.getEventIds(), Event.Id::new),
            template.getTitle(),
            template.getPrice(),
            template.getValuta(),
            template.isSitting(),
            copy(template.getAreas(), id -> id),
            template.getTimestamp()
        );
      }

      public Template(
          Seszt<Event.Id> eventIds,
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

        private final Set<java.util.UUID> eventIds;

        private final String title;

        public DTO(Template template) {
          super(template);
          eventIds = template.get_eventIds().asSet(Identity::get_value);
          title = template.get_title();
        }
      }
    }
  }
}
