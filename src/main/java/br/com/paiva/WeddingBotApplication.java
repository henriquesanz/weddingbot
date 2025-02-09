package br.com.paiva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.paiva")
public class WeddingBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeddingBotApplication.class, args);
	}

}
