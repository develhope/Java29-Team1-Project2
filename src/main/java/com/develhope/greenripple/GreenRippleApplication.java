package com.develhope.greenripple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
// Specify the package where JPA entities are located
@EntityScan(basePackages = "com.develhope.greenripple.model")
public class GreenRippleApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreenRippleApplication.class, args);
	}

}
