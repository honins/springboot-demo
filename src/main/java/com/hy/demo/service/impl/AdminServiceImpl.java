package com.hy.demo.service.impl;

import com.hy.demo.component.constant.RedisConstant;
import com.hy.demo.pojo.dto.OnlineUserDTO;
import com.hy.demo.pojo.dto.SysUserDTO;
import com.hy.demo.pojo.dto.SysUserOnlineDTO;
import com.hy.demo.service.AdminService;
import com.hy.demo.service.UserService;
import com.hy.demo.util.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Created by hy
 * @date on 2019/11/26 11:36
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    UserService userService;

    @Override
    public SysUserOnlineDTO getOnline() {
        List<OnlineUserDTO> userDtos = new ArrayList<>();
        // 查询所有Redis键
        Set<String> keys = JedisUtil.keysS(RedisConstant.PREFIX_SHIRO_CACHE_ACCOUNT + "*");
        for (String key : keys) {
            if (JedisUtil.exists(key)) {
                // 根据:分割key，获取最后一个字符(帐号)
                String[] keyArray = key.split(":");
                OnlineUserDTO userDto = new OnlineUserDTO();
                userDto.setUid(Integer.valueOf(keyArray[keyArray.length - 1]));
                SysUserDTO user = userService.getUser(userDto.getUid());
                userDto.setUsername(user.getUsername());
                userDto.setStatus(user.getStatus());
                userDtos.add(userDto);
            }
        }
        SysUserOnlineDTO sysUserOnlineDTO = new SysUserOnlineDTO();
        sysUserOnlineDTO.setCount(userDtos.size());
        sysUserOnlineDTO.setListSysUser(userDtos);
        return sysUserOnlineDTO;
    }
}
