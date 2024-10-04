package com.record.config;

import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;

@Configuration
public class UndertowConfiguration implements WebServerFactoryCustomizer<UndertowServletWebServerFactory>{

    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {

            WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();

            // 设置合理的参数
            webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(true, 8192));

            deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", webSocketDeploymentInfo);
        });
    }
}