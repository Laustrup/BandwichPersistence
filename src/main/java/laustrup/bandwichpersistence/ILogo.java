package laustrup.bandwichpersistence;

import java.util.stream.Stream;

public interface ILogo {

    Stream<String> framing(Stream<String> lines);

    Stream<String> body();

    void print();

    void print(Stream<String> additionalLines);
}
