package com.ejercicio.ms_caceres_anculle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsCaceresAnculleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCaceresAnculleApplication.class, args);
	}

}
