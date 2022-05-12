package com.chd.service;

import com.chd.pojo.User;

public interface UserService {
    User findUserByUsername(String username);
}
