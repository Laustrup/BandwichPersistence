package laustrup.bandwichpersistence.items.aaa.assertions;

import lombok.Getter;

public enum AssertionMessage {
    SUCCESS("SUCCESS!"),
    ASSERTION_ERROR("There was a error in asserting..."),
    NOT_APPLIED("The statement for applying assertion function is false..."),
    IS_NULL("Expected and/or actual is null..."),
    LENGTH_IS_DIFFERENT("Expected and/or actual length is not the same...");

    @Getter
    private String _content;
    AssertionMessage(String content) { _content = content; }
}
