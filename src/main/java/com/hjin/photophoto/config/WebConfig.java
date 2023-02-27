package com.hjin.photophoto.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig  implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080", "http://ec2-54-180-92-252.ap-northeast-2.compute.amazonaws.com",
                        "http://photophoto.me",
                        "http://www.photophoto.me",
                        "http://localhost:3000"
                )
                .allowedOriginPatterns("*")
                .allowedMethods("*")
        ;

    }
}
