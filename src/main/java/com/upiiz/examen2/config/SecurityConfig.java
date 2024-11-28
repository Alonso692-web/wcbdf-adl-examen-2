package com.upiiz.examen2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable() // Deshabilitar CSRF para facilitar pruebas (habilítalo en producción)
                .httpBasic(Customizer.withDefaults()) // Autenticación básica
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless para APIs REST
                .authorizeHttpRequests(auth -> {
                    // Configuración de permisos por endpoint
                    auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasAuthority("CREATE");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/expenses/**").hasAuthority("READ");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/expenses/**").hasAuthority("CREATE");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/expenses/**").hasAuthority("UPDATE");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/expenses/**").hasAuthority("DELETE");
                    auth.anyRequest().authenticated();
                })
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder()); // Codificador de contraseñas
        daoAuthenticationProvider.setUserDetailsService(userDetailsService()); // Servicio de detalles del usuario
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
        // return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("1234"))
                .roles("ADMIN")
                .authorities("READ", "CREATE", "UPDATE", "DELETE")
                .build();
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("user1234"))
                .roles("USER")
                .authorities("READ", "CREATE")
                .build();
        UserDetails moderator = User.withUsername("moderator")
                .password(passwordEncoder().encode("mode1234"))
                .roles("MODERATOR")
                .authorities("READ", "UPDATE")
                .build();
        UserDetails editor = User.withUsername("editor")
                .password(passwordEncoder().encode("editor1234"))
                .roles("EDITOR")
                .authorities("READ", "WRITE", "UPDATE")
                .build();
        UserDetails developer = User.withUsername("developer")
                .password(passwordEncoder().encode("dev1234"))
                .roles("DEVELOPER")
                .authorities("READ", "WRITE", "CREATE", "UPDATE", "DELETE", "CREATE-USER")
                .build();
        UserDetails analyst = User.withUsername("analyst")
                .password(passwordEncoder().encode("ana1234"))
                .roles("ANALYST")
                .authorities("READ", "DELETE")
                .build();

        return new InMemoryUserDetailsManager(admin, user, moderator, editor, developer, analyst);
    }
}