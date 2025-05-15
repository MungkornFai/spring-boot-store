package com.mungkorn.springbootecommerceapi.users;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public Iterable<UserDto> getAllUsers(String sortBy) {
        if(!Set.of("name","email").contains(sortBy)) {
            sortBy = "name";
        }
        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return userMapper.toDto(user);
    }

    public UserDto updateUser(Long userId, UpdateUserRequest request) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userMapper.updateUser(request,user);

        return userMapper.toDto(user);
    }

    public UserDto registerUser(RegisterUserRequest request) {
        // check if user already exist
        if(userRepository.existsByEmail(request.getEmail())){
            throw new DeplicateUserException();
        }
        // map the data that was sent from client to entity shape for saving to database
        var user = userMapper.toEntity(request);
        // hash password before save it to database
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // set the role
        user.setRole(Role.USER);
        // then save it to database
        userRepository.save(user);
        // then map data to DTO shape before sent to client
        return userMapper.toDto(user);
    }

    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AccessDeniedException("Passwords doesn't match");
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }
}





















