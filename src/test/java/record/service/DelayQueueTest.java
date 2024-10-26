package record.service;

import com.record.component.DelayQueueComponent;
import com.record.model.CustomizeDelayQueue;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import record.CaseApplicationTest;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DelayQueueTest extends CaseApplicationTest {

    @Autowired
    private DelayQueueComponent queueComponent;

    @Test
    public void delayTest() {
        log.info("start delayTest");
        queueComponent.put(10L, 1L, TimeUnit.SECONDS);
        while (true) {
            if (queueComponent.size() > 0) {
                CustomizeDelayQueue object = queueComponent.poll();
                if (Objects.nonNull(object)) {
                    log.info("poll:{}", object);
                    // do something
                    Long id = object.id;
                    log.info("id:{}", id);

                    break;
                }
            }
        }
    }

}
