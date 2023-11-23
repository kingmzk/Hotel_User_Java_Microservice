package com.lcwd.user.service.services;

import com.lcwd.user.service.entities.User;

import java.util.List;

public interface UserService {

    //user operations

    //create
    User saveUser(User user);


    //get all users
    List<User> getAllUsers();

    //get a single User with UserId
    User getUserById(String userId);


    User UpdateUser(User user);


    void deleteUser(String userId);
}
