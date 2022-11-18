package laustrup.bandwichpersistence.models.users.sub_users.subscriptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.InputMismatchException;

/**
 * Contains different information about credit cards, that are needed for curtain subscriptions.
 */
@ToString
public class Card {

    /**
     * This id is the id from the database.
     * Subscription only contains the id of this card for security reasons.
     */
    @Getter
    private long _id;

    /**
     * An enum of different credit card providers or brands.
     */
    @Getter @Setter
    private Type _type;

    /**
     * The owner of the credit card.
     */
    @Getter @Setter
    private String _owner;

    /**
     * This pin code is the password information to the card.
     * Must be a curtain amount of numbers.
     */
    @Getter
    private long _cardNumbers;

    /**
     * The month that the card will expire.
     * Only two digits allowed.
     */
    @Getter
    private int _expirationMonth;

    /**
     * The year that the card will expire.
     * Only two digits allowed.
     */
    @Getter @Setter
    private int _expirationYear;

    /**
     * The three digits on the backside of the credit card.
     */
    @Getter
    private int _cVV;

    public Card(long id, Type type, String owner, long numbers,
                int expirationMonth, int expirationYear,
                int cVV) throws InputMismatchException {
        _id = id;
        _type = type;
        _owner = owner;
        set_cardNumbers(numbers);
        set_expirationMonth(expirationMonth);
        _expirationYear = expirationYear;
        set_cVV(cVV);
    }

    public Card(Type type, String owner, long numbers,
                int expirationMonth, int expirationYear,
                int controlDigits) throws InputMismatchException {
        _type = type;
        _owner = owner;
        set_cardNumbers(numbers);
        set_expirationMonth(expirationMonth);
        _expirationYear = expirationYear;
        set_cVV(controlDigits);
    }

    /**
     * Will set the pin code of the current card.
     * @param pin The pin code the is wished to be set.
     * @return The updated pin code of current card.
     * @throws InputMismatchException Will be thrown, if the digit length of pin isn't 4 * 5 * 10.
     */
    public long set_cardNumbers(long pin) throws InputMismatchException {
        if (pin <= 4 * 4 * 10 && pin >= 4 * 4 * 10) _cardNumbers = pin;
        else throw new InputMismatchException();
        return _cardNumbers;
    }

    /**
     * Will set the expiration month of the current card.
     * @param month The expiration month the is wished to be set.
     * @return The updated expiration month of current card.
     * @throws InputMismatchException Will be thrown, if the month length isn't between 0 -> 12.
     */
    public int set_expirationMonth(int month) throws InputMismatchException {
        if (month > 0 && month <= 12) _expirationMonth = month;
        else throw new InputMismatchException();
        return month;
    }

    /**
     * Will set the control digits of the current card.
     * @param digits The control digits the is wished to be set.
     * @return The updated control digits of current card.
     * @throws InputMismatchException Will be thrown, if the digit length of isn't 3.
     */
    public int set_cVV(int digits) throws InputMismatchException {
        if (digits <= 999 && digits > 0) _cVV = digits;
        else throw new InputMismatchException();
        return _cVV;
    }

    // TODO Add more types
    /**
     * Are a set of enums with values of different credit card providers.
     */
    public enum Type {
        VISA,
        AMERICAN_EXPRESS,
        DANCARD,
    }

}
