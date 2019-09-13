package com.acme.acmetrade.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.IdGenerator;
import org.springframework.util.JdkIdGenerator;

/**
 * Created by volen on 2017-07-30.
 */
@Configuration
// to load bean definition from external xml file, uncomment below
// @ImportResource("classpath:envConfig.xml")
public class AppConfig {
    @Autowired
    private ApplicationContext ctx;

    @Bean
    public IdGenerator idGenerator() {
      return new JdkIdGenerator();
    }

}
