package com.adnan.rrs.service;

import com.adnan.rrs.entity.AccountType;
import com.adnan.rrs.entity.Restaurant;
import com.adnan.rrs.entity.RestaurantStatus;
import com.adnan.rrs.entity.User;
import com.adnan.rrs.repository.RestaurantRepository;

import com.adnan.rrs.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(
            RestaurantRepository restaurantRepository,
            UserRepository userRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public Restaurant createRestaurant(
            Restaurant restaurant,
            String userEmail
    ) {

        User authenticatedUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (authenticatedUser.getAccountType() != AccountType.RESTAURANT) {
            throw new RuntimeException("Only restaurant accounts can create restaurants");
        }
        restaurant.setOwner(authenticatedUser);
        restaurant.setStatus(RestaurantStatus.PENDING);

        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAllRestaurants(){
        return restaurantRepository.findAll();
    }
}
