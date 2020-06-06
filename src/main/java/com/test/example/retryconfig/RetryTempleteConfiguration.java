package com.test.example.retryconfig;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@EnableRetry
@Configuration
public class RetryTempleteConfiguration {

    @Bean
    public RetryTemplate retryTemplate() {

        // Add Exception list which need to be retried.
        Map<Class<? extends Throwable>, Boolean> retryOnExceptions = new HashMap<>();
        retryOnExceptions.put(RuntimeException.class, true);
        retryOnExceptions.put(IllegalArgumentException.class, true);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(3, retryOnExceptions); // Here 3 is no of retry attemps

        // This define a pause time to retry operation
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(1500); // value should be in milliseconds

        RetryTemplate template = new RetryTemplate();
        template.setRetryPolicy(retryPolicy);
        template.setBackOffPolicy(backOffPolicy);

        return template;
    }
}