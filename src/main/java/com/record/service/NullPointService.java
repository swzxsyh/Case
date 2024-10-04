package com.record.service;

import com.record.component.ThreadPoolComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class NullPointService {

    @Autowired
    private ThreadPoolComponent component;

    public void pull() {
        component.execute(this::doPull);
    }

    private void doPull() {
        try {
            Integer source = 1;
            Integer result = Objects.nonNull(source) ? getTarget() : 2;
            log.info("result:{}", result);
        } catch (Exception e) {
            log.error("err");
        }
    }

    private Integer getTarget() {
        return null;
    }
}
