package com.hk.elasticsearch.vo;

import com.hk.vo.log.OperationLogVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huangkun
 * @date 2025/8/24 15:28
 */
@Data
@Document(indexName = "operation_log")
public class OperationLogEsVO implements Serializable {
    private static final long serialVersionUID = -4830157848838676208L;

    @Schema(description = "主键")
    @Id
    private String id;

    @Schema(description = "用户id")
    @Field(name = "userId", type = FieldType.Keyword)
    private String userId;

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

    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "1-成功，0-失败")
    private Integer status;

    /**
     * 将操作日志VO转换为ES存储对象
     *
     * @param operationLogVO
     * @return
     */
    public static OperationLogEsVO convertToEsVO(OperationLogVO operationLogVO) {
        if (operationLogVO == null) return null;
        OperationLogEsVO operationLogEsVO = new OperationLogEsVO();
        operationLogEsVO.setId(String.valueOf(operationLogVO.getId()));
        operationLogEsVO.setUserId(String.valueOf(operationLogVO.getUserId()));
        operationLogEsVO.setUsername(operationLogVO.getUsername());
        operationLogEsVO.setOperationContent(operationLogVO.getOperationContent());
        operationLogEsVO.setOperationModule(operationLogVO.getOperationModule());
        operationLogEsVO.setIpAddress(operationLogVO.getIpAddress());
        operationLogEsVO.setOperationAddress(operationLogVO.getOperationAddress());
        operationLogEsVO.setCreateTime(operationLogVO.getCreateTime());
        operationLogEsVO.setStatus(operationLogVO.getStatus());
        return operationLogEsVO;

    }

    /**
     * 将ES存储对象转换为实体对象
     *
     * @param operationLogEsVO
     * @return
     */
    public static OperationLogVO convertToVO(OperationLogEsVO operationLogEsVO) {
        if (operationLogEsVO == null) return null;
        OperationLogVO operationLogVO = new OperationLogVO();
        operationLogVO.setId(Long.valueOf(operationLogEsVO.getId()));
        operationLogVO.setUserId(Long.valueOf(operationLogEsVO.getUserId()));
        operationLogVO.setUsername(operationLogEsVO.getUsername());
        operationLogVO.setOperationContent(operationLogEsVO.getOperationContent());
        operationLogVO.setOperationModule(operationLogEsVO.getOperationModule());
        operationLogVO.setIpAddress(operationLogEsVO.getIpAddress());
        operationLogVO.setOperationAddress(operationLogEsVO.getOperationAddress());
        operationLogVO.setCreateTime(operationLogEsVO.getCreateTime());
        operationLogVO.setStatus(operationLogEsVO.getStatus());
        return operationLogVO;
    }
}
