package com.record.platform.diffusion;


import com.pushtechnology.diffusion.client.session.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DiffusionSessionManager {

    private final DiffusionFactory diffusion;
    private volatile Session session = null;

    /**
     * 需要添加 static 修饰, 否则对于子类是新instance
     */
    public static final ConcurrentHashMap<Session, Set<String>> SUBSCRIBE_MAP = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Session, Set<String>> SUBSCRIBE_TOPIC = new ConcurrentHashMap<>();

    @Autowired
    public DiffusionSessionManager(DiffusionFactory diffusion) {
        this.diffusion = diffusion;
    }

//    @Scheduled(fixedDelay = 500)
    public void init() {
        // 定时检查会话是否有效
        try {
            session = getSession();
            if (Objects.isNull(session)) {
                log.error("session create error, session is null");
                return;
            }
            if (!isSessionValid(session)) {
                SUBSCRIBE_MAP.remove(session);
                destroy(session);
            }
            // 检查并清理 CLOSED_FAILED 的会话
            Set<Session> failedSession = SUBSCRIBE_MAP.keySet().stream().filter(e -> !isSessionValid(e)).collect(Collectors.toSet());
            failedSession.forEach(this::clear);
        } catch (Exception e) {
            log.error("DiffusionSessionManager error", e);
        }
    }

    public synchronized void clear(Session session) {
        if (Objects.isNull(session) || isSessionValid(session)) {
            return;
        }
        destroy(session);
        if (!isSessionValid(session)) {
            SUBSCRIBE_MAP.remove(session);
            SUBSCRIBE_TOPIC.remove(session);
            session = null;
        }
    }

    public synchronized Session getSession() {
        if (Objects.isNull(session) || session.getState().isClosed() ||
                session.getState() == Session.State.CLOSED_FAILED ||
                session.getState() == Session.State.CLOSED_BY_CLIENT ||
                session.getState() == Session.State.CLOSED_BY_SERVER ||
                session.getState() == Session.State.RECOVERING_RECONNECT) {
            log.info("Get session start, current session state:{}", Objects.isNull(session) ? "null" : session.getState());

            destroy(session);

            // 创建新会话并更新监控
            session = diffusion.session();
            log.info("Get session end, current session state:{}", Objects.isNull(session) ? "null" : session.getState());
        }
        return session;
    }

    private boolean isSessionValid(Session session) {
        return session.getState().equals(Session.State.CONNECTED_ACTIVE) || session.getState().equals(Session.State.CONNECTING);
    }

    public synchronized void destroy(Session session) {
        if (Objects.nonNull(session)) {
            log.info("Destroy, session state:{}", session.getState());
        }
        if (Objects.isNull(session)) {
            return;
        }
        if (!isSessionValid(session)) {
            // Session 有 AutoCloseable，这里尝试只调用close()方法, 让其自动释放
            session.close();
        }
    }

    public boolean containsKey(Session session) {
        return SUBSCRIBE_MAP.containsKey(session);
    }

    public void computeSession(Session session, String topic) {
        SUBSCRIBE_MAP.computeIfAbsent(session, k -> new HashSet<>()).add(topic);
    }

    public boolean ensureSession(Session session, String topic) {
        return SUBSCRIBE_TOPIC.containsKey(session) && SUBSCRIBE_TOPIC.get(session).contains(topic);
    }

    public void computeTopic(Session session, String topic) {
        SUBSCRIBE_TOPIC.computeIfAbsent(session, k -> new HashSet<>()).add(topic);
    }
}