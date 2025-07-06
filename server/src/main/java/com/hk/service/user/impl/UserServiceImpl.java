package com.hk.service.user.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.common.ErrorCode;
import com.hk.constants.BaseConstant;
import com.hk.entity.user.RoleEntity;
import com.hk.entity.user.UserEntity;
import com.hk.entity.user.UserRoleEntity;
import com.hk.enums.StatusEnum;
import com.hk.exception.BusinessException;
import com.hk.mapper.user.UserMapper;
import com.hk.param.UserSearchParam;
import com.hk.service.user.RoleService;
import com.hk.service.user.UserRoleService;
import com.hk.service.user.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.utils.Md5Utils;
import com.hk.vo.user.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
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

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;

    @Override
    public Page<UserVO> getUserList(UserSearchParam userSearchParam) {
        String account = userSearchParam.getAccount();
        String email = userSearchParam.getEmail();
        String phone = userSearchParam.getPhone();
        Integer status = userSearchParam.getStatus();
        Integer pageNum = userSearchParam.getPageNum();
        Integer pageSize = userSearchParam.getPageSize();
        String userName = userSearchParam.getUserName();

        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(account), UserEntity::getAccount, account);
        queryWrapper.like(StringUtils.isNotBlank(email), UserEntity::getEmail, email);
        queryWrapper.like(StringUtils.isNotBlank(phone), UserEntity::getPhone, phone);
        queryWrapper.like(StringUtils.isNotBlank(userName), UserEntity::getUserName, userName);
        queryWrapper.eq(status != null, UserEntity::getStatus, status);
        Page<UserEntity> page = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        Page<UserVO> pageResult = new Page<>(pageNum, pageSize, page.getTotal());
        List<UserVO> userVOS = converterVO(page.getRecords());
        if (CollectionUtil.isNotEmpty(userVOS)) {
            List<Long> userIds = userVOS.stream().map(UserVO::getId).collect(Collectors.toList());
            List<UserRoleEntity> userRoleList = userRoleService.selectByUserIds(userIds);
            List<Long> roleIds = userRoleList.stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());
            Map<Long, Long> userRoleMap = userRoleList.stream().collect(Collectors.toMap(UserRoleEntity::getUserId, UserRoleEntity::getRoleId, (k1, k2) -> k2));
            List<RoleEntity> roleList = roleService.selectByIds(roleIds);
            Map<Long, String> roleMap = roleList.stream().collect(Collectors.toMap(RoleEntity::getId, RoleEntity::getRoleCode));
            userVOS.forEach(vo -> {
                Long roleId = userRoleMap.get(vo.getId());
                if (roleId != null) {
                    vo.setRoleCode(roleMap.get(roleId));
                }
            });
        }
        pageResult.setRecords(userVOS);
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUser(UserAddVO addVO) {
        String password = addVO.getPassword();
        String confirmPassword = addVO.getConfirmPassword();
        if (!password.equals(confirmPassword)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "密码不一致");
        }
        RoleEntity role = roleService.getById(addVO.getRoleId());
        if (role == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "添加用户异常，请联系管理员");
        }
        String salt = UUID.randomUUID().toString();
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(addVO, userEntity);
        userEntity.setSalt(salt);
        String md5 = Md5Utils.md5(salt, password);
        userEntity.setPassword(md5);
        String email = addVO.getEmail();
        String phone = addVO.getPhone();
        checkIphoneAndEmail(email, phone);
        userEntity.setEmail(email == null ? "" : email);
        userEntity.setPhone(phone == null ? "" : phone);
        userEntity.setStatus(StatusEnum.NORMAL.getCode());
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setRoleId(role.getId());
        boolean save = this.save(userEntity);
        userRole.setUserId(userEntity.getId());
        userRoleService.save(userRole);
        return save;
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
        if (user == null) return null;
        UserVO userVO = this.converterVO(user);
        UserRoleEntity userRole = userRoleService.selectByUserId(user.getId());
        if (userRole != null) {
            RoleVO roleVO = roleService.selectById(userRole.getRoleId());
            userVO.setRoleVO(roleVO);
            userVO.setRoleCode(roleVO.getRoleCode());
        }
        return userVO;
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
        checkIphoneAndEmail(editVO.getEmail(), editVO.getPhone());
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(editVO, userEntity);
        userEntity.setId(editVO.getUserId());
        return this.updateById(userEntity);
    }

    @Override
    public boolean updatePassword(String password, Long userId) {
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
        String md5 = Md5Utils.md5(salt, BaseConstant.DEFAULT_PASSWORD);
        LambdaUpdateWrapper<UserEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserEntity::getId, userId);
        updateWrapper.set(UserEntity::getPassword, md5);
        return this.update(updateWrapper);
    }

    private void checkIphoneAndEmail(String email, String phone) {
        // 邮箱正则表达式（支持常见格式）
        final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        // 手机号正则表达式（中国大陆11位）
        final String PHONE_REGEX = "^1[3-9]\\d{9}$";

        if (StringUtils.isNotBlank(email)) {
            if (!Pattern.matches(EMAIL_REGEX, email.trim())) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "邮箱格式不正确");
            }
        }
        if (StringUtils.isNotBlank(phone)) {
            if (!Pattern.matches(PHONE_REGEX, phone.trim())) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "手机号格式不正确");
            }
        }
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
