package com.mungkorn.springbootecommerceapi.controllers;


import com.mungkorn.springbootecommerceapi.config.JwtConfig;
import com.mungkorn.springbootecommerceapi.dtos.JwtResponse;
import com.mungkorn.springbootecommerceapi.dtos.LoginRequestDto;
import com.mungkorn.springbootecommerceapi.dtos.UserDto;
import com.mungkorn.springbootecommerceapi.mappers.UserMapper;
import com.mungkorn.springbootecommerceapi.repositories.UserRepository;
import com.mungkorn.springbootecommerceapi.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequestDto request,
            HttpServletResponse response

    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generaRefreshToken(user);

        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); // 7d
        cookie.setSecure(true);
        response.addCookie(cookie);


        return ResponseEntity.ok(new JwtResponse(accessToken));

    }

    @PostMapping("/validate")
    public boolean validate(@RequestHeader("Authorization") String authHeader) {
        System.out.println("Validating called");
        var token = authHeader.replace("Bearer ", "");
        return jwtService.validateToken(token);
    }
    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        var user = userRepository.findById(userId).orElseThrow();

        var userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }



    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
