package com.adnan.rrs.controller;

import com.adnan.rrs.dto.CreateReservationRequest;
import com.adnan.rrs.entity.Reservation;
import com.adnan.rrs.service.ReservationService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public Reservation createReservation(@RequestBody CreateReservationRequest request) {
        return reservationService.createReservation(request);
    }

    @GetMapping("/pending")
    public List<Reservation> getPendingReservations(){
        return reservationService.getPendingReservations();
    }

    @PutMapping("/{id}/confirm")
    public Reservation confirmReservation(@PathVariable Long id) {
        return reservationService.confirmReservation(id);
    }
}
