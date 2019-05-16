package com.malt.commission.config;

import com.malt.commission.aop.logging.LoggingAspect;
import com.malt.commission.util.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    @Profile(Constants.DEV_PROFILE)
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
