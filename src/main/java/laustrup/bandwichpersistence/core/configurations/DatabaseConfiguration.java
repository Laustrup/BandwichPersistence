package laustrup.bandwichpersistence.core.configurations;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import laustrup.bandwichpersistence.core.libraries.DatabaseLibrary;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static laustrup.bandwichpersistence.core.scriptorian.managers.ScriptorianManager.onStartup;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DatabaseConfiguration extends HikariConfig {

    private void setup() {
        setDriverClassName(DatabaseLibrary.get_driver());
        setJdbcUrl(DatabaseLibrary.get_connectionString());
        setUsername(DatabaseLibrary.get_user());
        setPassword(DatabaseLibrary.get_password());
    }

    @Bean
    public DataSource dataSource() {
        try {
            onStartup();
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            System.exit(2);
        }
        setup();
        return new HikariDataSource(this);
    }
}
