package com.record.platform.websocket;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class WebSocketConfig implements InitializingBean {

    private static final String URL = "wss://test.url.com";


    @Bean
    public MockWebSocketClient oddsWebSocketClient(){
         URI uri = URI.create(URL);
         return new MockWebSocketClient(uri);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
