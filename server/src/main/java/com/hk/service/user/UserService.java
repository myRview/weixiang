package com.hk.service.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.entity.user.UserEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.param.UserSearchParam;
import com.hk.vo.user.UserAddVO;
import com.hk.vo.user.UserEditVO;
import com.hk.vo.user.UserVO;

import java.time.LocalDate;
import java.util.Map;

/**
* <p>
    * 用户表 服务类
    * </p>
*
* @author hk
* @since 2025-06-22
*/
public interface UserService extends IService<UserEntity> {

    Page<UserVO> getUserList(UserSearchParam userSearchParam);

    UserVO getInfo(Long id);

    boolean saveUser(UserAddVO addVO);

    boolean transStatus(Long userId, Integer status);

    UserEntity selectOneByAccount(String account);

    boolean updateUser(UserEditVO editVO);

    boolean updatePassword(String password, Long userId);

    boolean resetPassword(Long userId);

    boolean sign();
    boolean isSigned();

    int getMonthSignCount();

    int getContinuousSignCount();

    Map<LocalDate, Boolean> getSignRecord(Integer year);
}