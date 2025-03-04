package laustrup.bandwichpersistence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BandwichPersistenceApplication {

    public static void main(String[] args) {
        if (ProgramInitializer.startup(args))
            SpringApplication.run(BandwichPersistenceApplication.class, args);
        else
            System.exit(-1);
    }
}
