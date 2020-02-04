package com.hy.demo.service;


import com.hy.demo.data.dto.SysUserDTO;
import com.hy.demo.data.dto.UserTokenDTO;

/**
 * Created by hy on 2019/9/4 13:59
 */
public interface UserService{
    SysUserDTO getUser(String account, String password);

    SysUserDTO getUser(Integer uid);

    UserTokenDTO login(String account, String password);
}
