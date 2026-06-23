package com.adnan.rrs.repository;

import com.adnan.rrs.entity.Reservation;
import com.adnan.rrs.entity.RestaurantTable;
import com.adnan.rrs.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByTableAndReservationDateAndReservationTime(
            RestaurantTable table,
            LocalDate reservationDate,
            LocalTime reservationTime
    );

    List<Reservation> findByStatus(ReservationStatus status);
}