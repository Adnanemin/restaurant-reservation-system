package com.adnan.rrs.controller;

import com.adnan.rrs.entity.Restaurant;
import com.adnan.rrs.service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        // TODO: After Spring Security is added, obtain the authenticated restaurant account.
        // from the SecurityContext and pass it to the service.

        throw new UnsupportedOperationException("Restaurant creation will be completed after JWT integration.");
    }

    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }
}
