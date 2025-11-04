package laustrup.bandwichpersistence.core.models.chats;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import laustrup.bandwichpersistence.core.models.Event;
import laustrup.bandwichpersistence.core.models.users.User;
import laustrup.bandwichpersistence.core.persistence.worm.annotations.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.UUID;

import static laustrup.bandwichpersistence.core.services.ModelService.toStringify;

/**
 * Determines if a User have approved to be a part of the Event.
 */
@Getter @FieldNameConstants @Table.Target
public class Request {

  /**
   * The User that needs to approve the Event.
   */
  @Table.Column(isPrimary = true)
  private final User.Id _receiverId;

  @Table.Column(isPrimary = true)
  private final User.Id _senderId;

  /**
   * The Event that has been requested for.
   */
  @Table.Column(isPrimary = true)
  private final Event _event;

  /**
   * The value that indicates if the request for the Event has been approved.
   * From the first date that isn't null, this has been approved.
   */
  @Setter @Table.Column("is_approved")
  private Instant _approved;

  /**
   * Will set the approved to now and therefore approve from now on.
   * In case that it is already approved, nothing will happen.
   */
  public void approve() {
    if (_approved == null)
      _approved = Instant.now();
  }

  /**
   * Will tell if the Request is approved, by whether the time is approved was null.
   * @return True if the approved is null.
   */
  public boolean isApproved() {
    return _approved != null;
  }

  /**
   * Will set the approved to null and therefore not approved.
   */
  public void deny() {
    _approved = null;
  }

  private final Instant _timestamp;

  /**
   * Will translate a transport object of this object into a construct of this object.
   * @param request The transport object to be transformed.
   */
  public Request(DTO request) {
    this(
        new User.Id(request.getReceiverId()),
        new User.Id(request.getSenderId()),
        new Event(request.getEvent()),
        request.getApproved(),
        request.getTimestamp()
    );
  }

  public Request(
      User.Id receiver,
      User.Id sender,
      Event event,
            Instant approved,
            Instant timestamp
    ) {
        if (receiver == null || event == null)
            throw new IllegalArgumentException("User and event are both required for Request with timestamp: " + timestamp);

        _receiverId = receiver;
        _senderId = sender;
        _event = event;
        _approved = approved;
        _timestamp = timestamp;
    }

    @Override
    public String toString() {
        return toStringify(this);
    }

    public Event.Id get_eventId() {
        return _event.get_identity();
    }

    /** Determines if a User have approved to be a part of the Event. */
    @Getter @FieldNameConstants
    public static class DTO {

        /** The User that needs to approve the Event. */
        private final UUID receiverId;

        private final UUID senderId;

        /** The Event that has been requested for. */
        private final Event.DTO event;

        /** The value that indicates if the request for the Event has been approved. */
        private final Instant approved;

        private final Instant timestamp;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public DTO(
                @JsonProperty UUID receiverId,
                @JsonProperty UUID senderId,
                @JsonProperty Event.DTO event,
                @JsonProperty Instant approved,
                @JsonProperty Instant timestamp
        ) {
            this.receiverId = receiverId;
            this.senderId = senderId;
            this.event = event;
            this.approved = approved;
            this.timestamp = timestamp;
        }

        /**
         * Converts into this DTO Object.
         * @param request The Object to be converted.
         */
        public DTO(Request request) {
            receiverId = request.get_receiverId().get_value();
            senderId = request.get_senderId().get_value();
            event = new Event.DTO(request.get_event());
            approved = request.get_approved();
            timestamp = request.get_timestamp();
        }
    }
}
