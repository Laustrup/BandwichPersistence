package laustrup.bandwichpersistence;

import org.springframework.boot.SpringApplication;

public class BandwichPersistenceApplication {

    public static void main(String[] args) {
        if (ProgramInitializer.startup(args))
            SpringApplication.run(BandwichPersistenceApplication.class, args);
        else
            System.exit(-1);
    }
}
