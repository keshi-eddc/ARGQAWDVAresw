package com.edmi.jobs;

import com.edmi.service.service.LinkedinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LinkedinScheduledTasks {

    @Autowired
    private LinkedinService linkedinService;

    /*@Scheduled(cron = "0 05 10 * * ?")*/
    public void importLinkedinPageDatasToBase() {
        linkedinService.readLinkedinFilesToBase();
    }


}
