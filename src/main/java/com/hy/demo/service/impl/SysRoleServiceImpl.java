package com.hy.demo.service.impl;

import com.hy.demo.dao.SysPermissionDao;
import com.hy.demo.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by hy on 2019/9/17 10:34
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    SysPermissionDao sysPermissionDao;

    @Override
    public Set<String> listRoleNameByUserId(Integer uid) {
        return sysPermissionDao.listRoleNameByUserId(uid);
    }

    @Override
    public Set<String> listPermissionByUserId(Integer uid) {
        return sysPermissionDao.listPermissionByUserId(uid);
    }
}
