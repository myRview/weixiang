package com.hk.vo.log;

import com.hk.entity.log.LoginLogEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author huangkun
 * @date 2025/6/27 13:43
 */
@Data
public class LoginLogVO {

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "IP地址")
    private String ipAddress;

    @Schema(description = "登录地点")
    private String location;

    @Schema(description = "登录设备")
    private String device;

    @Schema(description = "登录时间")
    private Date loginTime;

    @Schema(description = "1-登录成功，0-登录失败")
    private Integer status;

    public static LoginLogVO converter(LoginLogEntity loginLogEntity) {
        LoginLogVO loginLogVO = new LoginLogVO();
        loginLogVO.setId(loginLogEntity.getId());
        loginLogVO.setUsername(loginLogEntity.getUsername());
        loginLogVO.setUserId(loginLogEntity.getUserId());
        loginLogVO.setIpAddress(loginLogEntity.getIpAddress());
        loginLogVO.setLocation(loginLogEntity.getLocation());
        loginLogVO.setDevice(loginLogEntity.getDevice());
        loginLogVO.setLoginTime(loginLogEntity.getLoginTime());
        loginLogVO.setStatus(loginLogEntity.getStatus());
        return loginLogVO;
    }
}
