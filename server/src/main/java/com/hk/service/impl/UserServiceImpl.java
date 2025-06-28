package com.hk.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.constants.UserConstant;
import com.hk.entity.UserEntity;
import com.hk.enums.StatusEnum;
import com.hk.mapper.UserMapper;
import com.hk.param.UserSearchParam;
import com.hk.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.utils.Md5Utils;
import com.hk.vo.user.UserAddVO;
import com.hk.vo.user.UserEditVO;
import com.hk.vo.user.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {


    @Override
    public Page<UserVO> getUserList(UserSearchParam userSearchParam) {
        String account = userSearchParam.getAccount();
        String email = userSearchParam.getEmail();
        String phone = userSearchParam.getPhone();
        Integer status = userSearchParam.getStatus();
        Integer pageNum = userSearchParam.getPageNum();
        Integer pageSize = userSearchParam.getPageSize();

        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(account), UserEntity::getAccount, account);
        queryWrapper.eq(status != null, UserEntity::getStatus, status);
        Page<UserEntity> page = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        Page<UserVO> pageResult = new Page<>(pageNum, pageSize, page.getTotal());
        pageResult.setRecords(converterVO(page.getRecords()));
        return pageResult;
    }

    @Override
    public boolean saveUser(UserAddVO addVO) {
        String password = addVO.getPassword();
        String confirmPassword = addVO.getConfirmPassword();
        if (!password.equals(confirmPassword)){
//            thow new RuntimeException("密码不一致");
        }
        String salt = UUID.randomUUID().toString();
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(addVO, userEntity);
        userEntity.setSalt(salt);
        String md5 = Md5Utils.md5(salt, password);
        userEntity.setPassword(md5);
        return this.save(userEntity);
    }

    @Override
    public boolean transStatus(Long userId, Integer status) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setStatus(status);
        return this.updateById(userEntity);
    }

    @Override
    public UserVO getInfo(Long id) {
        UserEntity user = this.getById(id);
        return this.converterVO(user);
    }

    @Override
    public UserEntity selectOneByAccount(String account) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getAccount, account);
        queryWrapper.eq(UserEntity::getStatus, StatusEnum.NORMAL.getCode());
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean updateUser(UserEditVO editVO) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(editVO, userEntity);
        return this.updateById(userEntity);
    }

    @Override
    public boolean updatePassword(String password) {
        //TODO:获取当前用户Id
        Long userId = 1L;
        UserEntity user = this.getById(userId);
        String salt = user.getSalt();
        String md5 = Md5Utils.md5(salt, password);
        LambdaUpdateWrapper<UserEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserEntity::getId, userId);
        updateWrapper.set(UserEntity::getPassword, md5);
        return this.update(updateWrapper);
    }

    @Override
    public boolean resetPassword(Long userId) {
        UserEntity user = this.getById(userId);
        String salt = user.getSalt();
        String md5 = Md5Utils.md5(salt, UserConstant.DEFAULT_PASSWORD);
        LambdaUpdateWrapper<UserEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserEntity::getId, userId);
        updateWrapper.set(UserEntity::getPassword, md5);
        return this.update(updateWrapper);
    }

    private UserVO converterVO(UserEntity user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    private List<UserVO> converterVO(List<UserEntity> userEntityList) {
        if (CollectionUtil.isEmpty(userEntityList)) {
            return new ArrayList<>();
        }
        return userEntityList.stream().map(this::converterVO).collect(Collectors.toList());
    }

}
