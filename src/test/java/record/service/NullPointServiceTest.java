package record.service;

import com.record.service.NullPointService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import record.CaseApplicationTest;


@Slf4j
public class NullPointServiceTest extends CaseApplicationTest {

    @Autowired
    private NullPointService nullPointService;


    @Test
    public void pullNullPointTest() {
        nullPointService.pull();
    }
}