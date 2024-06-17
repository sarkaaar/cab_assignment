package com.abdullah.coding.challenge.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SearchBean {
    private String custName;
    private String vehileType;
    private LocalDateTime requestTime;
}
