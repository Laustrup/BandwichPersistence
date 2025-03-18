package laustrup.bandwichpersistence.core.utilities.ansi;

import java.util.stream.Stream;

public interface IAnsiGenerator {

    Stream<String> a(boolean uppercase);
    Stream<String> n(boolean uppercase);
    Stream<String> d(boolean uppercase);
    Stream<String> w(boolean uppercase);
    Stream<String> i(boolean uppercase);
    Stream<String> c(boolean uppercase);
    Stream<String> h(boolean uppercase);

}
