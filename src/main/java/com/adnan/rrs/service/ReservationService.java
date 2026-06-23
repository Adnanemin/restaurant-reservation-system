package com.adnan.rrs.service;

import com.adnan.rrs.dto.CreateReservationRequest;
import com.adnan.rrs.entity.Reservation;
import com.adnan.rrs.entity.ReservationStatus;
import com.adnan.rrs.entity.RestaurantTable;
import com.adnan.rrs.entity.User;
import com.adnan.rrs.repository.ReservationRepository;
import com.adnan.rrs.repository.RestaurantTableRepository;
import com.adnan.rrs.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            RestaurantTableRepository restaurantTableRepository){
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.restaurantTableRepository = restaurantTableRepository;
    }

    public Reservation createReservation(CreateReservationRequest request) {

        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        RestaurantTable table = restaurantTableRepository.findById(request.getTableId()).orElseThrow(() -> new RuntimeException("Table not found"));

        if(request.getNumberOfGuests() > table.getCapacity()){
            throw new RuntimeException("Number of guests exceeds table capacity");
        }

        if (reservationRepository.existsByTableAndReservationDateAndReservationTime(table, request.getReservationDate(), request.getReservationTime())) {
            throw new RuntimeException("Table is already reserved for this date and time");
        }

        Reservation reservation = new Reservation();

        reservation.setUser(user);
        reservation.setTable(table);

        reservation.setReservationDate(request.getReservationDate());
        reservation.setReservationTime(request.getReservationTime());
        reservation.setNumberOfGuests(request.getNumberOfGuests());
        reservation.setSpecialRequest(request.getSpecialRequest());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDateTime.now());
        return  reservationRepository.save(reservation);
    }
}
