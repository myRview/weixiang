package com.hk.vo.message;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hk.entity.message.UserMessageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户消息表
 * </p>
 *
 * @author hk
 * @since 2025-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserMessageVO implements Serializable {


    private static final long serialVersionUID = -8177669967896560902L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "消息内容")
    private String message;

    @Schema(description = "阅读状态，0-未读，1-已读")
    private Integer readStatus;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "删除标识,0-正常，1-已删除")
    private Integer deleteFlag;

    public static UserMessageVO convert(UserMessageEntity userMessageEntity){
        if (userMessageEntity == null) return null;
        UserMessageVO userMessageVO = new UserMessageVO();
        BeanUtils.copyProperties(userMessageEntity, userMessageVO);
        return userMessageVO;
    }
}
