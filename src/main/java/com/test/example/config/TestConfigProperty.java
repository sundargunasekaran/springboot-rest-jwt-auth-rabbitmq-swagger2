package com.test.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class TestConfigProperty {
	
	 @Value("${email.host}")
	 private String host;

	 @Value("${email.port}")
	 private Integer port;
	    
	 @Value("${email.username}")
	 private String username;
	
	 @Value("${email.password}")
	 private String password;
	 
	@Value("${test.rabbitmq.queue}")
	private String queueName;

	@Value("${test.rabbitmq.exchange}")
	private String exchange;

	@Value("${test.rabbitmq.routingkey}")
	private String routingkey;
	
	@Value("${scheduling.job.cron}")
	private String cornJob;
	

    @Value("${jwt.expire.minutes}")
    private Long expireDuration;

    @Value("${jwt.token.secret}")
    private String secretKey;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getRoutingkey() {
		return routingkey;
	}

	public void setRoutingkey(String routingkey) {
		this.routingkey = routingkey;
	}

	public String getCornJob() {
		return cornJob;
	}

	public void setCornJob(String cornJob) {
		this.cornJob = cornJob;
	}

	public Long getExpireDuration() {
		return expireDuration;
	}

	public void setExpireDuration(Long expireDuration) {
		this.expireDuration = expireDuration;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	 
	 
	 
	 
	 
}
