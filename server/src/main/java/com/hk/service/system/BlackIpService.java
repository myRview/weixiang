package com.hk.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.system.BlackIpEntity;
import com.hk.vo.system.BlackIpVO;

import java.util.List;

/**
 * <p>
 * 黑名单ip表 服务类
 * </p>
 *
 * @author hk
 * @since 2025-08-25
 */
public interface BlackIpService extends IService<BlackIpEntity> {

    List<BlackIpVO> selectAll();
}
