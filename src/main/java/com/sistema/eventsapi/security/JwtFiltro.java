package com.sistema.authapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFiltro extends OncePerRequestFilter {

    private final JwtServico jwtServico;

    // ROTAS LIBERADAS (SEM TOKEN)
    private static final List<String> WHITELIST = List.of(
            "/auth",
            "/auth/",
            "/auth/login",
            "/auth/cadastrar",
            "/swagger-ui",
            "/swagger-ui/",
            "/v3/api-docs"
    );

    public JwtFiltro(JwtServico jwtServico) {
        this.jwtServico = jwtServico;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Swagger + Cadastro/Login = LIBERADO
        if (WHITELIST.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Verificar Header Authorization
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            // nenhuma autenticação configurada
            filterChain.doFilter(request, response);
            return;
        }

        // Extrai token JWT
        String token = header.substring(7);
        String email = jwtServico.validarToken(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            null
                    );

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
