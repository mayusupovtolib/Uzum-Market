package com.example.uzum_market.security;

import com.example.uzum_market.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if (token != null) {
            User user = getUserFromToken(token);
            if (user != null) {
                if (!user.isAccountNonExpired()) {
                    System.err.println("User Expired");
                } else if (!user.isAccountNonLocked()) {
                    System.err.println("User Locked");
                } else if (!user.isCredentialsNonExpired()) {
                    System.err.println("User Credential expired");
                } else if (!user.isEnabled()) {
                    System.err.println("User Disabled");
                } else {
                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.
                doFilter(request, response);
    }

    private User getUserFromToken(String token) {
        if (jwtProvider.valideToken(token)) {
            return jwtProvider.getUserFromToken(token);
        }
        return null;
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            token = token.split(" ")[1];
            return token;
        }
        return null;
    }


}
