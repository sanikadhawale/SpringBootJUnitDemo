package com.example.Junitdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestControllerClass {
	
	@GetMapping("/hello")
	public String helloWorld() {
		
		return "Hello World!!!!";
	}

}
