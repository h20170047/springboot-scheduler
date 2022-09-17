package com.svj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

@Service
public class NotificationService {

    private reportService reportServiceInstance;
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${report.send.email.toList}")
    private String toEmails;

    @Autowired
    public NotificationService( reportService reportServiceObj, JavaMailSender javaMailSender){
        this.reportServiceInstance= reportServiceObj;
        this.javaMailSender= javaMailSender;
    }

    public String sendDailyReports() throws MessagingException, IOException {
        MimeMessage mimeMessage= javaMailSender.createMimeMessage();
        MimeMessageHelper helper= new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(sender);
        helper.setTo(toEmails.split(","));
        helper.setSubject("List of orders_"+new Date());
        helper.setText("Hello,\nPlease find the attachment for today's order received!");

//        byte[] report= reportService.generateReport();
//        ByteArrayResource content= new ByteArrayResource(report);
//        helper.addAttachment(new Date()+"_orders.xlsx", content);

        buildReport(helper);

        javaMailSender.send(mimeMessage);
        return "Mail sent successfully with attachment";
    }

    private void buildReport(MimeMessageHelper helper) throws MessagingException {
        byte[] report= reportServiceInstance.generateReport();
        ByteArrayResource content= new ByteArrayResource(report);
        String fileName= reportServiceInstance instanceof PDFReportService? new Date()+"_orders.pdf": new Date()+"_orders.xlsx";
        helper.addAttachment(fileName, content);
    }
}
