package com.chd.dao;

import com.chd.pojo.Address;
import com.github.pagehelper.Page;

import java.util.List;

public interface AddressDao {
    List<Address> findAllMaps();

    Page<Address> findPage(String queryString);

    void addAddress(Address address);

    void deleteById(Integer id);
}
