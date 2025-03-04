package laustrup.bandwichpersistence.core.utilities.console;

import laustrup.bandwichpersistence.core.utilities.Utility;

/**
 * Paints String that will be used for the console into another colour
 * and/or with an underline and/or background.
 */
public abstract class Painter extends Utility {

    /**
     * Will generate a escape-sequence for a specific colour.
     * @param left The left value.
     * @param right The right value.
     * @return The generated escape-sequence.
     */
    private static String colourCode(int left, int right) {
        return "\033[" + left + ";" + right + "m";
    }

    /**
     * Will generate a escape-sequence for a specific colour.
     * @param value The value of the escape-sequence.
     * @return The generated escape-sequence.
     */
    private static String colourCode(int value) {
        return "\033[" + value + "m";
    }

    /**
     * This will reset the colour of the text in the console to the default white colour.
     */
    private static final String RESET = colourCode(0);

    /**
     * Determines the colours available.
     */
    protected enum Colour {
        BLACK,RED,GREEN,YELLOW,BLUE,PURPLE,CYAN,WHITE
    }

    /**
     * Removes all colours that are being used in this Painter.
     * Needs to be updated everytime a new colour is a possible use.
     * @param content The String that should have its colour escape-sequences removed.
     * @return The content without any colours.
     */
    protected String removeColours(String content) {
        for (int i = 90; i <= 97; i++)
            content = content.replace(colourCode(0,i),"");
        content = content.replace(colourCode(0),"");
        return content;
    }

    /**
     * This determines which colour have been used for previous colouring.
     */
    protected static Colour _previousColour;

    /**
     * Describes the escape-sequences of colours
     * to the text of consoles as regular type.
     */
    private enum Regular {
        BLACK(colourCode(0,30)),
        RED(colourCode(0,31)),
        GREEN(colourCode(0,32)),
        YELLOW(colourCode(0,33)),
        BLUE(colourCode(0,34)),
        PURPLE(colourCode(0,35)),
        CYAN(colourCode(0,36)),
        WHITE(colourCode(0,37));

        public final String _colour;
        private Regular(String colour) { _colour = colour; }
    }

    /**
     * Describes the escape-sequences of colours
     * to the text of consoles as bold type.
     */
    private enum Bold {
        BLACK(colourCode(1,30)),
        RED(colourCode(1,31)),
        GREEN(colourCode(1,32)),
        YELLOW(colourCode(1,33)),
        BLUE(colourCode(1,34)),
        PURPLE(colourCode(1,35)),
        CYAN(colourCode(1,36)),
        WHITE(colourCode(1,37));

        public final String _colour;
        private Bold(String colour) { _colour = colour; }
    }

    /**
     * Describes the escape-sequences of colours
     * to the text of consoles as High Intensity type.
     */
    private enum HighIntensity {
        BLACK(colourCode(0,90)),
        RED(colourCode(0,91)),
        GREEN(colourCode(0,92)),
        YELLOW(colourCode(0,93)),
        BLUE(colourCode(0,94)),
        PURPLE(colourCode(0,95)),
        CYAN(colourCode(0,96)),
        WHITE(colourCode(0,97));

        public final String _colour;
        private HighIntensity(String colour) { _colour = colour; }
    }

    /**
     * Describes the escape-sequences of colours
     * to the text of consoles as Bold High Intensity type.
     */
    private enum BoldHighIntensity {
        BLACK(colourCode(1,90)),
        RED(colourCode(1,91)),
        GREEN(colourCode(1,92)),
        YELLOW(colourCode(1,93)),
        BLUE(colourCode(1,94)),
        PURPLE(colourCode(1,95)),
        CYAN(colourCode(1,96)),
        WHITE(colourCode(1,97));

        public final String _colour;
        private BoldHighIntensity(String colour) { _colour = colour; }
    }

    /**
     * An underline to a text for the console with different colours.
     */
    private enum Underline {
        BLACK(colourCode(4,30)),
        RED(colourCode(4,31)),
        GREEN(colourCode(4,32)),
        YELLOW(colourCode(4,33)),
        BLUE(colourCode(4,34)),
        PURPLE(colourCode(4,35)),
        CYAN(colourCode(4,36)),
        WHITE(colourCode(4,37));

        public final String _colour;
        private Underline(String colour) { _colour = colour; }
    }

    /**
     * Escape-sequences for turning the background of some text on the console a specific colour.
     */
    private enum Background {
        BLACK(colourCode(40)),
        RED(colourCode(41)),
        GREEN(colourCode(42)),
        YELLOW(colourCode(43)),
        BLUE(colourCode(44)),
        PURPLE(colourCode(45)),
        CYAN(colourCode(46)),
        WHITE(colourCode(47));

        public final String _colour;
        private Background(String colour) { _colour = colour; }
    }

    /**
     * Escape-sequences for turning the background of some text on the console a specific colour in High Intensity.
     */
    private enum HighIntensityBackground {
        BLACK(colourCode(0,100)),
        RED(colourCode(0,101)),
        GREEN(colourCode(0,102)),
        YELLOW(colourCode(0,103)),
        BLUE(colourCode(0,104)),
        PURPLE(colourCode(0,105)),
        CYAN(colourCode(0,106)),
        WHITE(colourCode(0,107));

        public final String _colour;
        private HighIntensityBackground(String colour) { _colour = colour; }
    }

    /**
     * Will make a text a specified colour and afterward reset it.
     * @param text The text that will be printed to the console.
     * @param color The colour of the text as an escape-sequence.
     * @return A String with the text an
     */
    private static String colorize(String text, String color) {
        return color + text + RESET;
    }

    /**
     * Will generate a text into a colour with a switch case of the Colour enum.
     * @param text The text that will have its colour changed for the console.
     * @param colour An enum with the values of the different colours available.
     * @return The generated text with its colour. If default text, it will be white.
     */
    protected static String colorize(String text, Colour colour) {
        if (colour == null)
            return text;
        switch (colour) {
            case CYAN -> { return cyan(text); }
            case YELLOW -> { return yellow(text); }
            case GREEN -> { return green(text); }
            case RED -> { return red(text); }
            default -> { return white(text); }
        }
    }

    /**
     * Will make some specific text the colour cyan for console.
     * @param text The text that should be in cyan.
     * @return The text with some escape sequences for the console.
     */
    protected static String cyan(String text) {
        _previousColour = Colour.CYAN;
        return colorize(text, HighIntensity.CYAN._colour);
    }

    /**
     * Will make some specific text the colour green for console.
     * @param text The text that should be in green.
     * @return The text with some escape sequences for the console.
     */
    protected static String green(String text) {
        _previousColour = Colour.GREEN;
        return colorize(text, HighIntensity.GREEN._colour);
    }

    /**
     * Will make some specific text the colour yellow for console.
     * @param text The text that should be in yellow.
     * @return The text with some escape sequences for the console.
     */
    protected static String yellow(String text) {
        _previousColour = Colour.YELLOW;
        return colorize(text, HighIntensity.YELLOW._colour);
    }

    /**
     * Will make some specific text the colour red for console.
     * @param text The text that should be in red.
     * @return The text with some escape sequences for the console.
     */
    protected static String red(String text) {
        _previousColour = Colour.RED;
        return colorize(text, HighIntensity.RED._colour);
    }

    /**
     * Will make some specific text the colour white for console.
     * @param text The text that should be in white.
     * @return The text with some escape sequences for the console.
     */
    protected static String white(String text) {
        _previousColour = Colour.WHITE;
        return colorize(text, HighIntensity.WHITE._colour);
    }
}
