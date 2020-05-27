package com.test.example.scheduler;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.test.example.model.EmployeeModel;
import com.test.example.rabbitmq.TestRabbitMQPublisher;
import com.test.example.service.TestDataService;
import com.test.example.service.TestEmailService;


@Component
public class ScheduledTasks {

	@Value("${scheduling.job.cron}")
	String cornJob;
	
	@Autowired
	private TestRabbitMQPublisher testRabbitMqPublisher;
	
	@Autowired
	private TestEmailService testEmailService;
	
	@Autowired
	private TestDataService testDataService ;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm:ss");

	//@Scheduled(fixedRate = 10000) // 10sec
	//@Scheduled(cron = "#{getCronSyntax}")
	//@Scheduled(fixedDelayString = "#{@getCronSyntax}") //w
	//@Scheduled(cron = "${scheduling.job.cron}")
	@Scheduled(cron="#{@getCronValue}")
    @Retryable(backoff = @Backoff(delay = 5000), maxAttempts = 3)
	public void performTask() throws Exception {

		System.out.println("Regular task performed at "
				+ dateFormat.format(new Date())+"--"+cornJob);
		try {
			List<EmployeeModel>  bean = testDataService.getEmployeeInfo("3000");
			if(bean.size() > 0){
				System.out.println(bean.size());
				testRabbitMqPublisher.send(bean);
				testEmailService.sendMail("gsundar.rvs@gmail.com", "propbeantest mail", "test mail");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*@Scheduled(initialDelay = 1000, fixedRate = 10000)
	public void performDelayedTask() {

		System.out.println("Delayed Regular task performed at "
				+ dateFormat.format(new Date()));

	}*/

	//@Scheduled(cron = "*/5 * * * * *")
	/*public void performTaskUsingCron() {

		System.out.println("Regular task performed using Cron at "
				+ dateFormat.format(new Date()));

	}*/
	
/*	@Scheduled(cron="#{@getCronValue}")
    @Retryable(backoff = @Backoff(delay = 5000), maxAttempts = 3)
	 //@Retryable(backoff = @Backoff(delay = 5000, maxDelay = 101), maxAttempts = 3)
    public void transferData() throws Exception {

		System.out.println("Regular task performed at "+ dateFormat.format(new Date()));
		List<EmployeeModel>  bean = testDataService.getEmployeeInfo("3000");
        //throw new Exception();
    }*/

    @Recover
    public void recover(Exception exception) {
        System.out.println("Recovering from a service down"+ dateFormat.format(new Date()));
        testEmailService.sendMail("gsundar.rvs@gmail.com", "schedule failed after 3 retry "+dateFormat.format(new Date()), "Failure");
    }
}
