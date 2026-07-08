package com.adnan.rrs.service;

import com.adnan.rrs.entity.RestaurantTable;
import com.adnan.rrs.entity.Restaurant;

import com.adnan.rrs.repository.ReservationRepository;
import com.adnan.rrs.repository.RestaurantTableRepository;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class TableAssignmentService {

    private final RestaurantTableRepository restaurantTableRepository;
    private final ReservationRepository reservationRepository;

    public TableAssignmentService(
            RestaurantTableRepository restaurantTableRepository,
            ReservationRepository reservationRepository
    ){
        this.restaurantTableRepository = restaurantTableRepository;
        this.reservationRepository = reservationRepository;
    }

    public RestaurantTable assignBestTable(
            Restaurant restaurant,
            LocalDate date,
            LocalTime time,
            Integer guestCount
    ){
        List<RestaurantTable> candidateTables = restaurantTableRepository.findByRestaurantAndCapacityGreaterThanEqual(restaurant, guestCount);
        candidateTables.sort(Comparator.comparing(RestaurantTable::getCapacity));

        for (RestaurantTable table : candidateTables) {
            boolean reserved = reservationRepository.existsByTableAndReservationDateAndReservationTime(table, date, time);
            if (!reserved) {
                return table;
            }
        }

        throw new RuntimeException("No suitable table is available for the selected date and time.");
    }
}
