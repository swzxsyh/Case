package record.service;

import com.record.component.ClientPoolComponent;
import com.record.service.NullPointService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import record.CaseApplicationTest;


@Slf4j
public class NullPointServiceTest extends CaseApplicationTest {

    @Autowired
    private NullPointService nullPointService;

    @Autowired
    private ClientPoolComponent component;

    @Test
    public void pullNullPointTest() {
        nullPointService.pull();
    }

    @Test
    public void priorityTaskTest() {
        component.execute(() -> log.info("low task"), 2);
        component.execute(() -> log.info("high task"), 10);
        component.execute(() -> log.info("middle task"), 5);
        component.execute(() -> log.info("high task"), 10);
        component.execute(() -> log.info("high task"), 10);
    }
}
