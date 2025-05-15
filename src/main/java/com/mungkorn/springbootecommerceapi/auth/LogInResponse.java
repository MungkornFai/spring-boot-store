package com.mungkorn.springbootecommerceapi.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LogInResponse {
    private Jwt accessToken;
    private Jwt refreshToken;
}
