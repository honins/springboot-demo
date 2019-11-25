package com.hy.springbootdemo.service;


import com.hy.springbootdemo.dto.SysUserDTO;

/**
 * Created by hy on 2019/9/4 13:59
 */
public interface UserService {
    SysUserDTO getUser(String name, String password);

    SysUserDTO getUser(Integer uid);
}
