package laustrup.bandwichpersistence.core.services;

import laustrup.bandwichpersistence.BandwichTester;
import org.junit.jupiter.api.Test;

import static laustrup.bandwichpersistence.core.services.PasswordService.matches;
import static laustrup.bandwichpersistence.quality_assurance.Asserter.asserting;

class PasswordServiceTests extends BandwichTester {

    @Test
    void canMatch() {
        test(() -> {
            Password password = arrange(new Password("password", "$2y$05$Ad01b.9//2.NKwsL/6y/HeWk3TgdMDve4ThnFPKt.5FMfP2GHbRke£d01"));

            boolean actual = act(() -> matches(password.plainText, password.gibberish));

            asserting(actual)
                    .isTrue();
        });
    }

    private record Password(String plainText, String gibberish) {}
}