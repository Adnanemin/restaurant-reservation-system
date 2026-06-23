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
import java.util.List;

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

    public List<Reservation> getPendingReservations(){
        return reservationRepository.findByStatus(ReservationStatus.PENDING);
    }

    public Reservation confirmReservation(Long reservationId){

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("Reservation not found"));

        if(reservation.getStatus() != ReservationStatus.PENDING){
            throw new RuntimeException("Only pending reservations can be confirmed");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setUpdatedAt(LocalDateTime.now());
        return  reservationRepository.save(reservation);
    }

    public Reservation cancelReservation(Long reservationId){

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getStatus() == ReservationStatus.COMPLETED){
            throw new RuntimeException("Completed reservation can not be cancelled");
        }
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new RuntimeException("Reservation is already cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setUpdatedAt(LocalDateTime.now());
        return  reservationRepository.save(reservation);
    }

    public Reservation completeReservation(Long reservationId){

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getStatus() == ReservationStatus.PENDING){
            throw new RuntimeException("Reservation must be confirmed first");
        }

        if  (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new RuntimeException("Cancelled reservation cannot be completed");
        }

        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new RuntimeException("Reservation is already completed");
        }

        reservation.setStatus(ReservationStatus.COMPLETED);
        reservation.setUpdatedAt(LocalDateTime.now());
        return  reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByUser(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return reservationRepository.findByUser(user);
    }

    public Reservation cancelOwnReservation(Long reservationId, Long userId) {

        User user  = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("Reservation not found"));

        if(!reservation.getUser().getId().equals(userId)){
            throw new RuntimeException("You can only cancel your own reservation");
        }

        if(reservation.getStatus() == ReservationStatus.COMPLETED){
            throw new RuntimeException("Completed reservation cannot be cancelled");
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new RuntimeException("Reservation is already cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setUpdatedAt(LocalDateTime.now());
        return  reservationRepository.save(reservation);
    }
}
