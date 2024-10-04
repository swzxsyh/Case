package com.record.action;

import com.record.websocket.MockWebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceStarterAction {

    @Autowired
    private MockWebSocketClient mockWebSocketClient;

//    @PostConstruct
    public void start() {
        mockWebSocketClient.init();
    }
}
