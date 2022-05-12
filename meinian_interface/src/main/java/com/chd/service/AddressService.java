package com.chd.service;

import com.chd.entity.PageResult;
import com.chd.entity.QueryPageBean;
import com.chd.pojo.Address;

import java.util.List;

public interface AddressService {
    List<Address> findAllMaps();

    PageResult findPage(QueryPageBean queryPageBean);

    void addAddress(Address address);

    void deleteById(Integer id);
}
