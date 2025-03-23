package com.greenripple.green_ripple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
// Specify the package where JPA entities are located
@EntityScan(basePackages = "com.greenripple.green_ripple.model")
public class GreenRippleApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreenRippleApplication.class, args);
	}

}
