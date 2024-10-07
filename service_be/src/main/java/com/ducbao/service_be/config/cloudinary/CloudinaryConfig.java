package com.ducbao.service_be.config.cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Value("${spring.cloudinary.cloudName}")
    private String CLOUD_NAME;
    @Value("${spring.cloudinary.apiKey}")
    private String API_KEY;
    @Value("${spring.cloudinary.apiSecret}")
    private String API_SECRET = "CLOUD_API_SECRET_YOUR";

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        return new Cloudinary(config);
    }
}
