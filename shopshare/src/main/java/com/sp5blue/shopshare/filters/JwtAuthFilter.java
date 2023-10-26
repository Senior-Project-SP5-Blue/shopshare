package com.sp5blue.shopshare.filters;


import com.sp5blue.shopshare.models.user.Token;
import com.sp5blue.shopshare.models.user.User;
import com.sp5blue.shopshare.services.security.JwtService;
import com.sp5blue.shopshare.services.user.UserService;
import com.sp5blue.shopshare.services.token.ITokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ITokenService tokenService;
    Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService, ITokenService tokenService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt = null;
        UUID id = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            Optional<UUID> _id = jwtService.extractId(jwt);
            if (_id.isPresent()) id = _id.get();
        }

        if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CompletableFuture<User> getUserDetails = ((UserService)userDetailsService).getUserById(id);
            CompletableFuture<Token> getToken = tokenService.readByToken(jwt);
            CompletableFuture.allOf(getUserDetails, getToken).join();
            User userDetails = getUserDetails.join();
            Token token = getToken.join();
            boolean isTokenValid = !token.isExpired() && !token.isRevoked();

            if (jwtService.validateToken(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
