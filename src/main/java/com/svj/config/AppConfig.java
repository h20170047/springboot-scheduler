package com.svj.config;

import com.svj.repository.OrderRepository;
import com.svj.service.ExcelReportService;
import com.svj.service.PDFReportService;
import com.svj.service.reportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class AppConfig {

    private ApplicationContext context;

    public AppConfig(ApplicationContext applicationContext){
        context= applicationContext;
    }

    @Value("${report_format}")
    private String report_format;

    @Bean
    public reportService getReportInstance(){
        if(report_format.toLowerCase().equals("pdf"))
            return (reportService) context.getBean("PDFReportService");
        else
            return (reportService) context.getBean("excelReportService");
    }
}
