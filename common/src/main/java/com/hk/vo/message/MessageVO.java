package com.hk.vo.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author huangkun
 * @date 2025/7/15 13:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class MessageVO implements Serializable {

    @Schema(description = "消息类型")
    private String messageType;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "数据id")
    private Long dataId;

    public MessageVO(String messageType, Long dataId) {
        this.messageType = messageType;
        this.dataId = dataId;
    }
}
