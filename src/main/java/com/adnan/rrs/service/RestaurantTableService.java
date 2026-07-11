package com.adnan.rrs.service;

import com.adnan.rrs.dto.CreateRestaurantTableRequest;
import com.adnan.rrs.entity.Restaurant;
import com.adnan.rrs.entity.RestaurantTable;
import com.adnan.rrs.entity.User;
import com.adnan.rrs.repository.UserRepository;
import com.adnan.rrs.repository.RestaurantRepository;
import com.adnan.rrs.repository.RestaurantTableRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantTableService(
            RestaurantTableRepository restaurantTableRepository,
            RestaurantRepository restaurantRepository,
            UserRepository userRepository
    ){
        this.restaurantTableRepository = restaurantTableRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public RestaurantTable createTable(
            CreateRestaurantTableRequest request,
            String userEmail
    ) {
        User authenticatedUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Restaurant> restaurants = restaurantRepository.findByOwner(authenticatedUser);

        if (restaurants.isEmpty()) {
            throw new RuntimeException("Restaurant not found");
        }

        // TODO: When multiple restaurants per owner are supported in the UI,
        // use the selected restaurant instead of restaurants.get(0).
        Restaurant restaurant = restaurants.get(0);

        if(restaurantTableRepository.existsByRestaurantAndTableNumber(
                restaurant,
                request.getTableNumber())){
            throw new RuntimeException("A table with that number already exists");
        }
        RestaurantTable table = new RestaurantTable();

        table.setTableNumber(request.getTableNumber());
        table.setCapacity(request.getCapacity());
        table.setLocation(request.getLocation());
        table.setRestaurant(restaurant);

        return restaurantTableRepository.save(table);
    }

    // TODO: Return only the tables that belong to the authenticated restaurant.
    public List<RestaurantTable> getAllTables() {
        return restaurantTableRepository.findAll();
    }
}
