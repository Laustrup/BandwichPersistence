package laustrup.bandwichpersistence;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BandwichPersistenceApplication {

    public static void main(String[] args) {
        Program.start(
                BandwichPersistenceApplication.class,
                "bandwich",
                args
        );
    }
}
