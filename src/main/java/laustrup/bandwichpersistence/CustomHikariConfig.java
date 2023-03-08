package laustrup.bandwichpersistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import laustrup.bandwichpersistence.repositories.DbLibrary;
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
        setJdbcUrl(DbLibrary.get_instance().get_path());
        setUsername(DbLibrary.get_instance().get_user());
        setPassword(DbLibrary.get_instance().get_password());
        setPoolName("HikariCP");

        return new HikariDataSource(this);
    }

}