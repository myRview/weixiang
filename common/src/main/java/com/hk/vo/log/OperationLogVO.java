package com.hk.vo.log;

import com.hk.entity.log.OperationLogEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author huangkun
 * @date 2025/6/27 13:40
 */
@Data
public class OperationLogVO {

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "操作内容")
    private String operationContent;

    @Schema(description = "操作模块")
    private String operationModule;

    @Schema(description = "IP地址")
    private String ipAddress;

    @Schema(description = "操作地址")
    private String operationAddress;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "1-成功，0-失败")
    private Integer status;


    public static OperationLogVO converter(OperationLogEntity operationLog) {
        OperationLogVO operationLogVO = new OperationLogVO();
        operationLogVO.setId(operationLog.getId());
        operationLogVO.setUserId(operationLog.getUserId());
        operationLogVO.setUsername(operationLog.getUsername());
        operationLogVO.setOperationContent(operationLog.getOperationContent());
        operationLogVO.setOperationModule(operationLog.getOperationModule());
        operationLogVO.setIpAddress(operationLog.getIpAddress());
        operationLogVO.setOperationAddress(operationLog.getOperationAddress());
        operationLogVO.setCreateTime(operationLog.getCreateTime());
        operationLogVO.setStatus(operationLog.getStatus());
        return operationLogVO;
    }
}
