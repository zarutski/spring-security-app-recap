package com.recap.self.springcourse.security.config;

import com.recap.self.springcourse.security.service.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    // authentication configuration [integration of UserDetailsService with authentication process]
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(personDetailsService);
        authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return authProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // security/authorization configuration [with custom login form] - overrides default authentication logic (using default form and permissions)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // works step by step, just like a chain if-else -> if-else -> if-else (nested in depth)
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/login", "/auth/registration", "/error")
                        .permitAll() // permit all requests for listed resources
                        .anyRequest().authenticated() // any other requests should be authenticated
                )
                .formLogin(form -> form
                        .loginPage("/auth/login") // page used by Spring for default login page replacement
                        .loginProcessingUrl("/process_login") // address for form data processing (spring will wait for request to this URL, and will automatically process them - no need to create separate controller method); names of the fields sent should be 'username', 'password' for Spring to process them
                        .defaultSuccessUrl("/hello", true) // redirect (by Spring) in case of successful authentication
                        .failureUrl("/auth/login?error") // redirect in case of failure (additional parameter 'error' for error message rendering)
                )
                .csrf(AbstractHttpConfigurer::disable); // disable CSRF (csrf token should be passed with every request, disabled for now)

        return http.build();
    }

}
