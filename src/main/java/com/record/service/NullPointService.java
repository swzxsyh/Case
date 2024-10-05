package com.record.service;

import com.record.component.ThreadPoolComponent;
import lombok.extern.slf4j.Slf4j;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class NullPointService {

    @Autowired
    private ThreadPoolComponent component;

    @Async("asyncTaskExecutor")
    public void pull() {

        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 10);
        map.put(5, null);
        map.put(null, 10);
        map.put(null, null);

        map.forEach((key, value) -> component.execute(() -> doPull(key, value)));
    }

    private void doPull(Integer source, Integer target) {
        try {
            Integer result = Objects.nonNull(source) ? target : 2;
            log.info("source:{}, target:{} , result:{}", source, target, result);
        } catch (NullPointerException e) {
            Integer result = Objects.nonNull(source) ? Optional.ofNullable(target).orElse(null) : Integer.valueOf(2);
            log.info("source:{}, target:{} , result:{}", source, target, result);

            if (Objects.nonNull(source)) {
                if (Objects.isNull(target)) {
                    log.error("target is null");
                }
            }
        } catch (Exception e) {
            log.error("err, e:", e);
        }
    }
}
