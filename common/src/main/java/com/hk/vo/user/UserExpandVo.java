package com.hk.vo.user;

import com.hk.entity.user.UserInfoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huangkun
 * @date 2025/7/13 10:21
 */
@Data
public class UserExpandVo implements Serializable {
    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

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

    public static UserExpandVo convert(UserInfoEntity userInfo) {
        if (userInfo == null) {
            return null;
        }
        UserExpandVo vo = new UserExpandVo();
        vo.setId(userInfo.getId());
        vo.setUserId(userInfo.getUserId());
        vo.setBirthday(userInfo.getBirthday());
        vo.setProvince(userInfo.getProvince());
        vo.setCity(userInfo.getCity());
        vo.setDistrict(userInfo.getDistrict());
        vo.setAddress(userInfo.getAddress());
        vo.setBio(userInfo.getBio());
        return vo;
    }
}
