package laustrup.bandwichpersistence;

public class Logo implements ILogo {

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
//        System.out.println(ansi);
    }
}
