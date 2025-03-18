package laustrup.bandwichpersistence;

import java.util.stream.Stream;

public class Logo implements ILogo {

    private final int _width = 20;

    private final char
            _framingFilling = ':',
            _framingEdge  = '+',
            _framingTop = '-',
            _wall = '|';

    private final String _framePart = String.format(
            "%s%s%s",
            _wall,
            _framingFilling + _framingFilling,
            _wall
    );

    private final Stream<String> _frameEdges = Stream.of(
            String.format(
                    "%s%s%s",
                    _framingEdge,
                    String.valueOf(_framingTop).repeat(_width),
                    _framingEdge
                ),
            String.format(
                    "%s%s%s",
                    _wall
            )
    );

    public static final String ansi = """
                +-------------------------+
                |  _________  _________   |
                | /         \\/         \\  |
                | |                     | |
                | |_____________________| |
                |      __       __    __  
                |     /  \\     |  \\  /  |
                |    / __ \\    |   \\/   |
                |   / /__\\ \\   |        |
                |  / /----\\ \\  | |\\  /
                | /_/      \\_\\ |_| \\/
                """;

    private static Logo _instance;

    private Logo() {}

    public static Logo get_instance() {
        if (_instance == null)
            _instance = new Logo();

        return _instance;
    }

    public void print() {
        framing(_frameEdges).forEach(System.out::println);
    }

    private Stream<String> framing(Stream<String> lines) {
        return Stream.concat(
                _frameEdges,
                Stream.concat(
                        lines.map(line -> String.format(
                                "%s%s%s",
                                _framePart,
                                line,
                                _framePart
                        )),
                        _frameEdges
                )
        );
    }
}
