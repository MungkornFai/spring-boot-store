package com.mungkorn.springbootecommerceapi.mappers;

import com.mungkorn.springbootecommerceapi.dtos.RegisterUserRequest;
import com.mungkorn.springbootecommerceapi.dtos.UserDto;
import com.mungkorn.springbootecommerceapi.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
}
