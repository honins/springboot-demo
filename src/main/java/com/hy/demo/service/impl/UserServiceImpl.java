package com.hy.demo.service.impl;

import com.hy.demo.component.constant.RedisConstant;
import com.hy.demo.component.exception.CustomException;
import com.hy.demo.dao.UserDao;
import com.hy.demo.dto.SysUserDTO;
import com.hy.demo.dto.UserTokenDto;
import com.hy.demo.mbg.mapper.UserMapper;
import com.hy.demo.mbg.model.User;
import com.hy.demo.mbg.model.UserExample;
import com.hy.demo.service.UserService;
import com.hy.demo.util.JedisUtil;
import com.hy.demo.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdEqualTo(uid);
        List<User> users = userMapper.selectByExample(userExample);
        SysUserDTO sysUserDTO = new SysUserDTO();
        BeanUtils.copyProperties(users.get(0), sysUserDTO);
        return sysUserDTO;
    }

    @Override
    public UserTokenDto login(String username, String password) {
        SysUserDTO userDTO = userDao.getUser(username, password);
        if (userDTO == null){
            throw new CustomException("账号密码错误");
        }
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        //生成jwt token
        String token = JwtUtil.createToken(userDTO.getUid(),currentTimeMillis);
        //将账户和创建时间存进redis
        JedisUtil.setObject(RedisConstant.PREFIX_SHIRO_CACHE_ACCOUNT + userDTO.getUid(), currentTimeMillis, JwtUtil.getExpiration().intValue());
        return new UserTokenDto(token,JwtUtil.getExpiration());
    }
}
