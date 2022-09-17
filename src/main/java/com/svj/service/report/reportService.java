package com.svj.service.report;

import com.svj.model.Order;

import java.util.List;

public interface reportService {
    public byte[] generateReport(List<Order> orders);
}
