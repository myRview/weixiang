package com.hk.vo.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hk.entity.system.BlackIpEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 黑名单ip表
 * </p>
 *
 * @author hk
 * @since 2025-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BlackIpVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "ip地址")
    private String ip;

    public static BlackIpVO convertToVo(BlackIpEntity entity){
        if(entity == null){
            return null;
        }
        BlackIpVO vo = new BlackIpVO();
        vo.setId(entity.getId());
        vo.setUserId(entity.getUserId());
        vo.setIp(entity.getIp());
        return vo;
    }
}
