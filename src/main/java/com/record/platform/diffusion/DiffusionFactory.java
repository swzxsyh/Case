package com.record.platform.diffusion;

import com.pushtechnology.diffusion.client.Diffusion;
import com.pushtechnology.diffusion.client.session.Session;
import com.pushtechnology.diffusion.client.session.reconnect.ReconnectionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class DiffusionFactory {

    /**
     * 多例模式，使用原因：当出现 CLOSED_FAILED 状态时，无法恢复
     * SDK ##: The session has lost its connection to a server and could not be recovered.
     * <р>
     * 但为避免 多处创建 或OOM 问题还是全局Singleton模式
     *
     * @return Session
     */
    public Session session() {
        //session有 Autocloseable，这里尝试只调用cLose()方法，让其自动释放
        return Diffusion.sessions()
                .principal("username")
                .password("pwd")
                .listener((currentSession, oldState, newstate) -> log.info("Session state changed from {} to {}", oldState, newstate))
                .listener((session1, reason, thr) -> log.warn("Session warning: {}, e:{}", reason, thr))
                .maximumQueueSize(2)
                .noReconnection().reconnectionTimeout(0)
                .errorHandler((errorSession, errorReason) -> {
                    //session有 Autocloseable，这里尝试只调用cLose()方法，让其自动释放
                    errorSession.close();
                    errorSession = null;
                    log.error("Session error: {}", errorReason);
                })
                .reconnectionStrategy(ReconnectionStrategy.ReconnectionAttempt::abort)
                .open("wss://xxx");
    }
}
