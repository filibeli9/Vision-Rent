package com.visionrent.dto.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss")
	@NotNull(message = "Please provide the pick up time of the reservation")
	private LocalDateTime pickUpTime;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss")
	@NotNull(message = "Please provide the drop off time of the reservation")
	private LocalDateTime dropOffTime;
	
	@Size(max=150,message= "Pick up location must be max {max} chars")
	@NotBlank(message = "Please provide the pick up location of the reservation")
	private String pickUpLocation;
	
	@Size(max=150,message= "Drop off location must be max {max} chars")
	@NotBlank(message = "Please provide the drop off location of the reservation")
	private String dropOffLocation;

}
