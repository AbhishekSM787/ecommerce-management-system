package com.example.ecommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    private final CustomUserDetailsService users;
    public JwtAuthenticationFilter(JwtService jwt, CustomUserDetailsService users) {
        this.jwt = jwt; this.users = users;
    }
    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                              FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String email = jwt.extractUsername(token);
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails details = users.loadUserByUsername(email);
                    if (jwt.isValid(token, details)) {
                        UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (RuntimeException ignored) {
                // Invalid or expired tokens remain unauthenticated; protected routes return 401.
            }
        }
        chain.doFilter(request, response);
    }
}
