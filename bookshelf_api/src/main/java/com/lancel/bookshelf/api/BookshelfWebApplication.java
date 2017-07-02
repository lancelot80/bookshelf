package com.lancel.bookshelf.api;

import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Created by lancel on 29.06.2017.
 */
@EntityScan("com.lancel.bookshelf.core.entities")
@SpringBootApplication
public class BookshelfWebApplication {

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(BookshelfWebApplication.class, args);
    }

    @Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
        registration.addUrlMappings("/console/*");
        return registration;
    }
}
