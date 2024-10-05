package record.service;

import com.record.component.ClientPoolComponent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import record.CaseApplicationTest;

import java.util.concurrent.*;

@Slf4j
public class ExecutorsComponentTest extends CaseApplicationTest {

    @Autowired
    private ClientPoolComponent component;

    @Test
    public void priorityTaskTest() {
        component.execute(() -> log.info("low task"), 2);
        component.execute(() -> log.info("high task"), 10);
        component.execute(() -> log.info("middle task"), 5);
        component.execute(() -> log.info("high task"), 10);
        component.execute(() -> log.info("high task"), 10);
    }


    @Test
    public void executeExceptionTest() {
        log.info("start executeExceptionTest");
        component.execute(this::executeError, 0);
        log.info("end executeExceptionTest");
    }

    private void executeError() {
        throw new RuntimeException("execute exception");
    }

    @Test
    public void submitExceptionTest() throws Exception {
        log.info("start submitExceptionTest");
        Future<Integer> submit = component.submit(this::submitError, 0);
        log.info("submit submitExceptionTest");
        Integer result = submit.get();
        log.info("end submitExceptionTest, result:{}", result);
    }

    private int submitError() {
        return 1 / 0;
    }
}
