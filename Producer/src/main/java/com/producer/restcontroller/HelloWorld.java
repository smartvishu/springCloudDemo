package com.producer.restcontroller;

import java.sql.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.producer.TokenResponse;

@RestController
@RequestMapping("/oauth")
public class HelloWorld {
	
	@GetMapping()
	public String home() {
			System.out.println("Token Validated Successfully");
			return "Validated Successfully";
	}

	@GetMapping({"/check_token/{token}"})
	public ResponseEntity<TokenResponse> check_token(@PathVariable  String token) {
		TokenResponse res = new TokenResponse();
		if (null != token & token.contains("right")) {
			res = new TokenResponse();
			Date date = new Date(2018, 12, 10);
			res.setClientIdentifier("clientIdentifier");
			res.setExpiryTime(date);
			System.out.println("Token Validated Successfully");
			return new ResponseEntity<TokenResponse>(res, HttpStatus.OK);
		}else{
			System.out.println("Token Validation failed");
			return new ResponseEntity<TokenResponse>(res, HttpStatus.BAD_REQUEST);
		}
	}
}
