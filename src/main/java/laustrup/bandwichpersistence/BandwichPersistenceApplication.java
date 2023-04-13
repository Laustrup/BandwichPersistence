package laustrup.bandwichpersistence;

import laustrup.bandwichpersistence.console_ui.Greeter;
import laustrup.bandwichpersistence.console_ui.Runner;
import laustrup.bandwichpersistence.utilities.console.Printer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

@SpringBootApplication
public class BandwichPersistenceApplication extends SpringBootServletInitializer {

    /** Will greet at and after startup if true. */
    private static final boolean doGreet = true;

    /**
     * Will start the application in test mode,
     * which means the database will be run as an inmemory H2 database,
     * set it as false to run the application with the configured database connections-strings.
     */
    private static final boolean _startInTestMode = true;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BandwichPersistenceApplication.class);
    }

    public static void main(String[] args) {
        Program.get_instance().setTestingMode(_startInTestMode);
        if (doGreet)
            Greeter.setup();

        run(args);
    }

    /**
     * This will run the program with the given arguments.
     * In case the program crashes, it will revive unless it was doing at startup.
     * @param args The arguments from the main().
     */
    private static void run(String[] args) {
        ConfigurableApplicationContext context = null;

        while (true) {
            try {
                context = SpringApplication.run(BandwichPersistenceApplication.class, args);
                Program.get_instance().applicationIsRunning();

                if (doGreet)
                    Greeter.startupStatus();

                Runner.running(context);
            } catch (Exception e) {
                if (context != null && context.isActive())
                    context.close();

                boolean doStop = !Program.get_instance().is_applicationIsRunning();

                Printer.get_instance().print("An uncaught exception occurred, " +
                        (doStop ? "It was was doing the startup and because of that must be fixed before continuing"
                                : "therefor the application will restart..."),e);
                if (doStop)
                    System.exit(665);
            }
        }
    }


}