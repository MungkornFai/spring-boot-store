package com.mungkorn.springbootecommerceapi.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "Name is require")
    @Size(max = 255, message = "Name must be at least 255 characters")
    private String name;

    @NotBlank(message = "Email is require")
    @Email(message = "Email must be valid")
    @Lowercase(message = "Email must in lowercase")
    private String email;

    @NotBlank(message = "Password is require")
    @Size(min = 6, max = 18, message = "password must be between 6 to 18 characters long")
    private String password;
}
