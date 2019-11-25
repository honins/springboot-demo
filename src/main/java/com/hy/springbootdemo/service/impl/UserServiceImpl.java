package com.hy.springbootdemo.service.impl;

import com.hy.springbootdemo.dto.SysUserDTO;
import com.hy.springbootdemo.mbg.mapper.UserMapper;
import com.hy.springbootdemo.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by hy on 2019/9/4 14:01
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public SysUserDTO getUser(String username, String password) {
        return userMapper.getUser(username,password);
    }

    @Override
    public SysUserDTO getUser(Integer uid) {
        return userMapper.getUserByUid(uid);
    }
}
