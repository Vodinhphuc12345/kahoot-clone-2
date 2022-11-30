package com.group2.kahootclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class KahootCloneApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(KahootCloneApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(KahootCloneApplication.class);
    }

}
