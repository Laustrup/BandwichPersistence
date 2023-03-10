package laustrup.bandwichpersistence;

import laustrup.bandwichpersistence.console_ui.Greeter;
import laustrup.bandwichpersistence.utilities.console.Printer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BandwichPersistenceApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BandwichPersistenceApplication.class);
    }

    public static void main(String[] args) {
        Greeter.startUp();

        try {
            SpringApplication.run(BandwichPersistenceApplication.class, args);
            Greeter.running();
        } catch (Exception e) {
            Printer.get_instance().print("An uncaught exception occurred, therefor the application will restart...",e);
            main(new String[]{});
        }
    }
}