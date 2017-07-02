package com.lancel.bookshelf.config;

import com.lancel.bookshelf.web.WicketApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lancel on 29.06.2017.
 */
@Configuration
public class WicketSpringConfig {

    @Bean
    public WicketApplication wicketSpringApplication() {
        return new WicketApplication();
    }
}
