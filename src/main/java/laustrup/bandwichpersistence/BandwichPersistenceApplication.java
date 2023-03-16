package laustrup.bandwichpersistence;

import laustrup.bandwichpersistence.console_ui.Greeter;
import laustrup.bandwichpersistence.utilities.console.Printer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BandwichPersistenceApplication extends SpringBootServletInitializer {

    /** Will greet at and after startup if true. */
    private static final boolean doGreet = true;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BandwichPersistenceApplication.class);
    }

    public static void main(String[] args) {
        Program.get_instance().setTestingMode(false);
        if (doGreet)
            Greeter.startUp();

        running(args);
    }

    /**
     * This will run the program.
     * In case the program crashes, it will revive unless it was doing at startup.
     * @param args The args from the main().
     */
    private static void running(String[] args) {
        while (true) {
            try {
                ConfigurableApplicationContext context = SpringApplication.run(BandwichPersistenceApplication.class, args);
                Program.get_instance().applicationIsRunning();

                if (doGreet)
                    Greeter.running();

                while (context.isActive()) {
                    Thread.sleep(10000);
                }
            } catch (Exception e) {
                boolean doStop = !Program.get_instance().is_applicationIsRunning();

                Printer.get_instance().print("An uncaught exception occurred, " +
                        (doStop ? "It was was doing the startup and because of that must be fixed before continuing"
                                : "therefor the application will restart..."),e);
                if (doStop)
                    break;
            }
        }
    }
}