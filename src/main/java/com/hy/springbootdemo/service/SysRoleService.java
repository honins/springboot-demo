package com.hy.springbootdemo.service;

import java.util.Set;

/**
 * Created by hy on 2019/9/16 17:15
 */
public interface SysRoleService {
    Set<String> getRoleNameByUserId(Integer valueOf);

    Set<String> listPermissionByUserId(Integer valueOf);
}
