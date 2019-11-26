package com.hy.demo.service;

import java.util.Set;

/**
 * Created by hy on 2019/9/16 17:15
 */
public interface SysRoleService {
    Set<String> listRoleNameByUserId(Integer valueOf);

    Set<String> listPermissionByUserId(Integer valueOf);
}
