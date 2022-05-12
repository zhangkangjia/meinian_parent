package com.chd.dao;

import com.chd.pojo.Role;

import java.util.Set;

public interface RoleDao {
    Set<Role> findRolesByUserId(Integer userId);
}
