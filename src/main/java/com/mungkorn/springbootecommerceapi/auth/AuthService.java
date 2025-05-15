package com.mungkorn.springbootecommerceapi.auth;

import com.mungkorn.springbootecommerceapi.users.User;
import com.mungkorn.springbootecommerceapi.users.UserMapper;
import com.mungkorn.springbootecommerceapi.users.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public LogInResponse login(LogInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generaRefreshToken(user);


        return new LogInResponse(accessToken,refreshToken);
    }

    public Jwt refreshAccessToken(String refreshToken) {
        var jwt = jwtService.parseToken(refreshToken);
        if(jwt == null || jwt.isExpired()){
            throw  new BadCredentialsException("Invalid refresh token");
        }
        var user = userRepository.findById(jwt.getUserId()).orElseThrow();


        return jwtService.generaRefreshToken(user);
    }

    public User getCurrectUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();
        return userRepository.findById(userId).orElse(null);
    }
}
