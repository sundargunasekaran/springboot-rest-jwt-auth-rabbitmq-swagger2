package com.test.example.rabbitmq;

import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.test.example.config.TestConfigProperty;
import com.test.example.model.EmployeeModel;

@Component
public class TestRabbitMQPublisher {

	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	 @Autowired
	 private TestConfigProperty testConfigProperty;
	
	public void send(List<EmployeeModel> bean) {
		amqpTemplate.convertAndSend(testConfigProperty.getExchange(), testConfigProperty.getRoutingkey(), bean);
		System.out.println("Send msg = " + bean);
	    
	}
	
}
