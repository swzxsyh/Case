package com.record.websocket;

import com.record.component.ThreadPoolComponent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;

@Slf4j
public class MockWebSocketClient extends WebSocketClient {

    @Autowired
    private ThreadPoolComponent component;

    public MockWebSocketClient(URI serverUri) {
        super(serverUri);
    }

//    @PostConstruct
    public void init() {
        try {
            this.connectBlocking();
        } catch (InterruptedException e) {
            log.error("init err");
        }
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        String handshake = "test_hi";
        super.send(handshake);
        String subscribe = "test_subscribe";
        super.send(subscribe);
    }

    @Override
    public void onMessage(String message) {
        component.execute(() -> {
            this.executeMessage(message);
        });
    }

    private void executeMessage(String message) {
        log.info(message);
    }


    @Override
    public void onClose(int i, String s, boolean b) {
        try {
            super.reconnectBlocking();
        } catch (InterruptedException e) {
            log.error("wss on close, e:", e);
        }
    }

    @Override
    public void onError(Exception e) {
    }

}
