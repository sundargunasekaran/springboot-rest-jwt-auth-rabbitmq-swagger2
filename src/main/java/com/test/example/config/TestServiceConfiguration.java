package com.test.example.config;


import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.test.example.service.TestDataService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages="com.test.example")
/*@PropertySource("file:${catalina_home}/conf/test-service.properties") */
@PropertySource("file:E:/apache-tomcat-7.0.34-windows-x64/apache-tomcat-7.0.34/conf/test-service.properties") 
public class TestServiceConfiguration {

	 @Autowired
	 private Environment environment;
	 
	 @Value("${email.host}")
	 private String host;

	 @Value("${email.port}")
	 private Integer port;
	    
	 @Value("${email.username}")
	 private String username;
	
	 @Value("${email.password}")
	 private String password;
	    
	 @Autowired
	 private TestDataService testDataService ;
	 
	 @Bean
	 public DataSource getDataSource() {
	        /*DriverManagerDataSource bds = new DriverManagerDataSource();
			bds.setDriverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"));
			bds.setUrl(environment.getRequiredProperty("spring.datasource.url"));
			bds.setUsername(environment.getRequiredProperty("spring.datasource.username"));
			bds.setPassword(environment.getRequiredProperty("spring.datasource.password"));
			return bds;*/
		 
		 	HikariConfig hikariConfig = new HikariConfig();
		    hikariConfig.setDriverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"));
		    hikariConfig.setJdbcUrl(environment.getRequiredProperty("spring.datasource.url")); 
		    hikariConfig.setUsername(environment.getRequiredProperty("spring.datasource.username"));
		    hikariConfig.setPassword(environment.getRequiredProperty("spring.datasource.password"));

		    hikariConfig.setMaximumPoolSize(5);
		  //  hikariConfig.setConnectionTestQuery("SELECT 1 from dual");   //oracle
		    hikariConfig.setConnectionTestQuery("SELECT 1");   //mysql
		    hikariConfig.setPoolName("springHikariCP");

		    hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true");
		    hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
		    hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
		    hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true");

		    HikariDataSource dataSource = new HikariDataSource(hikariConfig);
		 
		    return dataSource;
		}
	 
	 
	@Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setPassword(password);
        javaMailSender.setUsername(username);
        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "false");
        return properties;
    }
    
    @Bean
	public String getCronValue() {
	    return testDataService.getScheduledCornExpression();
	}
}
