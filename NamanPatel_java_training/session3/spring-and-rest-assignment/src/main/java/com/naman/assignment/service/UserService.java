package com.naman.assignment.service;

import com.naman.assignment.entity.User;
import com.naman.assignment.exception.BadRequestException;
import com.naman.assignment.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

//this class contains all business logic related to users
@Service
public class UserService {

    private final UserRepository userRepository;

    // constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    // method to search users
    public List<User> searchUsers(String name, Integer age, String role) {

        // if no parameters passed then return all users
        if((name == null || name.isEmpty()) && age == null && (role == null || role.isEmpty())) {
            return userRepository.getAllUsers();
        }
        else{
            // else search users
            return userRepository.searchUsers(name, age, role);
        }
    }



    // method to submit user data
    public String submitUser(User user) {

        // manual validation for each field
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new BadRequestException("Name is required");          // throws custom exception if field is empty or null
        }

        if (user.getAge() == null) {
            throw new BadRequestException("Age is required");
        }

        if (user.getRole() == null || user.getRole().isEmpty()) {
            throw new BadRequestException("Role is required ADMIN/USER");
        }

        return "User data Submitted successfully";
    }



    // method to delete user with confirmation
    public String deleteUser(Integer id, Boolean confirm) {

        // if confirm is false or not provided then return message
        if (confirm == null || !confirm) {
            return "Confirmation required";
        }

        // delete user
        boolean deleted = userRepository.deleteUserById(id);

        return deleted ? "User deleted successfully" : "User not found";
    }
}