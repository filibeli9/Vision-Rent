package com.visionrent.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.visionrent.domain.Car;
import com.visionrent.domain.Reservation;
import com.visionrent.domain.User;
import com.visionrent.domain.enums.ReservationStatus;
import com.visionrent.dto.ReservationDTO;
import com.visionrent.dto.request.ReservationRequest;
import com.visionrent.dto.request.ReservationUpdateRequest;
import com.visionrent.exception.BadRequestException;
import com.visionrent.exception.ResourceNotFoundException;
import com.visionrent.exception.message.ErrorMessage;
import com.visionrent.mapper.ReservationMapper;
import com.visionrent.repository.ReservationRepository;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private ReservationMapper reservationMapper;

	//***********CREATE
	public void createReservation(ReservationRequest reservationRequest, User user, Car car) {
		checkReservationTimeIsCorrect(reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());
		
		boolean carStatus = checkCarAvailability(car, reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());
		
		Reservation reservation = reservationMapper.reservationRequestToReservation(reservationRequest);
		
		if(carStatus) {
			reservation.setStatus(ReservationStatus.CREATED);
		}else {
			throw new BadRequestException(ErrorMessage.CAR_NOT_AVAILABLE_MESSAGE);
		}
		reservation.setCar(car);
		reservation.setUser(user);
		
		Double totalPrice = getTotalPrice(car, reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());
		
		reservation.setTotalPrice(totalPrice);
		
		reservationRepository.save(reservation);
		
	}
	
	public void checkReservationTimeIsCorrect(LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
		LocalDateTime now = LocalDateTime.now();
		if(pickUpTime.isBefore(now)) {//is reservation time is before current time
			throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);
		}
		//is pick up time same with drop off time?
		boolean isEqual = pickUpTime.isEqual(dropOffTime)?true:false;
		//is pick up time is before drop off time?
		boolean isBefore = pickUpTime.isBefore(dropOffTime)?true:false;
		
		if(isEqual||!isBefore) {
			throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);
		}
	}
	
	public boolean checkCarAvailability(Car car, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
		List<Reservation> existReservations = getConflictReservations(car, pickUpTime, dropOffTime);
		return existReservations.isEmpty();
	}
	
	public Double getTotalPrice(Car car, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
		//to get minutes between pick up time and drop off time
		Long minutes = ChronoUnit.MINUTES.between(pickUpTime, dropOffTime);
		double hours = Math.ceil(minutes/60.0);
		return hours*car.getPricePerHour();
	}
	
	private List<Reservation> getConflictReservations(Car car, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
		if(pickUpTime.isAfter(dropOffTime)) {
			throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);
		}
		ReservationStatus[] status = {ReservationStatus.CANCELED, ReservationStatus.DONE};
		
		List<Reservation> existReservations = reservationRepository.checkCarStatus(car.getId(), pickUpTime, dropOffTime, status);
		
		return existReservations;
		
	}

	public List<ReservationDTO> getAllReservations() {
		
		List<Reservation> reservations = reservationRepository.findAll();
		
		return reservationMapper.map(reservations);
	}

	public Page<ReservationDTO> getReservationPage(Pageable pageable) {
		Page<Reservation> reservationPage = reservationRepository.findAll(pageable);
		//Page<ReservationDTO> reservationDTOPage= reservationPage.map(reservationMapper::reservationToReservationDTO);
		return getReservationDTOPage(reservationPage);
		
	}
	
	private Page<ReservationDTO> getReservationDTOPage(Page<Reservation> reservationPage){
		
		Page<ReservationDTO> reservationDTOPage = reservationPage.map(new Function<Reservation,ReservationDTO>(){
			@Override
			public ReservationDTO apply(Reservation reservation) {
				return reservationMapper.reservationToReservationDTO(reservation);
			}
		});
		
		return reservationDTOPage;
	}

	public void updateReservation(Long reservationId, Car car, ReservationUpdateRequest reservationUpdateRequest) {
		Reservation reservation = getByID(reservationId);
		
		//if reservation status is cancelled or done, update won't be done
		if(reservation.getStatus().equals(ReservationStatus.CANCELED)||reservation.getStatus().equals(ReservationStatus.DONE)) {
			throw new BadRequestException(ErrorMessage.RESERVATION_STATUS_CANT_CHANGE_MESSAGE);
		}
		// if the reservation is going to be done or cancelled, not to check pick up time and drop off time 
		//but if status is created, time check must be done 
		if(reservationUpdateRequest.getStatus()!=null && reservationUpdateRequest.getStatus().equals(ReservationStatus.CREATED)) {
			checkReservationTimeIsCorrect(reservationUpdateRequest.getPickUpTime(), reservationUpdateRequest.getDropOffTime());
			
			//conflict check
			List<Reservation> conflictReservations = getConflictReservations(car, reservationUpdateRequest.getPickUpTime(), 
															reservationUpdateRequest.getDropOffTime());
			if(!conflictReservations.isEmpty()) {
				if(!(conflictReservations.size()==1 && conflictReservations.get(0).getId().equals(reservationId)))
				{
					throw new BadRequestException(ErrorMessage.CAR_NOT_AVAILABLE_MESSAGE);
				}	
			}
			if(!(conflictReservations.size()==1 && conflictReservations.get(0).getId().equals(reservationId))) {
			
				throw new BadRequestException(ErrorMessage.CAR_NOT_AVAILABLE_MESSAGE);
			}
			
			//total price
			Double totalPrice = getTotalPrice(car, reservationUpdateRequest.getPickUpTime(), reservationUpdateRequest.getDropOffTime());
			
			reservation.setTotalPrice(totalPrice);
			reservation.setCar(car);
		}
		reservation.setPickUpTime(reservationUpdateRequest.getPickUpTime());
		reservation.setDropOffTime(reservationUpdateRequest.getDropOffTime());
		reservation.setPickUpLocation(reservationUpdateRequest.getPickUpLocation());
		reservation.setDropOffLocation(reservationUpdateRequest.getDropOffLocation());
		reservation.setStatus(reservationUpdateRequest.getStatus());
		
		reservationRepository.save(reservation);
		
	}
	
	public Reservation getByID(Long id) {
		Reservation reservation = reservationRepository.findById(id).orElseThrow(()->
		new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
		return reservation;
	}

	public ReservationDTO getReservationDTO(Long id) {
		
		Reservation reservation = getByID(id);
		
		return reservationMapper.reservationToReservationDTO(reservation);
	}

	public Page<ReservationDTO> findReservationsPageByUser(User user, Pageable pageable) {
		
		Page<Reservation> reservationPage = reservationRepository.findAllByUser(user,pageable);
		
		return getReservationDTOPage(reservationPage);
	}

	public ReservationDTO findByIdAndUser(Long id, User user) {
		Reservation reservation = reservationRepository.findByIdAndUser(id, user).orElseThrow(()->
		new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
		return reservationMapper.reservationToReservationDTO(reservation);
	}

	public void removeById(Long id) {
		
		boolean exist = reservationRepository.existsById(id);
		
		if(!exist) {
			throw new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id));
		}
		
		reservationRepository.deleteById(id);
		
	}

	public boolean existsByCar(Car car) {
		return reservationRepository.existsByCar(car);
	}

	public boolean existsByUser(User user) {
		return reservationRepository.existsByUser(user);
	}
	
}
