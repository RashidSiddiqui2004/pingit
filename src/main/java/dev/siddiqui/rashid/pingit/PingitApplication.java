package dev.siddiqui.rashid.pingit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PingitApplication {

	public static void main(String[] args) {
		SpringApplication.run(PingitApplication.class, args);
	}

}
