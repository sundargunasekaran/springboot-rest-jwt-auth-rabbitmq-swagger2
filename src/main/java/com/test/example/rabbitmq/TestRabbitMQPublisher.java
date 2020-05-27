package com.test.example.rabbitmq;

import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.test.example.model.EmployeeModel;

@Component
public class TestRabbitMQPublisher {

	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Value("${test.rabbitmq.exchange}")
	private String exchange;
	
	@Value("${test.rabbitmq.routingkey}")
	private String routingkey;	
	
	public void send(List<EmployeeModel> bean) {
		amqpTemplate.convertAndSend(exchange, routingkey, bean);
		System.out.println("Send msg = " + bean);
	    
	}
	
}
