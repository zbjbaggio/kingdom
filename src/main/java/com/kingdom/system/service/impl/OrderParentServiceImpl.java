package com.kingdom.system.service.impl;

import com.kingdom.system.data.entity.OrderParent;
import com.kingdom.system.mapper.OrderParentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderParentServiceImpl {

    @Autowired
    private OrderParentMapper orderParentMapper;

    public List<OrderParent> list(String name, String startDate, String endDate) {
        return orderParentMapper.list(name, startDate, endDate);
    }
}
