package com.hk.service.log;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.log.LoginLogEntity;
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

    IPage<LoginLogVO> selectLoginPage(LogSearchParam searchParam);

    LoginLogVO getInfoById(Long id);

    boolean addLog(LoginLogVO loginLogVO);
}