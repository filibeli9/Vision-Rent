package com.visionrent.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.visionrent.domain.Car;
import com.visionrent.domain.ImageFile;
import com.visionrent.dto.CarDTO;
import com.visionrent.exception.BadRequestException;
import com.visionrent.exception.ConflictException;
import com.visionrent.exception.ResourceNotFoundException;
import com.visionrent.exception.message.ErrorMessage;
import com.visionrent.mapper.CarMapper;
import com.visionrent.repository.CarRepository;

@Service
public class CarService {

	@Autowired
	private CarRepository carRepository;
	
	@Autowired
	private CarMapper carMapper;
	
	@Autowired
	private ImageFileService imageFileService;
	
	@Autowired
	private ReservationService reservationService;

	public void saveCar(String imageId, CarDTO carDTO) {
		//Does image id exist in image repository?
		ImageFile imageFile =  imageFileService.findImageById(imageId);
		
		//is this image id used by another car?
		Integer usedCarCount = carRepository.findCarCountByImageId(imageFile.getId());
		if(usedCarCount>0) {
			throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
		}
		//mapper
		Car car = carMapper.carDTOToCar(carDTO);
		
		Set<ImageFile> imFiles = new HashSet<>();
		imFiles.add(imageFile);
		
		car.setImage(imFiles);
		
		carRepository.save(car);
	}

	public List<CarDTO> getAllCars() {
		List<Car> cars =  carRepository.findAll();
		List<CarDTO> carDTOs = carMapper.map(cars);
		return carDTOs;
	}

	public Page<CarDTO> findAllWithPage(Pageable pageable) {
		Page<Car> pageCars = carRepository.findAll(pageable);
		Page<CarDTO> pageCarDTOs = pageCars.map(new Function<Car,CarDTO>(){
			@Override
			public CarDTO apply(Car car) {
				return carMapper.carToCarDTO(car);
			}
		});
		return pageCarDTOs;
	}

	public CarDTO findById(Long id) {
		Car car = getCar(id);
		return carMapper.carToCarDTO(car);
		
	}

	public Car getCar(Long id) {
		Car car= carRepository.findCarById(id).orElseThrow(()->
		new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
		return car;
	}

	public void updateCar(Long id, String imageId, CarDTO carDTO) {
		Car car= getCar(id);
		if(car.getBuiltIn()) {
			throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
		}
		
		ImageFile imageFile= imageFileService.findImageById(imageId);
		
		//aim: is the image file with that given image id used with any other cars?
		List<Car> carList= carRepository.findCarsByImageId(imageFile.getId());
		for(Car c: carList) {
			//is the given car's image id same with the found car's image id?
			if(car.getId().longValue()!=c.getId().longValue()) {
				throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
			}
		}
		
		car.setAge(carDTO.getAge());
		car.setAirConditioning(carDTO.getAirConditioning());
		car.setBuiltIn(carDTO.getBuiltIn());
		car.setDoors(carDTO.getDoors());
		car.setFuelType(carDTO.getFuelType());
		car.setLuggage(carDTO.getLuggage());
		car.setModel(carDTO.getModel());
		car.setPricePerHour(carDTO.getPricePerHour());
		car.setSeats(carDTO.getSeats());
		car.setTransmission(carDTO.getTransmission());
		
		car.getImage().add(imageFile);
		
		carRepository.save(car);
	}
	
	public void removeById(Long id) {
		Car car= getCar(id);
		if(car.getBuiltIn()) {
			throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
		}
		//reservation control
		boolean exist= reservationService.existsByCar(car);
		
		if(exist) {
			throw new BadRequestException(ErrorMessage.CAR_USED_BY_RESERVATION_MESSAGE);
		}
		
		carRepository.delete(car);
		
	}

	public Car getCarById(Long id) {
		Car car= carRepository.findById(id).orElseThrow(()->
		new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
		return car;
	}
	
}
