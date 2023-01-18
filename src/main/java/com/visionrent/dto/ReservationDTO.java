package com.visionrent.dto;

import java.time.LocalDateTime;

import com.visionrent.domain.enums.ReservationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

	private Long id;
	
	private CarDTO car;
	

	private Long userId;
	
	private LocalDateTime pickUpTime;
	
	private LocalDateTime dropOffTime;
	
	private String pickUpLocation;
	
	private String dropOffLocation;
	
	private ReservationStatus status;
	
	private Double totalPrice;
}
