package com.cma.systemc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SystemCManufacturingDemoApplication extends SpringBootServletInitializer {
    
      @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SystemCManufacturingDemoApplication.class);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(SystemCManufacturingDemoApplication.class, args);
    }
}
