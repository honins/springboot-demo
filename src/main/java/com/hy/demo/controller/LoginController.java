package com.hy.demo.controller;

import com.hy.demo.common.CommonResult;
import com.hy.demo.component.constant.RedisConstant;
import com.hy.demo.dto.*;
import com.hy.demo.service.AdminService;
import com.hy.demo.service.UserService;
import com.hy.demo.util.JedisUtil;
import com.hy.demo.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    AdminService adminService;

    /**
     * 登陆
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult<UserTokenDto> submitLogin(@RequestBody @Valid LoginParam param) {
        try {
            UserTokenDto tokenDto = userService.login(param.getUsername(), param.getPassword());
            return CommonResult.success(tokenDto);
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
            return CommonResult.success("welcome "+uid);
        }
    }

    /**
     * 获取在线用户
     * @param
     */
    @GetMapping("/online")
    @RequiresPermissions(logical = Logical.AND, value = {"sys:online:view"})
    public CommonResult online() {
        SysUserOnlineDTO online = adminService.getOnline();
        return CommonResult.success(online);
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