package com.adnan.rrs.controller;

import com.adnan.rrs.dto.CreateRestaurantTableRequest;
import com.adnan.rrs.entity.RestaurantTable;
import com.adnan.rrs.service.RestaurantTableService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class RestaurantTableController {

    private final RestaurantTableService restaurantTableService;

    public RestaurantTableController(RestaurantTableService restaurantTableService) {
        this.restaurantTableService = restaurantTableService;
    }

    @PostMapping
    public RestaurantTable createTable(
            @RequestBody CreateRestaurantTableRequest request
    ){
        return restaurantTableService.createTable(request);
    }

    @GetMapping
    public List<RestaurantTable> getAllTables(){
        return restaurantTableService.getAllTables();
    }
}
