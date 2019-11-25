package com.hy.springbootdemo.mbg.model;

import com.hy.springbootdemo.dto.SysUserDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by hy on 2019/9/4 13:43
 */
@Setter
@Getter
public class SysUser implements Serializable{

    private Integer id;
    private String username;
    private String password;
    private Integer status;

    public SysUserDTO toSysUserDTO(){
        SysUserDTO sysUserDTO = new SysUserDTO();
        sysUserDTO.setUid(this.getId());
        sysUserDTO.setUsername(this.getUsername());
        sysUserDTO.setStatus(this.getStatus());
        return sysUserDTO;
    }

}
