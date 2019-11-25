package com.hy.springbootdemo.controller;

import com.hy.springbootdemo.common.CommonResult;
import com.hy.springbootdemo.component.constant.RedisConstant;
import com.hy.springbootdemo.dto.JwtToken;
import com.hy.springbootdemo.dto.LoginParam;
import com.hy.springbootdemo.dto.SysUserDTO;
import com.hy.springbootdemo.dto.SysUserOnlineDTO;
import com.hy.springbootdemo.service.UserService;
import com.hy.springbootdemo.util.JedisUtil;
import com.hy.springbootdemo.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * @author hy
 */
@RestController
@RequestMapping(value = "/auth")
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    /**
     * 登陆
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult submitLogin(@RequestBody @Valid LoginParam loginParam) {
        try {
            logger.debug("debug login");
            SysUserDTO userDTO = userService.getUser(loginParam.getUsername(), loginParam.getPassword());
            String currentTimeMillis = String.valueOf(System.currentTimeMillis());
            //生成jwt token
            String token = JwtUtil.createToken(userDTO.getUid(),currentTimeMillis);
            JwtToken jwtToken = new JwtToken(token);
            //将账户和创建时间存进redis
            JedisUtil.setObject(RedisConstant.PREFIX_SHIRO_CACHE_ACCOUNT + userDTO.getUid(), currentTimeMillis, JwtUtil.getExpiration().intValue());
            return CommonResult.success(jwtToken);
        } catch (DisabledAccountException e) {
            return CommonResult.failed("账户已被禁用");
        } catch (AuthenticationException e) {
            return CommonResult.failed("token验证错误"+e.getMessage());
        }
    }

    /**
     * 访问首页
     * @param request
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public CommonResult loginSuccessMessage(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        String jwtToken = String.valueOf(subject.getPrincipals());
        String uid = JwtUtil.getClaim(jwtToken,JwtUtil.CLAIM_KEY_ACCOUNT);
        if (uid == null){
            return CommonResult.unauthorized("身份验证失败");
        }else {
            return CommonResult.success("ok");
        }
    }

    /**
     * 获取在线用户
     * @param
     */
    @GetMapping("/online")
    @RequiresPermissions(logical = Logical.AND, value = {"sys:online:view"})
    public CommonResult online() {
        List<SysUserDTO> userDtos = new ArrayList<>();
        // 查询所有Redis键
        Set<String> keys = JedisUtil.keysS(RedisConstant.PREFIX_SHIRO_CACHE_ACCOUNT + "*");
        for (String key : keys) {
            if (JedisUtil.exists(key)) {
                // 根据:分割key，获取最后一个字符(帐号)
                String[] keyArray = key.split(":");
                SysUserDTO userDto = new SysUserDTO();
                userDto.setUid(Integer.valueOf(keyArray[keyArray.length - 1]));
                userDto = userService.getUser(userDto.getUid());
                userDtos.add(userDto);
            }
        }
        int size = userDtos.size();
        if (size <= 0) {
            return CommonResult.failed();
        }
        SysUserOnlineDTO sysUserOnlineDTO = new SysUserOnlineDTO();
        sysUserOnlineDTO.setCount(size);
        sysUserOnlineDTO.setListSysUser(userDtos);
        return CommonResult.success(sysUserOnlineDTO);
    }

    /**
     * 在线踢人
     * @param id
     * @return
     */
    @GetMapping("/online/remove/{uid}")
    @RequiresPermissions(value = {"sys:online:remove"})
    public CommonResult remove(@PathVariable("uid") Integer id){
        SysUserDTO sysUserDTO = userService.getUser(id);
        String key = RedisConstant.PREFIX_SHIRO_CACHE_ACCOUNT+sysUserDTO.getUid();
        if (JedisUtil.exists(key)){
            if (JedisUtil.delKey(key) > 0){
                return CommonResult.success("踢人成功");
            }
        }
        return CommonResult.failed();
    }

}