package com.tinuvile.config;


import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tinuvile
 * @description http 框架
 * @since 2025/11/15
 */
@Configuration
public class OKHttpClientConfig {

    @Bean
    public OkHttpClient httpClient() {
        return new OkHttpClient();
    }

}
