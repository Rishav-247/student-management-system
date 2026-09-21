package com.example.sms.config;

import com.example.sms.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                // Static resources
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll()
                // Public auth & error routes
                .requestMatchers("/login", "/access-denied", "/error").permitAll()
                // Admin-only management
                .requestMatchers("/users/**", "/settings/**").hasRole("ADMIN")
                .requestMatchers("/departments/new", "/departments/edit/**", "/departments/delete/**").hasRole("ADMIN")
                .requestMatchers("/courses/new", "/courses/edit/**", "/courses/delete/**").hasRole("ADMIN")
                .requestMatchers("/teachers/new", "/teachers/edit/**", "/teachers/delete/**").hasRole("ADMIN")
                // Teacher and Admin functionality
                .requestMatchers("/students/new", "/students/edit/**", "/students/delete/**").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers("/attendance/mark", "/attendance/save", "/attendance/edit/**").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers("/marks/record", "/marks/save", "/marks/edit/**", "/marks/delete/**").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers("/enrollments/new", "/enrollments/save", "/enrollments/delete/**").hasAnyRole("ADMIN", "TEACHER")
                // Authenticated read routes for all roles (ADMIN, TEACHER, STUDENT)
                .requestMatchers("/", "/dashboard", "/students/**", "/teachers/**", "/departments/**", "/courses/**",
                                 "/subjects/**", "/enrollments/**", "/attendance/**", "/marks/**", "/reports/**", "/api/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/access-denied")
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin()) // Allow H2 or embedded frames if needed
            );

        return http.build();
    }
}
