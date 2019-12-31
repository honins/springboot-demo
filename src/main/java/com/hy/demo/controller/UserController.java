package com.hy.demo.controller;

import com.hy.demo.common.CommonResult;
import com.hy.demo.component.constant.RedisConstant;
import com.hy.demo.pojo.dto.SysUserDTO;
import com.hy.demo.pojo.dto.SysUserOnlineDTO;
import com.hy.demo.pojo.dto.UserTokenDTO;
import com.hy.demo.pojo.param.LoginParam;
import com.hy.demo.service.AdminService;
import com.hy.demo.service.UserService;
import com.hy.demo.util.JedisUtil;
import com.hy.demo.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * @author hy
 */
@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    AdminService adminService;

    /**
     * 登陆
     * @return
     */
    @PostMapping(value = "/login")
    @ApiOperation(value = "登陆",notes = "输入账号密码登陆")
    public CommonResult<UserTokenDTO> submitLogin(@RequestBody @Valid LoginParam param) {
        try {
            UserTokenDTO tokenDto = userService.login(param.getUsername(), param.getPassword());
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
    @GetMapping(value = "/index")
    @ApiOperation(value = "首页",notes = "需身份验证")
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
    @ApiOperation(value = "获取在线用户",notes = "需验证权限")
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
    @PostMapping("/online/remove/{uid}")
    @ApiOperation(value = "在线踢人",notes = "需验证权限")
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