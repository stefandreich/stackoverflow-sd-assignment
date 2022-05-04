package com.sd.stackoverflow.mapper;

import com.sd.stackoverflow.dto.UserDTO;
import com.sd.stackoverflow.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        return User.builder()
                .userId(userDTO.getUserId())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();

    }
}
