package com.chrisp1985.resultschecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ResultscheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResultscheckerApplication.class, args);
	}

}
