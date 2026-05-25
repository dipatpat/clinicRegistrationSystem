package pjatk.mas.clinicregistrationsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("""
                ROLE_DOCTOR > ROLE_STAFF
                ROLE_RECEPTIONIST > ROLE_STAFF
                ROLE_MANAGER > ROLE_STAFF
                """);
        return hierarchy;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/my-appointments/**", "/my-profile/**").hasRole("PATIENT")
                        .requestMatchers("/employees/**", "/payroll/**").hasRole("MANAGER")
                        .requestMatchers("/my-schedule/**").hasRole("DOCTOR")
                        .requestMatchers("/medical-records/**").hasRole("DOCTOR")
                        .requestMatchers("/appointments/**", "/patients/**", "/doctors/**").hasRole("STAFF")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/")
                        .loginProcessingUrl("/perform_login")
                        .successHandler(authenticationSuccessHandler())
                        .failureUrl("/?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String roles = authentication.getAuthorities().toString();
            if (roles.contains("ROLE_MANAGER")) {
                response.sendRedirect("/dashboard");
            } else if (roles.contains("ROLE_DOCTOR")) {
                response.sendRedirect("/my-schedule");
            } else if (roles.contains("ROLE_RECEPTIONIST")) {
                response.sendRedirect("/appointments");
            } else if (roles.contains("ROLE_PATIENT")) {
                response.sendRedirect("/my-appointments");
            } else {
                response.sendRedirect("/");
            }
        };
    }
}
