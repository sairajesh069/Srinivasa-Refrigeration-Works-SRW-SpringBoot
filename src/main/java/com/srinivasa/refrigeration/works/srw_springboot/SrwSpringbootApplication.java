package com.srinivasa.refrigeration.works.srw_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SrwSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SrwSpringbootApplication.class, args);
	}

}