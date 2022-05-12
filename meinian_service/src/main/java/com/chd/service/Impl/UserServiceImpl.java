package com.chd.service.Impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.chd.dao.UserDao;
import com.chd.pojo.User;
import com.chd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Override
    public User findUserByUsername(String username) {
        User user = userDao.findUserByUsername(username);
        return user;
    }
}
