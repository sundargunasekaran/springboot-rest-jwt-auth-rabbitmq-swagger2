package com.test.example.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableRetry
@ComponentScan(basePackages="com.test.example")
@PropertySource("file:E:/apache-tomcat-7.0.34-windows-x64/apache-tomcat-7.0.34/conf/test-service.properties")
public class SpringBootHelloWorldApplication {
	
	public static void main(String[] args) {

		/*SpringApplication.run(
				new Object[] { SpringBootHelloWorldApplication.class }, args);*/
		SpringApplication.run(SpringBootHelloWorldApplication.class, args);
		System.out.println("---> Spring Boot 2.0.5 application started!!!!! now you can enjoy");
	}
}