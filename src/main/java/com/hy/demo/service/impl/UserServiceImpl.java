package com.hy.demo.service.impl;

import com.hy.demo.component.constant.RedisConstant;
import com.hy.demo.component.exception.CustomException;
import com.hy.demo.dao.UserDao;
import com.hy.demo.data.dto.SysUserDTO;
import com.hy.demo.data.dto.UserTokenDTO;
import com.hy.demo.mapper.UserMapper;
import com.hy.demo.model.User;
import com.hy.demo.service.UserService;
import com.hy.demo.common.util.JedisUtil;
import com.hy.demo.common.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Created by hy
 * @date on 2019/11/26 13:43
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserDao userDao;
    @Resource
    UserMapper userMapper;

    @Override
    public SysUserDTO getUser(String name, String password) {
        return userDao.getUser(name,password);
    }

    @Override
    public SysUserDTO getUser(Integer uid) {
        Example example = Example.builder(User.class)
                .where(Sqls.custom().andEqualTo("id", uid))
                .orderByDesc("create_time")
                .build();
        List<User> list = userMapper.selectByExample(example);
        SysUserDTO sysUserDTO = new SysUserDTO();
        BeanUtils.copyProperties(list.get(0), sysUserDTO);
        return sysUserDTO;
    }

    @Override
    public UserTokenDTO login(String username, String password) {
        SysUserDTO userDTO = userDao.getUser(username, password);
        if (userDTO == null){
            throw new CustomException("账号密码错误");
        }
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        //生成jwt token
        String token = JwtUtil.createToken(userDTO.getUid(),currentTimeMillis);
        //将账户和创建时间存进redis
        JedisUtil.setObject(RedisConstant.PREFIX_SHIRO_CACHE_ACCOUNT + userDTO.getUid(), currentTimeMillis, JwtUtil.getExpiration().intValue());
        return new UserTokenDTO(token,JwtUtil.getExpiration());
    }
}
