package com.chd.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chd.constant.MessageConstant;
import com.chd.entity.PageResult;
import com.chd.entity.QueryPageBean;
import com.chd.entity.Result;
import com.chd.pojo.Address;
import com.chd.service.AddressService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Reference
    AddressService addressService;
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        try {
            addressService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_ADDRESS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_ADDRESS_FAIL);

        }

    }

    @RequestMapping("/addAddress")
    public Result addAddress(@RequestBody Address address){

        try {
            addressService.addAddress(address);
            return new Result(true, MessageConstant.ADD_ADDRESS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ADDRESS_FAIL);

        }


    }
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
      PageResult pageResult= addressService.findPage(queryPageBean);
        return pageResult;
    }

    @RequestMapping("/findAllMaps")
    public Map findAllMaps(){
        Map map =new HashMap<>();
        List<Address> lists=addressService.findAllMaps();

        List<Map> gridnMaps=new ArrayList<>();
        List<Map> nameMaps= new ArrayList<>();

        for (Address list : lists) {
            Map gridMap=new HashMap();
            Map nameMap=new HashMap<>();
            //????????????
            gridMap.put("lng",list.getLng());
            //????????????
            gridMap.put("lat",list.getLat());
            gridnMaps.add(gridMap);
            //?????????????????????
            nameMap.put("addressName",list.getAddressName());
            nameMaps.add(nameMap);

        }
        // ???????????????
        map.put("gridnMaps",gridnMaps);
        // ????????????
        map.put("nameMaps",nameMaps);
        return map;

    }
}
