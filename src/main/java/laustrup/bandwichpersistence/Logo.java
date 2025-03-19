package laustrup.bandwichpersistence;

import laustrup.bandwichpersistence.core.utilities.ascii.AsciiArtist;
import laustrup.bandwichpersistence.core.utilities.collections.Collection;
import laustrup.bandwichpersistence.core.utilities.collections.lists.Liszt;

import java.util.Arrays;
import java.util.stream.Stream;

public class Logo implements ILogo {

    private final int
            _innerWidth = 16,
            _frameWidth = _innerWidth + 10,
            _letterHeight = 7;

    private final AsciiArtist _artist = new AsciiArtist();

    private final char
            _filling = ':',
            _framingEdge  = '+',
            _framingTop = '-',
            _wall = '|';

    private final Liszt<String> _frameColumnPart = new Liszt<>(Stream.of(String.format(
            "%s".repeat(3),
            _wall,
            String.valueOf(_filling).repeat(2),
            _wall
    )));

    private final Liszt<String> _frameOuterEdge = new Liszt<>(Stream.of(String.format(
            "%s".repeat(3),
            _framingEdge,
            String.valueOf(_framingTop).repeat(_frameWidth - 2),
            _framingEdge
    )));

    private final Liszt<String> _frameFilling = new Liszt<>(Stream.of(String.format(
            "%s".repeat(3),
            _wall,
            String.valueOf(_filling).repeat(_frameWidth - 2),
            _wall
    )));

    private final Stream<String> _frameInnerEdge = Stream.of(
            String.format(
                    "%s".repeat(5),
                    _wall,
                    String.valueOf(_filling).repeat(2),
                    String.valueOf(_framingTop).repeat(_innerWidth),
                    String.valueOf(_filling).repeat(2),
                    _wall
            )
    );

    private static Logo _instance;

    private Logo() {}

    public static Logo get_instance() {
        if (_instance == null)
            _instance = new Logo();

        return _instance;
    }

    @Override
    public void print() {
        framing(body()).forEach(System.out::println);
    }

    @Override
    public void print(Stream<String> additionalLines) {
        Stream.Builder<String> print = Stream.builder();
        Liszt<String> body = new Liszt<>(framing(body()));
        Liszt<String> lines = new Liszt<>(additionalLines);

        for (int i = 1; i <= body.size(); i++) {
            String bodyPart = body.Get(i);
            print.accept(bodyPart + (i <= lines.size() ? " " + lines.Get(i) : ""));
        }

        body.forEach(System.out::println);
    }

    public Stream<String> frameTop() {
        Stream.Builder<String> frameTop = Stream.builder();

        _frameOuterEdge.forEach(frameTop);
        _frameFilling.forEach(frameTop);
        _frameInnerEdge.forEach(frameTop);

        return frameTop.build();
    }

    public Stream<String> frameBottom() {
        Stream.Builder<String> frameTop = Stream.builder();

        _frameInnerEdge.forEach(frameTop);
        _frameFilling.forEach(frameTop);
        _frameOuterEdge.forEach(frameTop);

        return frameTop.build();
    }

    public Stream<String> framing(Stream<String> lines) {
        Stream.Builder<String> builder = Stream.builder();

        new Liszt<>(frameTop()).forEach(builder);
        lines.map(line -> String.format(
                "%s%s%s",
                _frameColumnPart,
                line,
                _frameColumnPart
        )).forEach(builder);
        new Liszt<>(frameBottom()).forEach(builder);

        return builder.build();
    }

    @Override
    public Stream<String> body() {
        Stream.Builder<String> builder = Stream.builder();

        bBun().forEach(builder);
        and().forEach(builder);
        wic().forEach(builder);
        hButton().forEach(builder);

        return builder.build();
    }

    public Stream<String> and() {
        return putLettersInRow(
                Liszt.of(Stream.of(_artist.a(true))),
                Liszt.of(Stream.of(_artist.n(true))),
                Liszt.of(Stream.of(_artist.d(true)))
        );
    }

    public Stream<String> wic() {
        return putLettersInRow(
                Liszt.of(Stream.of(_artist.w(true))),
                Liszt.of(Stream.of(_artist.i(true))),
                Liszt.of(Stream.of(_artist.c(true)))
        );
    }

    private Stream<String> putLettersInRow(Collection<Stream<String>>... letters) {
        Stream.Builder<String> builder = Stream.builder();

        for (int i = 0; i < _letterHeight; i++) {
            int index = i;
            builder.accept(
                    String.format(
                            "%s".repeat(letters.length - 1),
                            Arrays.stream(letters).map(letter -> letter.get_data()[index])
                    )
            );
        }

        return builder.build();
    }

    public Stream<String> bBun() {
        return Stream.of(
        "    ___    ___  ",
                "  /;::B__B:::B  ",
                " /;:;_:::;_:::B ",
                "(;::| B::| B:::B",
                "|::::BB:::BB:::B",
                "|::::::::::::::B",
                "+-------------' "
        );
    }

    public Stream<String> hButton() {
        return Stream.of(
         "+HHHHHHHHHHHHHHH",
                "|::::::::::::::H",
                "+-----+::+-----H",
                "      |::H      ",
                "+-----+::HHHHHHH",
                "|::::::::::::::H",
                "+-------------' "
        );
    }
}
