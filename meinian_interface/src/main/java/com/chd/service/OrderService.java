package com.chd.service;

import com.chd.entity.Result;

import java.util.Map;

public interface OrderService {
    Result saveOrder(Map map) throws Exception;

    Map findById4Detail(Integer id) throws Exception;
}
