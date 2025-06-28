package com.hk.param;

import com.hk.common.PageBaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huangkun
 * @date 2025/6/27 13:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogSearchParam extends PageBaseVO {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "IP地址")
    private String ipAddress;
}
