package com.upiiz.examen2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("adminpassword")
                .roles("ADMIN")
                .authorities("READ", "CREATE", "UPDATE", "DELETE")
                .build();
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("userpassword")
                .roles("USER")
                .authorities("READ")
                .build();
        UserDetails moderator = User.withDefaultPasswordEncoder()
                .username("moderator")
                .password("moderatorpassword")
                .roles("MODERATOR")
                .authorities("READ", "UPDATE")
                .build();
        UserDetails editor = User.withDefaultPasswordEncoder()
                .username("editor")
                .password("editorpassword")
                .roles("EDITOR")
                .authorities("READ", "WRITE", "UPDATE")
                .build();
        UserDetails developer = User.withDefaultPasswordEncoder()
                .username("developer")
                .password("developerpassword")
                .roles("DEVELOPER")
                .authorities("READ", "WRITE", "CREATE", "UPDATE", "DELETE", "CREATE-USER")
                .build();
        UserDetails analyst = User.withDefaultPasswordEncoder()
                .username("analyst")
                .password("analystpassword")
                .roles("ANALYST")
                .authorities("READ", "DELETE")
                .build();

        return new InMemoryUserDetailsManager(admin, user, moderator, editor, developer, analyst);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //configurar los filtros personalizados
        return httpSecurity.httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    http.requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/v1/expenses/**").hasAnyAuthority("READ");
                    http.requestMatchers(HttpMethod.PUT, "/api/v1/expenses/**").hasAnyAuthority("UPDATE");
                    http.requestMatchers(HttpMethod.DELETE, "/api/v1/expenses/**").hasAnyAuthority("DELETE");
                    http.requestMatchers(HttpMethod.POST, "/api/v1/expenses/**").hasAnyAuthority("CREATE");
                    http.anyRequest().denyAll();

                }).build();

    }

    /*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService, AuthenticationProvider authenticationProvider) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/expenses/**").authenticated()
                        .anyRequest().permitAll()
                )
                .authenticationProvider(authenticationProvider)
                .httpBasic(httpBasic -> {
                });

        return http.build();
    }

     */

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) { // Inyectar PasswordEncoder aquí
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}