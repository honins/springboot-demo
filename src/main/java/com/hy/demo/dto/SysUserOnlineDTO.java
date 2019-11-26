package com.hy.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by hy on 2019/9/12 16:41
 */
@Setter
@Getter
@ToString
public class SysUserOnlineDTO {

    private Integer count;

    private List<OnlineUserDto> listSysUser;

}
