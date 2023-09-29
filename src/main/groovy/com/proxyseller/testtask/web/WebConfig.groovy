package com.proxyseller.testtask.web

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig implements WebMvcConfigurer {

    @Override
    void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home")
        registry.addViewController("/login")
    }
}
