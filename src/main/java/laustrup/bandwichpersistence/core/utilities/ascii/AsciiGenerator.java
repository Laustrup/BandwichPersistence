package laustrup.bandwichpersistence.core.utilities.ascii;

import java.util.stream.Stream;

public class AsciiGenerator implements IAsciiGenerator {

    @Override
    public Stream<String> a(boolean uppercase) {
        return build(
                Stream.of(
                 " /AAAAAAA ",
                        "/ AA_   AA",
                        "| AA A  AA",
                        "| AAAAAAAA",
                        "| AA__  AA",
                        "| AA  | AA",
                        "|__/  |__/"
                ),
                Stream.of(),
                uppercase
        );
    }

    @Override
    public Stream<String> n(boolean uppercase) {
        return build(
                Stream.of(
                 "/NNN  /NNNN",
                        "| NNN |  NN",
                        "| NNNN|  NN",
                        "| NNANN  NN",
                        "| NN ANN NN",
                        "| NN  ANNNN",
                        "|__/   A__/"
                ),
                Stream.of(),
                uppercase
        );
    }

    @Override
    public Stream<String> d(boolean uppercase) {
        return Stream.empty();
    }

    @Override
    public Stream<String> w(boolean uppercase) {
        return Stream.empty();
    }

    @Override
    public Stream<String> i(boolean uppercase) {
        return Stream.empty();
    }

    @Override
    public Stream<String> c(boolean uppercase) {
        return Stream.empty();
    }

    @Override
    public Stream<String> h(boolean uppercase) {
        return Stream.empty();
    }

    private Stream<String> build(Stream<String> uppercase, Stream<String> lowercase, boolean isUppercase) {
        return isUppercase ? uppercase : lowercase;
    }
}
