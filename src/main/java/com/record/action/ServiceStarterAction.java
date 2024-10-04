package com.record.action;

import com.record.platform.diffusion.DiffusionSessionManager;
import com.record.platform.websocket.MockWebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceStarterAction {

    @Autowired
    private MockWebSocketClient mockWebSocketClient;

    @Autowired
    private DiffusionSessionManager diffusionSessionManager;

    //    @PostConstruct
    public void start() {
        mockWebSocketClient.init();
        diffusionSessionManager.init();
    }
}
