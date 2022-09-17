package com.svj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;

@Service
@EnableScheduling
public class JobService {
    private NotificationService service;

    @Autowired
    public JobService(NotificationService notificationService){
        service= notificationService;
    }

    @Scheduled(cron= "${cron_interval}", zone = "IST")
    public void process(){
        System.out.println("Job started on "+new Date());
        try {
            service.sendDailyReports();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
