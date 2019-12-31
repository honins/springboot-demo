package com.hy.demo.service;


import com.hy.demo.pojo.dto.SysUserDTO;
import com.hy.demo.pojo.dto.UserTokenDTO;

/**
 * Created by hy on 2019/9/4 13:59
 */
public interface UserService {
    SysUserDTO getUser(String name, String password);

    SysUserDTO getUser(Integer uid);

    UserTokenDTO login(String username, String password);
}
