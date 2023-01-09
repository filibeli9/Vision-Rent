package com.visionrent.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visionrent.dto.CarDTO;
import com.visionrent.dto.response.ResponseMessage;
import com.visionrent.dto.response.VRResponse;
import com.visionrent.service.CarService;

@RestController
@RequestMapping("/car")
public class CarController {

	@Autowired
	private CarService carService;
	
	//*****save-car
	@PostMapping("/admin/{imageId}/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<VRResponse> saveCar(@PathVariable String imageId, @Valid @RequestBody CarDTO carDTO){
		carService.saveCar(imageId, carDTO);
		VRResponse response = new VRResponse(ResponseMessage.CAR_SAVED_RESPONSE,true);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	//*****Get All Cars
	@GetMapping("/visitors/all")
	public ResponseEntity<List<CarDTO>> getAllCars(){
		List<CarDTO> carDTOs = carService.getAllCars();
		return ResponseEntity.ok(carDTOs);
	}
	
	//***Get All Cars with Page
	@GetMapping("/visitors/pages")
	public ResponseEntity<Page<CarDTO>> getAllCarsWithPage(@RequestParam("page") int page,
																				@RequestParam("size") int size, 
																				@RequestParam("sort") String prop,
																				@RequestParam(value = "direction", 
																				required = false, 
																				defaultValue = "DESC") Direction direction){
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
		Page<CarDTO> pageDTO = carService.findAllWithPage(pageable);
		return ResponseEntity.ok(pageDTO);
	}
	
	//*****************get Car by id
	@GetMapping("/visitors/{id}")
	public ResponseEntity<CarDTO> getCarById(@PathVariable Long id){
		CarDTO carDTO = carService.findById(id);
		return ResponseEntity.ok(carDTO);
	}
	
	//**********update Car with Image ID************
	@PutMapping("/admin/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<VRResponse> updateCar(@RequestParam("id") Long id,
												@RequestParam("imageId") String imageId,
												@Valid @RequestBody CarDTO carDTO){
		carService.updateCar(id,imageId, carDTO);
		VRResponse response = new VRResponse(ResponseMessage.CAR_UPDATED_RESPONSE,true);
		return ResponseEntity.ok(response);
	}
	
	//*******************DELETE
	@DeleteMapping("/admin/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<VRResponse> deleteCar(@PathVariable Long id){
		carService.removeById(id);
		VRResponse response = new VRResponse(ResponseMessage.CAR_DELETE_RESPONSE,true);
		return ResponseEntity.ok(response);
	}
}
