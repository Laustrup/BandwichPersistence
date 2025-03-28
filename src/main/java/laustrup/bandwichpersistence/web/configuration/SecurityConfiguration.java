package laustrup.bandwichpersistence.web.configuration;

import laustrup.bandwichpersistence.core.managers.UserDetailsManager;
import laustrup.bandwichpersistence.web.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final Logger _logger = Logger.getLogger(SecurityConfiguration.class.getName());

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(SecurityConfiguration::configureRequests)
//                .logout(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults())
                .sessionManagement(SecurityConfiguration::configureSessionManagement)
                .build();
    }

    private static <T extends HttpSecurityBuilder<T>> void configureRequests(
            AuthorizeHttpRequestsConfigurer<T>.AuthorizationManagerRequestMatcherRegistry registry
    ) {
        registry
                .anyRequest()
                .permitAll();
    }

    private static <T extends HttpSecurityBuilder<T>> void configureSessionManagement(
            SessionManagementConfigurer<T> configurer
    ) {
        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(UserDetailsManager::getUserDetails);
        authenticationProvider.setPasswordEncoder(new PasswordEncoder());

        return authenticationProvider;
    }
}
