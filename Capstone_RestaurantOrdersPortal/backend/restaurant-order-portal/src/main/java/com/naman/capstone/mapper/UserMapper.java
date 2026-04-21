package com.naman.capstone.mapper;

import com.naman.capstone.dto.UserRequestDTO;
import com.naman.capstone.dto.UserResponseDTO;
import com.naman.capstone.entity.User;

//this class contains mapping logic for user
public class UserMapper {

    //mapping dto to entity
    public static User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(dto.getRole());
        return user;
    }

    //mapping entity to dto
    public static UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setWalletBalance(user.getWalletBalance());
        return dto;
    }
}