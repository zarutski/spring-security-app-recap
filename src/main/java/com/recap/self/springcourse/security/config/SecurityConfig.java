package com.recap.self.springcourse.security.config;

import com.recap.self.springcourse.security.service.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // security configuration on method-level [prePostEnabled(default = true) - adds Pre/Post authorize method configuration; without it only @Secured @RolesAllowed annotations available]
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
        authProvider.setPasswordEncoder(passwordEncoder()); // set PasswordEncoder, so Spring will use it automatically while authentication for passwords check (no need in manual encoding for authentication)
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
        // so more specific rules better to move upper (for any request Spring will apply first matched rule without moving forward)
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/login", "/auth/registration", "/error")
                        .permitAll() // permit all requests for listed resources
                        .anyRequest().hasAnyRole("USER", "ADMIN") // any other resources [only users with one of the listed Roles]
                )
                .formLogin(form -> form
                        .loginPage("/auth/login") // page used by Spring for default login page replacement
                        .loginProcessingUrl("/process_login") // address for form data processing (spring will wait for request to this URL, and will automatically process them - no need to create separate controller method); names of the fields sent should be 'username', 'password' for Spring to process them
                        .defaultSuccessUrl("/hello", true) // redirect (by Spring) in case of successful authentication
                        .failureUrl("/auth/login?error") // redirect in case of failure (additional parameter 'error' for error message rendering)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL for logout requests [user will be removed from session, browser will no longer store cookies]
                        .logoutSuccessUrl("/auth/login")); // redirect URL after successful logout
        // CSRF protection enabled by default (csrf token should be passed with every request, disabled for now)

        return http.build();
    }

}
