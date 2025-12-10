package com.sistema.eventsapi.security;

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

// ************************ NOVAS IMPORTAÇÕES ************************
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import java.util.List;
// *******************************************************************

@Component
public class JwtFiltro extends OncePerRequestFilter {

    private final JwtServico jwtServico;

    public JwtFiltro(JwtServico jwtServico) {
        this.jwtServico = jwtServico;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        String usuario = jwtServico.validarToken(token);

        if (usuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // ************************ CÓDIGO CORRIGIDO AQUI ************************
            // O token da GATE API precisa ter alguma autoridade para acessar a rota /presencas/offline.
            // Aqui injetamos a autoridade "ROLE_ADMIN" no contexto do Spring Security.

            List<SimpleGrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(usuario, null, authorities);

            // *************************************************************************

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}