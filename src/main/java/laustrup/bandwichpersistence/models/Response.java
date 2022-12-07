package laustrup.bandwichpersistence.models;

import laustrup.bandwichpersistence.utilities.Printer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This object is meant for containing an element from backend to frontend.
 * It will also contain a message with a description of status and an error
 * boolean value, to determine whether the response is an error or not.
 * @param <E> The element that will be delivered to frontend.
 */
@ToString
public class Response<E> {

    /**
     * The element that is determined of the Response type.
     * It is the element that will be delivered to frontend.
     */
    @Getter
    private E _element;

    /**
     * An enum that is a status title of the situation.
     * Is used for determining the message.
     */
    @Getter
    private StatusType _status;

    /**
     * This will be printed to inform the enduser of the situation
     * or even guide it through it.
     */
    private String _message;

    /**
     * Will determine if it is an error or not.
     * Will be false if noting is set or the status is OK.
     */
    @Getter
    private boolean _error = false;

    public Response(E element) {
        _element = element;
        _status = StatusType.OK;
    }

    public Response(E element, StatusType status) {
        _element = element;
        _status = status;
        _message = set_message();
    }

    /**
     * Will set the message out of the status before getting it.
     * Also sets the error.
     * @return The described message.
     */
    public String get_message() {
        return set_message(_status);
    }

    /**
     * Sets the message depending on the status.
     * Also sets the error.
     * @return The described message.
     */
    public String set_message() {
        _message = new Status<E>(_status).describeMessageFor(_element);
        _error = set_error();
        return _message;
    }

    /**
     * Sets the message depending on the status.
     * The status will be set as well.
     * Also sets the error.
     * @param status An enum that describes the status of the Response.
     * @return The described message.
     */
    public String set_message(StatusType status) {
        _status = status;
        _error = set_error();
        _message = new Status<E>(_status).describeMessageFor(_element, _status);
        return _message;
    }

    /**
     * Will set the error from the status situation.
     * Will only become true, if status is OK.
     * @return The described error.
     */
    public boolean set_error() {
        _error = _status == StatusType.OK;
        return _error;
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
                    Printer.get_instance().print("No message to write in response...");
                    return new String();
                }
            }
        }
    }
}
