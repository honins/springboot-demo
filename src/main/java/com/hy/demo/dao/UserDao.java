package com.hy.demo.dao;

import com.hy.demo.pojo.dto.SysUserDTO;
import org.apache.ibatis.annotations.Param;

/**
 * @author Created by hy
 * @date on 2019/11/26 12:56
 */
public interface UserDao {

    SysUserDTO getUser(@Param("username") String username, @Param("password") String password);

    SysUserDTO getUser(Integer uid);

}
