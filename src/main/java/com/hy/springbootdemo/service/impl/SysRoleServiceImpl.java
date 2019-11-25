package com.hy.springbootdemo.service.impl;

import com.hy.springbootdemo.mbg.mapper.SystemRoleMapper;
import com.hy.springbootdemo.service.SysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Created by hy on 2019/9/17 10:34
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Resource
    SystemRoleMapper systemRoleMapper;

    @Override
    public Set<String> getRoleNameByUserId(Integer uid) {
        return systemRoleMapper.getRoleNameByUserId(uid);
    }

    @Override
    public Set<String> listPermissionByUserId(Integer uid) {
        return systemRoleMapper.listPermissionByUserId(uid);
    }
}
