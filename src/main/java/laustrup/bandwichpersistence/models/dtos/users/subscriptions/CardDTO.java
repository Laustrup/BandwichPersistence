package laustrup.bandwichpersistence.models.dtos.users.subscriptions;

import laustrup.bandwichpersistence.models.users.subscriptions.Card;
import lombok.*;

/**
 * Contains different information about credit cards, that are needed for curtain subscriptions.
 */
@NoArgsConstructor @Data
public class CardDTO {

    /**
     * This id is the id from the database.
     * Subscription only contains the id of this card for security reasons.
     */
    private long id;

    /**
     * An enum of different credit card providers or brands.
     */
    private Type type;

    /**
     * The owner of the credit card.
     */
    private String owner;

    /**
     * This pin code is the password information to the card.
     * Must be a curtain amount of numbers.
     */
    private long cardNumbers;

    /**
     * The month that the card will expire.
     * Only two digits allowed.
     */
    private int expirationMonth;

    /**
     * The year that the card will expire.
     * Only two digits allowed.
     */
    private int expirationYear;

    /**
     * The three digits on the backside of the credit card.
     */
    private int cVV;

    public CardDTO(Card card) {
        id = card.get_id();
        type = Type.valueOf(card.get_type().toString());
        owner = card.get_owner();
        cardNumbers = card.get_cardNumbers();
        expirationMonth = card.get_expirationMonth();
        expirationYear = card.get_expirationYear();
        cVV = card.get_cVV();
    }

    /**
     * Are a set of enums with values of different credit card providers.
     */
    public enum Type {
        VISA,
        AMERICAN_EXPRESS,
        DANCARD,
    }
}
