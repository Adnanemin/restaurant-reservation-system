package com.adnan.rrs.controller;

import com.adnan.rrs.entity.Restaurant;
import com.adnan.rrs.service.RestaurantService;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public Restaurant createRestaurant(
            @RequestBody Restaurant restaurant,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return restaurantService.createRestaurant(
                restaurant,
                userDetails.getUsername()
        );
    }

    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }
}
