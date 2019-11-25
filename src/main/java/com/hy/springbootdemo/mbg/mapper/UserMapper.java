package com.hy.springbootdemo.mbg.mapper;

import com.hy.springbootdemo.dto.SysUserDTO;
import com.hy.springbootdemo.mbg.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    SysUserDTO getUser(@Param("username") String username, @Param("password") String password);

    SysUserDTO getUserByUid(Integer uid);
}