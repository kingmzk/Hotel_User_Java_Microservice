package com.lcwd.user.service.impl;

import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.lcwd.user.service.repositories.UserRepository;
import com.lcwd.user.service.services.UserService;
import com.lcwd.user.service.exceptions.ResourceNotFoundException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(UserServiceimpl.class);
    @Override
    public User saveUser(User user) {
        String randomUserId = UUID.randomUUID().toString();  //unique user identifier
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {

//     return userRepository.findAll();

        // Fetch all users from the database
        List<User> users = userRepository.findAll();

        // For each user, fetch their ratings from the Rating service
        for (User user : users) {
            ArrayList<Rating> ratingsOfUser = restTemplate.getForObject(
                    "http://localhost:8084/ratings/users/" + user.getUserId(), ArrayList.class);

            logger.info("{} " + ratingsOfUser);

            user.setRatings(ratingsOfUser);
        }

        return users;

    }

    @Override
    public User getUserById(String userId) {
        //get user from database with the help of user repository
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id is not found on server " + userId));

        //fetch rating of the above user from Rating service
       Rating[] ratingsOfUser =  restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getUserId(), Rating[].class);
       logger.info("{} "+ratingsOfUser);

       List<Rating> ratings =  Arrays.stream(ratingsOfUser).toList();

       List<Rating> ratingList = ratings.stream().map(rating -> {
          ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/" + rating.getHotelId(), Hotel.class);
          Hotel hotel = forEntity.getBody();

          logger.info("response status code: {}", forEntity.getStatusCode());

          rating.setHotel(hotel);

          return  rating;
       }).collect(Collectors.toList());



       user.setRatings(ratingList);

        return user;
    }

    @Override
    public User UpdateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(String userId) {

    }
}
