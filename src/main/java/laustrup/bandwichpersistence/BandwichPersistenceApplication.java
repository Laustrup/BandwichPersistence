package laustrup.bandwichpersistence;

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
        SpringApplication.run(BandwichPersistenceApplication.class, args);
    }

}