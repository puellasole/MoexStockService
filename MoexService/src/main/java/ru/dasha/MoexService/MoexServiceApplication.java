package ru.dasha.MoexService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MoexServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoexServiceApplication.class, args);
	}

}
