package com.hk.service.message.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.entity.message.UserMessageEntity;
import com.hk.mapper.message.UserMessageMapper;
import com.hk.service.message.UserMessageService;
import com.hk.vo.message.UserMessageVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户消息表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-08-06
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessageEntity> implements UserMessageService {

    @Override
    public UserMessageVO getMessageById(Long id) {
        return UserMessageVO.convert(this.getById(id));
    }

    @Override
    public void saveMessage(UserMessageVO messageVO) {
        UserMessageEntity entity = new UserMessageEntity();
        entity.setUserId(messageVO.getUserId());
        entity.setMessage(messageVO.getMessage());
        this.save(entity);
        messageVO.setId(entity.getId());
    }

    @Override
    public Boolean readMessage(List<Long> messageIds) {
        LambdaUpdateWrapper<UserMessageEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(UserMessageEntity::getId, messageIds);
        updateWrapper.set(UserMessageEntity::getReadStatus, 1);
        return this.update(updateWrapper);
    }

    @Override
    public Boolean deleteMessage(List<Long> messageIds) {
        return this.removeBatchByIds(messageIds);
    }

    @Override
    public List<UserMessageVO> getMessageList(Long userId) {
        LambdaQueryWrapper<UserMessageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserMessageEntity::getUserId, userId);
        queryWrapper.orderByDesc(UserMessageEntity::getId);
        queryWrapper.select(UserMessageEntity::getId, UserMessageEntity::getUserId, UserMessageEntity::getMessage,
                UserMessageEntity::getReadStatus, UserMessageEntity::getCreateTime);
        List<UserMessageEntity> list = this.list(queryWrapper);
        return list.stream().map(UserMessageVO::convert).collect(Collectors.toList());
    }
}
