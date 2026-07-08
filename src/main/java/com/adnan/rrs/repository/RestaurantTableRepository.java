package com.adnan.rrs.repository;

import com.adnan.rrs.entity.Restaurant;
import com.adnan.rrs.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    List<RestaurantTable>   findByRestaurant(Restaurant restaurant);
    List<RestaurantTable> findByRestaurantAndCapacityGreaterThanEqual(Restaurant restaurant, int capacity);
    boolean existsByRestaurantAndTableNumber(
            Restaurant restaurant,
            Integer tableNumber
    );
}