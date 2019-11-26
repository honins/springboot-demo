package com.hy.demo.service;


import com.hy.demo.dto.SysUserDTO;
import com.hy.demo.dto.UserTokenDto;

/**
 * Created by hy on 2019/9/4 13:59
 */
public interface UserService {
    SysUserDTO getUser(String name, String password);

    SysUserDTO getUser(Integer uid);

    UserTokenDto login(String username, String password);
}
