package com.record.task;

import com.record.service.NullPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class JobTask {

    @Autowired
    private NullPointService nullPointService;

    /**
     * 10 min
     */
//    @PostConstruct
//    @Scheduled(cron = "0 0/10 * * * ?")
    public void pullTask(){
        nullPointService.pull();
    }
}
