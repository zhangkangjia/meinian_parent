package com.chd.dao;

import com.chd.pojo.User;

public interface UserDao {
    User findUserByUsername(String username);
}
