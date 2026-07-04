package com.adnan.rrs.repository;

import com.adnan.rrs.entity.Restaurant;
import com.adnan.rrs.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    boolean existsByRestaurantAndTableNumber(
            Restaurant restaurant,
            Integer tableNumber
    );
}