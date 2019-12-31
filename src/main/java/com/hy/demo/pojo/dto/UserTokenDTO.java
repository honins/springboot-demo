package com.hy.demo.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Created by hy
 * @date on 2019/11/26 11:01
 */
@Data
@AllArgsConstructor
public class UserTokenDTO {

    private static final long serialVersionUID = 1282057025599847895L;

    @ApiModelProperty(value = "token",example = "asdas.avd.sdgfwdv")
    private String token;

    @ApiModelProperty(value = "过期时间，单位秒")
    private Long expireTime;

}
