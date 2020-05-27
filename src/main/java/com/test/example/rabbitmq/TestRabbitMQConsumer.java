package com.test.example.rabbitmq;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.test.example.model.EmployeeModel;

@Component
public class TestRabbitMQConsumer {

	
	@RabbitListener(queues = "${test.rabbitmq.queue}")
	public void recievedMessage(List<EmployeeModel> bean) {
		System.out.println("--> "+bean.size());
		for(EmployeeModel  pb : bean){
			System.out.println("Recieved Message From RabbitMQ: " + pb.getEmpId());
		}
		
	}
	
}
