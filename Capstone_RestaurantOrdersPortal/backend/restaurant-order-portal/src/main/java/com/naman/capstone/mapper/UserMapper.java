package com.naman.capstone.mapper;

import com.naman.capstone.dto.request.RegisterRequestDTO;
import com.naman.capstone.dto.response.LoginResponseDTO;
import com.naman.capstone.dto.response.UserResponseDTO;
import com.naman.capstone.entity.User;

/**
 * this class handles mapping between user entity and dto
 */
public class UserMapper {

    //mapping dto to entity
    public static User toEntity(RegisterRequestDTO requestDto) {
        User user = new User();
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());
        user.setPhoneNumber(requestDto.getPhoneNumber());
        user.setRole(requestDto.getRole());
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