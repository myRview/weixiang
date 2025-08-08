package com.hk.service.message;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.message.UserMessageEntity;
import com.hk.vo.message.UserMessageVO;

import java.util.List;

/**
 * <p>
 * 用户消息表 服务类
 * </p>
 *
 * @author hk
 * @since 2025-08-06
 */
public interface UserMessageService extends IService<UserMessageEntity> {

    UserMessageVO getMessageById(Long id);

    void saveMessage(UserMessageVO messageVO);

    Boolean readMessage(List<Long> messageIds);

    Boolean deleteMessage(List<Long> messageIds);

    List<UserMessageVO> getMessageList(Long userId);

}
