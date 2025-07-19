package com.hk.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huangkun
 * @date 2025/7/13 10:31
 */
@Data
public class EditUserExpandVO implements Serializable {

    @Schema(description = "用户id")
    @NonNull
    private Long userId;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "性别 0-女，1-男")
    private Integer gender;

    @Schema(description = "头像")
    private String avatar;


    @Schema(description = "生日")
    private Date birthday;

    @Schema(description = "省")
    private String province;

    @Schema(description = "市")
    private String city;

    @Schema(description = "区/县")
    private String district;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "个人简介")
    private String bio;
}
