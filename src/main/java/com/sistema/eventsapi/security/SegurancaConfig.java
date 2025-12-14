package com.sistema.eventsapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SegurancaConfig {

    private final JwtFiltro jwtFiltro;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // 📌 Rotas públicas
                        .requestMatchers(HttpMethod.GET, "/eventos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/eventos/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // 🔐 Rotas de sincronização (Portaria) — só ADMIN/PORTEIRO
                        .requestMatchers("/offline/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PORTEIRO")

                        // legado (se ainda usar)
                        .requestMatchers(HttpMethod.POST, "/presencas/offline").hasAnyAuthority("ROLE_ADMIN", "ROLE_PORTEIRO")

                        // 🔒 Demais rotas exigem autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFiltro, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
