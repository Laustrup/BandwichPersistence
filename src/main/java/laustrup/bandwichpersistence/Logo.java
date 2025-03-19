package laustrup.bandwichpersistence;

import laustrup.bandwichpersistence.core.utilities.ascii.AsciiArtist;
import laustrup.bandwichpersistence.core.utilities.collections.lists.Liszt;
import laustrup.bandwichpersistence.core.utilities.collections.Collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Logo implements ILogo {

    private final int
            _innerWidth = 16,
            _frameWidth = _innerWidth + 10,
            _letterHeight = 7,
            _singleLetterWidth = 11,
            _letterRowWidth = _singleLetterWidth * 3,
            _letterRowHeight = 7;

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

    private final Liszt<String> _frameInnerEdge = Liszt.of(
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
        List<String> body = new ArrayList<>(framing(body()).toList());
        List<List<String>> lines = Stream.of(additionalLines).map(Stream::toList).toList();

//        for (int i = 0; i <= body.size(); i++) {
//            String bodyPart = body.get(i);
//            print.accept(bodyPart + (i <= lines.size() - 1 ? " " + lines.get(i) : ""));
//        }

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
//        and().forEach(builder);
//        wic().forEach(builder);
        hButton().forEach(builder);

        return builder.build();
    }

//    public Stream<String> and() {
//        return putLettersInRow(
//                Liszt.of(Stream.of(_artist.a(true))),
//                Liszt.of(Stream.of(_artist.n(true))),
//                Liszt.of(Stream.of(_artist.d(true)))
//        );
//    }
//
//    public Stream<String> wic() {
//        return putLettersInRow(
//                Liszt.of(Stream.of(_artist.w(true))),
//                Liszt.of(Stream.of(_artist.i(true))),
//                Liszt.of(Stream.of(_artist.c(true)))
//        );
//    }

//    private Stream<String> putLettersInRow(Collection<Stream<String>>... collection) {
//        Stream.Builder<String> builder = Stream.builder();
//
//        for (int i = 0; i < _letterHeight; i++) {
//            Stream.Builder<String> lines = Stream.builder();
//
//            int row = 1;
//            for (int j = 0; j <= collection.length; j++) {
//                var test = collection[j].get_data()[row - 1];
//                lines.accept(test.toString());
//                row *= _letterHeight;
//            }
//
//            lines.build().forEach(builder);
//        }
//
//        return builder.build();
//    }

    public Stream<String> bBun() {
        return Stream.of(
         "    __________     __________    ",
                "   / BBBBBBBB A   / BBBBBBBBBB   ",
                "  / BBB    BBB A_/ BBB      BBB  ",
                " / BBB       BBBBBBBB        BBB ",
                "| BBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|",
                "| BBBBBBBBBBBBBBBBBBBBBBBBBBBBBB|",
                "|_______________________________|"
        );
    }

    public Stream<String> hButton() {
        return Stream.of(
         " _______________________________ ",
                "|  HHHHHHHHHHHHHHHHHHHHHHHHHHHHH|",
                "|_____________ HHH _____________|",
                "              |HHH|              ",
                " _____________|HHH|_____________ ",
                "|  HHHHHHHHHHHHHHHHHHHHHHHHHHHHH|",
                "|_______________________________|"
        );
    }
}
