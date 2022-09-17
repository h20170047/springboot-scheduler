package com.svj.service;

import com.svj.model.Order;
import com.svj.repository.OrderRepository;
import com.svj.service.report.PDFReportService;
import com.svj.service.report.reportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class NotificationService {

    private reportService reportServiceInstance;
    private JavaMailSender javaMailSender;
    private OrderRepository repository;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${report.send.email.toList}")
    private String toEmails;

    @Autowired
    public NotificationService( reportService reportServiceObj, JavaMailSender javaMailSender, OrderRepository orderRepository){
        this.reportServiceInstance= reportServiceObj;
        this.javaMailSender= javaMailSender;
        repository= orderRepository;
    }

    public String sendDailyReports() throws MessagingException, IOException {
        MimeMessage mimeMessage= javaMailSender.createMimeMessage();
        MimeMessageHelper helper= new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(sender);
        helper.setTo(toEmails.split(","));
        helper.setSubject("List of orders_"+new Date());
        helper.setText("Hello,\nPlease find the attachment for today's order received!");

        buildReport(helper);

        javaMailSender.send(mimeMessage);
        return "Mail sent successfully with attachment";
    }

    private void buildReport(MimeMessageHelper helper) throws MessagingException {
        List<Order> orders = repository.findAll();
        byte[] report= reportServiceInstance.generateReport(orders);
        ByteArrayResource content= new ByteArrayResource(report);
        String fileName= reportServiceInstance instanceof PDFReportService ? new Date()+"_orders.pdf": new Date()+"_orders.xlsx";
        helper.addAttachment(fileName, content);
    }
}
