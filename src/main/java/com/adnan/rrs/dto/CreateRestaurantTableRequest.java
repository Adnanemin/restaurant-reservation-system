package com.adnan.rrs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRestaurantTableRequest {

    private Integer tableNumber;

    private Integer capacity;
    
    private String location;


}
