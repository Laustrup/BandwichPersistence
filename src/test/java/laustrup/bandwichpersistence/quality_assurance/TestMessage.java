package laustrup.bandwichpersistence.quality_assurance;

import lombok.Getter;

/** Optional values of indicating an issue of an occurrence in a test. */
public enum TestMessage {
    SUCCESS("SUCCESS!"),
    FAILURE("FAILURE!"),
    WRONG_ARRANGEMENT("The arrangement was not as expected..."),
    WRONG_ACT("The arrangement was not as expected...");

    /** The value as a message for the specific enum. */
    @Getter
    private final String _content;
    TestMessage(String content) { _content = content; }
}
