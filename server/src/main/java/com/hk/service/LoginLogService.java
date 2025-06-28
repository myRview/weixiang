package com.hk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.entity.LoginLogEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.param.LogSearchParam;
import com.hk.vo.log.LoginLogVO;

/**
* <p>
    * 登录日志表 服务类
    * </p>
*
* @author hk
* @since 2025-06-22
*/
public interface LoginLogService extends IService<LoginLogEntity> {

    Page<LoginLogVO> selectLoginPage(LogSearchParam searchParam);

    LoginLogVO getInfoById(Long id);
}