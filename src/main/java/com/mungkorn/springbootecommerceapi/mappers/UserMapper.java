package com.mungkorn.springbootecommerceapi.mappers;

import com.mungkorn.springbootecommerceapi.dtos.RegisterUserRequest;
import com.mungkorn.springbootecommerceapi.dtos.UpdateUserRequest;
import com.mungkorn.springbootecommerceapi.dtos.UserDto;
import com.mungkorn.springbootecommerceapi.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void updateUser(UpdateUserRequest request, @MappingTarget User user);
}
