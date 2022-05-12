package com.chd.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chd.pojo.Permission;
import com.chd.pojo.Role;
import com.chd.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    UserService userService;

    @Override//根据用户名查询用户信息
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //远程调用用户服务，根据用户名查询用户信息
        com.chd.pojo.User user =userService.findUserByUsername(username);
        if (user==null){
            return null;//不存在这个用户，返回null给框架，框架会抛出异常，进行异常处理,跳转到登录页面
        }
        //构架权限集合
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        Set<Role> roles =user.getRoles();//集合数据 由RoleDao帮忙方法查询得到
        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //授权
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        /**
         * User()
         * 1：指定用户名
         * 2：指定密码（SpringSecurity会自动对密码进行校验）
         * 3：传递授予的角色和权限
         */
        UserDetails userDetails = new User(username,user.getPassword(),list);
        return userDetails;

    }
}
