package laustrup.bandwichpersistence.quality_assurance;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QualityAssuranceTests extends Tester<Object> {

    @Test @SuppressWarnings("all")
    void failsOnException() {
        assertThrows(Exception.class, () -> {
            test(() -> {
                arrange();
                Integer.parseInt("String");
            });
            test(() -> {
                arrange();
                Integer.parseInt("String");
                return TestMessage.SUCCESS.get_content();
            });
        });
    }

    @Test
    void canAssert() {
        test(() -> {
            try {
                arrange();
                act(() -> assertTrue(true));
            } catch (Exception e) {
                fail("Exception caught in asserting...", e);
            }
        });
    }
}
