package com.hy.springbootdemo.mbg.mapper;


import com.hy.springbootdemo.mbg.model.SystemRole;

import java.util.List;
import java.util.Set;

public interface SystemRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemRole record);

    int insertSelective(SystemRole record);

    SystemRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemRole record);

    int updateByPrimaryKey(SystemRole record);

    List<SystemRole> listAllRole();

    Set<String> getRoleNameByUserId(Integer id);

    Set<String> listPermissionByUserId(Integer uid);
}