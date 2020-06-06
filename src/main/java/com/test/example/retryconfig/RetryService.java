package com.test.example.retryconfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
public class RetryService {

    Logger logger= LogManager.getLogger(getClass());
    
    @Autowired
    private RetryTemplate retryTemplate;

    // Define the retry operation with return data type and the Exception which is going to throw.
    final RetryCallback<String, Exception> retryCallback = new RetryCallback<String, Exception>() {
        public String doWithRetry(RetryContext context) throws Exception {
            System.out.println("retryCallback");
            return "RETRIED";
            
        }
    };

    // Define the recovery functionality here.
    final RecoveryCallback<String> recoveryCallback = new RecoveryCallback<String>() {
        public String recover(RetryContext context) throws Exception {
            System.out.println("recoveryCallback");
            return "RECOVERED";
        }
    };
    
    public void executeService(){
        try {
            logger.info("retryTemplate execute start");
            // Here i'm using the return type as String. you can use what ever object as a return type
            String status= retryTemplate.execute(retryCallback, recoveryCallback);
            logger.info("Response-"+status);
            logger.info("retryTemplate execute end");
            
        } catch (Exception e) {
            logger.error("Error occoured", e);
        }
    }

}