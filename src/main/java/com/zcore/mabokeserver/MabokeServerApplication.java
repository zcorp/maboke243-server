package com.zcore.mabokeserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class })
public class MabokeServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(MabokeServerApplication.class, args);
	}
}
