package com.techprimers.cloud;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking")
public class HelloResource {

	@GetMapping
	public ResponseEntity<Booking> booking() {
		Booking res = new Booking();
		res.setBookingStatus("Done");
		res.setName("Vishnu");
		ResponseEntity<Booking> response = new ResponseEntity<>(res, HttpStatus.OK);
		return response;
	}
}
