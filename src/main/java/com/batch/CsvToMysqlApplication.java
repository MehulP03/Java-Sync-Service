package com.batch;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
//@EnableScheduling
public class CsvToMysqlApplication {
//	@Scheduled(fixedRate = 2000)
	public static void main(String[] args) {
		SpringApplication.run(CsvToMysqlApplication.class, args);
	}
}
