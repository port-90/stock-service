package com.port90.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"com.port90.core", "com.port90.stockdomain"})
@EnableJpaRepositories(basePackages = {"com.port90.core", "com.port90.stockdomain"})
@EntityScan(basePackages = {"com.port90.core", "com.port90.stockdomain"})
@SpringBootApplication
public class CoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

}
