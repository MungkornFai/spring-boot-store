package com.mungkorn.springbootecommerceapi.auth;


import com.mungkorn.springbootecommerceapi.users.UserDto;
import com.mungkorn.springbootecommerceapi.users.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final JwtConfig jwtConfig;
    private final UserMapper userMapper;
    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponse login(@Valid @RequestBody LogInRequest request, HttpServletResponse response) {
        var loginResult = authService.login(request);
        var refreshToken = loginResult.getRefreshToken().toString();

        // set cookie
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return new JwtResponse(loginResult.getAccessToken().toString());

    }

    @PostMapping("/refresh")
    public JwtResponse refreshToken(@CookieValue(name = "refreshToken") String refreshToken){
        var accessToken = authService.refreshAccessToken(refreshToken);
        return new JwtResponse(accessToken.toString());

    }


    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser(){
        var user = authService.getCurrectUser();
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        var userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }



    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
