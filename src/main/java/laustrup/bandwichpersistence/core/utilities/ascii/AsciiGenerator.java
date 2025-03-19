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
        return build(
                Stream.of(
                 "/DDDDDDD   ",
                        "| DD   ADD ",
                        "| DD    DDD",
                        "| DD    DDD",
                        "| DD   DDD",
                        "| DDDDDDD  ",
                        "|_______/  "
                ),
                Stream.of(),
                uppercase
        );
    }

    @Override
    public Stream<String> w(boolean uppercase) {
        return build(
                Stream.of(
                 "/WWW  WW  W",
                        "| WW  WW  W",
                        "| WW  WW  W",
                        "| WW W  W W",
                        "|  WW    WW",
                        " A   /A   /",
                        "  A_/  A_/ "
                ),
                Stream.of(),
                uppercase
        );
    }

    @Override
    public Stream<String> i(boolean uppercase) {
        return build(
                Stream.of(
                 " __________",
                        "|  IIIIIIII",
                        "|__IIIIIIII",
                        "    |II    ",
                        " ___|II___ ",
                        "|  IIIIIIII",
                        "|__IIIIIIII"
                ),
                Stream.of(),
                uppercase
        );
    }

    @Override
    public Stream<String> c(boolean uppercase) {
        return build(
                Stream.of(
                 "  / CCCCC  ",
                        " / CC  CCC ",
                        "| CC       ",
                        "| CC       ",
                        "A  CC  CCC ",
                        " A  CCCCC /",
                        "  A______/ "
                ),
                Stream.of(),
                uppercase
        );
    }

    private Stream<String> build(Stream<String> uppercase, Stream<String> lowercase, boolean isUppercase) {
        return isUppercase ? uppercase : lowercase;
    }
}
