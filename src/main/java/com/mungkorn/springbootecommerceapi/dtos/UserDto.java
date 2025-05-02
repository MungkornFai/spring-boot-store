package com.mungkorn.springbootecommerceapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    @JsonProperty("user_id")
    private Long id;
    private String name;
    private String email;

}
