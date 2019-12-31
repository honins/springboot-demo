package com.hy.demo.pojo.dto;

import com.hy.demo.mbg.model.SysUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by hy on 2019/9/4 14:02
 * @author hy
 */
@Setter
@Getter
@ToString
public class SysUserDTO implements Serializable {

    private Integer uid;
    private String username;
    private String password;
    private Integer status;
    private String jwtToken;

    public static SysUserDTO toSysUserDTO(SysUser sysUser){
        SysUserDTO dto = new SysUserDTO();
        dto.setUid(sysUser.getId());
        dto.setUsername(sysUser.getUsername());
        dto.setPassword(sysUser.getPassword());
        dto.setStatus(sysUser.getStatus());
        return dto;
    }

}
