package com.et.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@SpringBootApplication
public class DemoApplication {

	@Value("${config.name}")
	String name = "World";

	@RequestMapping("/")
	String home() {
		return "Hello " + name;
	}


	@RequestMapping("/now")
	String now(){
		return "It is: " + (new Date()).toString();
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
