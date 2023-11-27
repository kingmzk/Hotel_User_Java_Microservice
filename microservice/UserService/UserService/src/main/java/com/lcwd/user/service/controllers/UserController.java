package com.lcwd.user.service.controllers;

import com.lcwd.user.service.entities.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lcwd.user.service.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    //create
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User user1 = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    //get user by id
    int retryCount = 1;
    @GetMapping("/{userId}")
    //@CircuitBreaker(name="ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    //@Retry(name="ratingHotelService", fallbackMethod = "ratingHotelFallback")
    @RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getUserById(@PathVariable("userId") String userId) {
        logger.info("Retry count : " + retryCount++);
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }


    //creating fallback method of rating hotel
    public ResponseEntity<User> ratingHotelFallback(String userId, Exception e){
        logger.info("Rating Hotel Fallback method called : " + e.getMessage());
        User user = User.builder()
                .email("Dummy@gmail.com")
                .name("Dummy").
                about("this is dummy user because service is down").userId("12124").build();
        return new ResponseEntity<>(user,HttpStatus.OK);
    }




    //get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}