package com.adnan.rrs.service;

import com.adnan.rrs.dto.CreateRestaurantTableRequest;
import com.adnan.rrs.entity.Restaurant;
import com.adnan.rrs.entity.RestaurantTable;
import com.adnan.rrs.repository.RestaurantRepository;
import com.adnan.rrs.repository.RestaurantTableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantTableService(
            RestaurantTableRepository restaurantTableRepository,
            RestaurantRepository restaurantRepository
    ){
        this.restaurantTableRepository = restaurantTableRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public RestaurantTable createTable(
            CreateRestaurantTableRequest request
    ) {
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId()).orElseThrow(() -> new RuntimeException("Restaurant not found"));

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

    public List<RestaurantTable> getAllTables() {
        return restaurantTableRepository.findAll();
    }
}
