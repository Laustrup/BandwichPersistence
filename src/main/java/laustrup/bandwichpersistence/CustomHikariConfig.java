package laustrup.bandwichpersistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import laustrup.bandwichpersistence.miscs.Crate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class CustomHikariConfig extends HikariConfig {

    @Bean
    public DataSource dataSource() {
        setDriverClassName("com.mysql.cj.jdbc.Driver");
        setJdbcUrl(Crate.get_instance().get_dbPath());
        setUsername(Crate.get_instance().get_dbUser());
        setPassword(Crate.get_instance().get_dbPassword());
        setPoolName("HikariCP");

        return new HikariDataSource(this);
    }

}