package com.futesat.hexagonal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = {
		"com.futesat.hexagonal.courses",
		"com.futesat.hexagonal.notifications",
		"com.futesat.hexagonal.shared",
		"com.futesat.hexagonal.infrastructure"
})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
