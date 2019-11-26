package com.hy.demo.dao;

import java.util.Set;

/**
 * @author Created by hy
 * @date on 2019/11/26 15:26
 */
public interface SysPermissionDao {

    Set<String> listRoleNameByUserId(Integer uid);

    Set<String> listPermissionByUserId(Integer uid);

}
