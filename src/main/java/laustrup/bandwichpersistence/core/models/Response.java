package laustrup.bandwichpersistence.core.models;

import laustrup.bandwichpersistence.core.utilities.console.Printer;
import lombok.*;
import lombok.experimental.FieldNameConstants;

/**
 * This object is meant for containing an element from backend to frontend.
 * It will also contain a message with a description of status and an error
 * boolean value, to determine whether the response is an error or not.
 * @param <E> The element that will be delivered to frontend.
 */
@Getter @Setter
@FieldNameConstants
public class Response<E> {

    /**
     * The element that is determined of the Response type.
     * It is the element that will be delivered to frontend.
     */
    private E element;

    /**
     * An enum that is a status title of the response.
     * Is used for determining the message.
     * Difference from Situation is that this is about the Response itself.
     */
    private StatusType status;

    /**
     * Simply said, this is used to describe a relevant situation.
     * Useful to identify an incident or change.
     * Difference from status is that this is about the body of the Response.
     */
    private String situation;

    /**
     * This will be printed to inform the enduser of the situation
     * or even guide it through it.
     */
    private String message;

    /**
     * Will determine if it is an error or not.
     * Will be false if noting is set or the status is OK.
     */
    private boolean error = false;

    /**
     * Sets the status to OK.
     * @param element The element which is considered the body of the response.
     * @param situation The situation of the body.
     */
    public Response(E element, Situation situation) {
        this.element = element;
        this.situation = situation.get_message();
        status = StatusType.OK;
    }

    /**
     * Will also set the message according to the status and element.
     * @param element The element which is considered the body of the response.
     * @param situation The situation of the body.
     * @param status The status for the Response.
     */
    public Response(E element, Situation situation, StatusType status) {
        this.element = element;
        this.situation = situation.get_message();
        this.status = status;
        message = setMessage();
    }

    /**
     * Will set the message out of the status before getting it.
     * Also sets the error.
     * @return The described message.
     */
    public String getMessage() {
        return setMessage(status);
    }

    /**
     * Sets the message depending on the status.
     * Also sets the error.
     * @return The described message.
     */
    public String setMessage() {
        message = new Status<E>(status).describeMessageFor(element);
        error = setError();
        return message;
    }

    /**
     * Sets the message depending on the status.
     * The status will be set as well.
     * Also sets the error.
     * @param status An enum that describes the status of the Response.
     * @return The described message.
     */
    public String setMessage(StatusType status) {
        this.status = status;
        error = setError();
        message = new Status<E>(this.status).describeMessageFor(element, this.status);
        return message;
    }

    /**
     * Will set the error from the status situation.
     * Will only become true, if status is OK.
     * @return The described error.
     */
    public boolean setError() {
        error = status == StatusType.OK;
        return error;
    }

    /**
     * Contains different status titles.
     * Is meant to describe the message and error of the Response.
     */
    public enum StatusType {
        OK,
        UNKNOWN,
        NO_CONTENT,
        NOT_ACCEPTABLE,
        WRONG_PASSWORD,
        INVALID_PASSWORD_FORMAT
    }

    /**
     * A private class of Response, that will describe the message from the status.
     * @param <E> The element type of the Response.
     */
    @NoArgsConstructor @ToString
    private class Status<E> {

        /**
         * The current status type of the Response.
         */
        @Getter @Setter
        private StatusType _type;

        public Status(StatusType statusType) {
            _type = statusType;
        }

        /**
         * Describes a message depending on the status type.
         * @param element This element is being used to have values included in the message.
         * @param type The type of the Response status.
         * @return The described message. If switch default is reached or status is OK,
         *         it will return an empty.
         */
        public String describeMessageFor(E element, StatusType type) {
            _type = type;
            return _type == StatusType.OK ? new String() : describeMessage(element);
        }

        /**
         * Describes a message depending on the status type.
         * @param element This element is being used to have values included in the message.
         * @return The described message. If switch default is reached or status is OK,
         *         it will return an empty.
         */
        public String describeMessageFor(E element) {
            return _type == StatusType.OK ? new String() : describeMessage(element);
        }

        /**
         * Uses a switch case to describe a message.
         * @param element This element is being used to have values included in the message.
         * @return The described message. If switch default is reached or status is OK,
         *         it will return an empty.
         */
        private String describeMessage(E element) {
            switch (_type) {
                case NO_CONTENT -> {
                    return "There wasn't found any matching element...";
                }
                case NOT_ACCEPTABLE -> {
                    return "That action is not allowed...";
                }
                case WRONG_PASSWORD -> {
                    return "Password is wrong...";
                }
                case INVALID_PASSWORD_FORMAT -> {
                    return "Password is not allowed... Please check the requirements.";
                }
                case UNKNOWN -> {
                    return "Unknown issue for response...";
                }
                default -> {
                    Printer.print("No message to write in response...");
                    return new String();
                }
            }
        }
    }
}
