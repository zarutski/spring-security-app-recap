package com.recap.self.springcourse.security.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.recap.self.springcourse.security.secure.JWTUtil;
import com.recap.self.springcourse.security.service.PersonDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// --- check every request for token verification
@Component
public class JWTFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_PREFIX = "Bearer";
    private static final String EMPTY_TOKEN_ERROR_MESSAGE = "Empty JWT Token in Bearer Header";
    private static final String INVALID_TOKEN_ERROR_MESSAGE = "Invalid JWT Token in Bearer Header";
    private static final int TOKEN_START_INDEX = 7;

    private final JWTUtil jwtUtil;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public JWTFilter(JWTUtil jwtUtil, PersonDetailsService personDetailsService) {
        this.jwtUtil = jwtUtil;
        this.personDetailsService = personDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith(BEARER_TOKEN_PREFIX)) {
            String jwt = authHeader.substring(TOKEN_START_INDEX);

            if (jwt.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, EMPTY_TOKEN_ERROR_MESSAGE);
            } else {
                try {

                    String userNameClaim = jwtUtil.decodeJWTAndRetrieveClaim(jwt);
                    UserDetails userDetails = personDetailsService.loadUserByUsername(userNameClaim);
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    userDetails.getPassword(),
                                    userDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                } catch (JWTVerificationException exception) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, INVALID_TOKEN_ERROR_MESSAGE);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
