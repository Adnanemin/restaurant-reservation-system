package com.adnan.rrs.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.time.LocalDate;


@Getter
@Setter

public class CreateReservationRequest {

    private Long userId;
    private Long tableId;

    private LocalDate reservationDate;
    private LocalTime reservationTime;

    private Integer numberOfGuests;

    private String specialRequest;
}
