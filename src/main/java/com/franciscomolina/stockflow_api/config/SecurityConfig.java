package com.franciscomolina.stockflow_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración base de Spring Security para la API.
 *
 * <p>De momento sólo expone como público lo estrictamente necesario para
 * desarrollo y observabilidad (Swagger, OpenAPI, health checks). El resto
 * de endpoints permanecen autenticados.</p>
 *
 * <p>Cuando integremos el endpoint de login con JWT, este filtro se ampliará
 * con el resource server OAuth2 y el filter de bearer tokens.</p>
 */
@Configuration
public class SecurityConfig {

    /**
     * Rutas que no requieren autenticación.
     * Documentación API, health checks y, durante desarrollo, error pages.
     */
    private static final String[] PUBLIC_ENDPOINTS = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/health/**",
            "/actuator/info",
            "/error"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // API stateless: cada request lleva su propio token (próximamente JWT).
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // CSRF deshabilitado: no aplica a APIs REST stateless.
                .csrf(AbstractHttpConfigurer::disable)

                // Login form deshabilitado: usaremos JWT, no formularios.
                .formLogin(AbstractHttpConfigurer::disable)

                // HTTP Basic deshabilitado: cerramos puertas que no usaremos.
                .httpBasic(AbstractHttpConfigurer::disable)

                // Reglas de autorización.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * Encoder BCrypt para passwords. Coste 10 (default), buen balance
     * seguridad/rendimiento para login interactivo.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}