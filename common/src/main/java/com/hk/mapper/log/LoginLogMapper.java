package com.hk.mapper.log;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.entity.log.LoginLogEntity;
import org.apache.ibatis.annotations.Mapper;
/**
 * <p>
 * 登录日志表 Mapper 接口
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLogEntity> {

}
