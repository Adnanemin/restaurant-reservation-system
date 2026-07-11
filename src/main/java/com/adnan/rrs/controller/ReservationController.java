package com.adnan.rrs.controller;

import com.adnan.rrs.dto.CreateReservationRequest;
import com.adnan.rrs.entity.Reservation;
import com.adnan.rrs.service.ReservationService;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public Reservation createReservation(
            @RequestBody CreateReservationRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return reservationService.createReservation(
                request,
                userDetails.getUsername()
        );
    }

    @GetMapping("/pending")
    public List<Reservation> getPendingReservations(){
        return reservationService.getPendingReservations();
    }

    @PutMapping("/{id}/confirm")
    public Reservation confirmReservation(@PathVariable Long id) {
        return reservationService.confirmReservation(id);
    }

    @PutMapping("/{id}/complete")
    public Reservation completeReservation(@PathVariable Long id) {
        return reservationService.completeReservation(id);
    }

    @PutMapping("/{id}/cancel")
    public Reservation cancelReservation(@PathVariable Long id) {
        return reservationService.cancelReservation(id);
    }

    @GetMapping("/user/{userId}")
    public List<Reservation> getReservationsByUser(@PathVariable Long userId) {
        return reservationService.getReservationsByUser(userId);
    }

    @PutMapping("/{reservationId}/cancel/user/{userId}")
    public Reservation cancelOwnReservation(@PathVariable Long reservationId, @PathVariable Long userId) {
        return reservationService.cancelOwnReservation(reservationId, userId);
    }
}
