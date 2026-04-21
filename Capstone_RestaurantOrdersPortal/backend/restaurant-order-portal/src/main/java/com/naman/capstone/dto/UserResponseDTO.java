package com.naman.capstone.dto;

import com.naman.capstone.enums.Role;
import lombok.*;

import java.math.BigDecimal;

//Represents the data sent back to the client after user operations
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;
    private BigDecimal walletBalance;
}