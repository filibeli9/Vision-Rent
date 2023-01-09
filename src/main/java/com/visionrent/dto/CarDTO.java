package com.visionrent.dto;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

	private Long id;
	
	@Size(max= 30, message= "Size is exceeded")
	@NotBlank(message = "Please provide car model")
	private String model;
	
	@NotNull(message = "Please provide car door info")
	private Integer doors;
	
	@NotNull(message = "Please provide car seat info")
	private Integer seats;
	
	@NotNull(message = "Please provide car luggage info")
	private Integer luggage;
	
	@NotBlank(message = "Please provide car transmission info")
	private String transmission;
	
	@NotNull(message = "Please provide car air condition info")
	private Boolean airConditioning;
	
	@NotNull(message = "Please provide car age info")
	private Integer age;
	
	@NotNull(message = "Please provide car price per hour info")
	private Double pricePerHour;
	
	@NotBlank(message = "Please provide car fuel type info")
	private String fuelType;
	
	//for Integer & Boolean--> @NotNull, for String --> can be either @NotBlank or @NotNull
	
	private Boolean builtIn = false;
	
	
	private Set<String> image;// pojo claas'indan gelecegi icin image file'lari almama gerek yok sadece isim olarak yeterli?
}
