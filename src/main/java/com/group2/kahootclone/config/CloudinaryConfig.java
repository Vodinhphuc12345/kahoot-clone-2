package com.group2.kahootclone.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Value("${cloud.key}")
    String key;
    @Value("${cloud.secret}")
    String secret;
    @Value("${cloud.name}")
    String cloud;

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary;
        cloudinary = Singleton.getCloudinary();
        cloudinary.config.cloudName = cloud;
        cloudinary.config.apiSecret = secret;
        cloudinary.config.apiKey = key;
        return cloudinary;
    }
}
