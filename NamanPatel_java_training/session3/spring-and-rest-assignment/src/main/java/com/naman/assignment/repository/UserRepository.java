package com.naman.assignment.repository;

// this class handles database related operations (here we have used dummy data)

import com.naman.assignment.entity.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    //list to store all users (acts like dummy database)
    private List<User> users = new ArrayList<>();

    //initialize dummy data inside constructor
    public UserRepository() {
        users.add(new User(1, "Naman", 21, "USER"));
        users.add(new User(2, "Modi", 25, "ADMIN"));
        users.add(new User(3, "Kejriwal", 30, "USER"));
        users.add(new User(4, "Adani", 22, "USER"));
        users.add(new User(5, "Ambani", 30, "ADMIN"));
    }


    // method to search users
    public List<User> searchUsers(String name, Integer age, String role) {

        //list to store search result users
        List<User> searchResult = new ArrayList<>();

        //loop through each user from dummy DB
        for (User user : users) {

            boolean match = true;            //initially all users matches the conditions

            // checking name
            if (name != null && !name.isEmpty()) {                 //when request contains name, this block will execute
                if (!user.getName().equalsIgnoreCase(name)) {      // this condition is checked for each user
                    match = false;
                }
            }

            // checking age
            if (age != null) {
                if (!user.getAge().equals(age)) {
                    match = false;
                }
            }

            // checking role
            if (role != null && !role.isEmpty()) {
                if (!user.getRole().equalsIgnoreCase(role)) {
                    match = false;
                }
            }

            // Adding user only when all required conditions matches
            if (match) {
                searchResult.add(user);
            }
        }

        return searchResult;
    }


    // method to delete user by id
    public boolean deleteUserById(Integer id) {

        for (User user : users) {
            if (user.getId().equals(id)) {
                users.remove(user);          //remove user when id matches
                return true;
            }
        }

        return false;          //return false if user not found
    }

}

