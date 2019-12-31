package com.hy.demo.mbg.model;

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

}
