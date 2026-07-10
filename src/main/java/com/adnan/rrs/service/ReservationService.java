package com.adnan.rrs.service;

import com.adnan.rrs.dto.CreateReservationRequest;
import com.adnan.rrs.entity.*;
import com.adnan.rrs.repository.ReservationRepository;
import com.adnan.rrs.repository.RestaurantRepository;
import com.adnan.rrs.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final TableAssignmentService tableAssignmentService;

    public ReservationService(
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            RestaurantRepository restaurantRepository,
            TableAssignmentService tableAssignmentService){
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.tableAssignmentService = tableAssignmentService;
    }

    public Reservation createReservation(CreateReservationRequest request) {

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId()).orElseThrow(() -> new RuntimeException("Restaurant not found."));

        RestaurantTable assignedTable = tableAssignmentService.assignBestTable(
                restaurant,
                request.getReservationDate(),
                request.getReservationTime(),
                request.getNumberOfGuests()
        );

        // TODO: Get authenticated customer from the JWT.
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
        return  reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByUser(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return reservationRepository.findByUser(user);
    }

    public Reservation cancelOwnReservation(Long reservationId, Long userId) {

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
        return  reservationRepository.save(reservation);
    }
}
