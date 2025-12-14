package com.sistema.eventsapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtServico {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /** Retorna o e-mail (subject) se o token for válido; senão retorna null */
    public String validarToken(String token) {
        try {
            return parseClaims(token).getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /** Extrai a role do token (claim "role"). Ex.: "ADMIN", "PORTEIRO", "USER" */
    public String extrairRole(String token) {
        try {
            Object role = parseClaims(token).get("role");
            return role != null ? role.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    // (Opcional) manter gerador de token se vocês usam em testes locais
    public String gerarToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
