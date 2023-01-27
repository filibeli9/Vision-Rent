package com.visionrent.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionrent.domain.Car;
import com.visionrent.domain.Reservation;
import com.visionrent.domain.User;
import com.visionrent.exception.message.ErrorMessage;
import com.visionrent.report.ExcelReporter;

@Service
public class ReportServica {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private ReservationService reservationService;

	//********USER
	public ByteArrayInputStream getUserReport() {
		List<User> users = userService.getUsers();
		
		try {
			return ExcelReporter.getUserExcelReport(users);
		} catch (IOException e) {
			throw new RuntimeException(ErrorMessage.EXCEL_REPORT_ERROR_MESSAGE);
		}
	}

	public ByteArrayInputStream getCarReport() {
		List<Car> cars = carService.getAllCar();
		try {
			return ExcelReporter.getCarExcelReport(cars);
		} catch (IOException e) {
			throw new RuntimeException(ErrorMessage.EXCEL_REPORT_ERROR_MESSAGE);
		}
	}

	public ByteArrayInputStream getReservationReport() {
		List<Reservation> reservations = reservationService.getAll();
		try {
			return ExcelReporter.getReservationExcelReport(reservations);
		} catch (IOException e) {
			throw new RuntimeException(ErrorMessage.EXCEL_REPORT_ERROR_MESSAGE);
		}
	}
}
