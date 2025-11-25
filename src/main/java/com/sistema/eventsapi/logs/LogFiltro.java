package com.sistema.eventsapi.logs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LogFiltro extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String metodo = request.getMethod();
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();

        System.out.println("📌 LOG API → " + metodo + " " + uri + " | IP: " + ip);

        filterChain.doFilter(request, response);
    }
}
