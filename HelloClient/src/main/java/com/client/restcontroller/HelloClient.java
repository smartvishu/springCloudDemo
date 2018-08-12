package com.client.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/bookings")
public class HelloClient {

	@Autowired
	DiscoveryClient discoveryClient;


	@GetMapping()
	public String bookings() {
		String url = discoveryClient.getInstances("get-booking").get(0).getUri().toString();

		RestTemplate restTemplate=new RestTemplate();
		
		return restTemplate.getForObject(url+"/booking", String.class);

	}
}
