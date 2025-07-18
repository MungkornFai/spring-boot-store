package com.mungkorn.springbootecommerceapi.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LogInRequest {
    @NotBlank(message = "email is require")
    private String email;

    @NotBlank(message = "Password is require")
    @Size(min = 6, max = 18, message = "password must be between 6 to 18 characters long")
    private String password;
}
