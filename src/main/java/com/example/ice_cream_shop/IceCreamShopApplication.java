package com.example.ice_cream_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication(scanBasePackages = "com.example.icecreamshop")
@EnableKafka
public class IceCreamShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(IceCreamShopApplication.class, args);
	}

}
