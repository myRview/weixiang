package com.hk.controller.system;

import com.hk.common.ResponseResult;
import com.hk.context.UserContext;
import com.hk.entity.system.BlackIpEntity;
import com.hk.service.system.BlackIpService;
import com.hk.utils.BlackIpUtil;
import com.hk.vo.system.BlackIpVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 黑名单ip表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2025-08-25
 */
@Tag(name = "黑名单管理")
@RestController
@RequestMapping("/system/setting")
public class BlackIpController {

    @Autowired
    private BlackIpService blackIpService;

    @Operation(summary = "添加黑名单ip")
    @PostMapping("/add/black/ip")
    @PreAuthorize("@ss.hasPermission('/system/setting/add/black/ip')")
    public ResponseResult addBlackIp(String ip) {
        if (StringUtils.isBlank(ip)) {
            return ResponseResult.fail("请输入ip");
        }
        BlackIpEntity entity = new BlackIpEntity();
        entity.setIp(ip);
        entity.setUserId(UserContext.getCurrentUserId());
        BlackIpUtil.addBlackIp(ip);
        return blackIpService.save(entity) ? ResponseResult.success("添加成功") : ResponseResult.fail("添加失败");
    }

    @Operation(summary = "移除黑名单ip")
    @PostMapping("/remove/black/ip")
    @PreAuthorize("@ss.hasPermission('/system/setting/remove/black/ip')")
    public ResponseResult removeBlackIp(Long id) {
        if (id == null) {
            return ResponseResult.fail("请输入id");
        }
        blackIpService.removeById(id);
        return blackIpService.removeById(id) ? ResponseResult.success("移除成功") : ResponseResult.fail("移除失败");
    }

    @Operation(summary = "获取所有黑名单ip")
    @PostMapping("/all/black/ip")
    @PreAuthorize("@ss.hasPermission('/system/setting/all/black/ip')")
    public ResponseResult<List<BlackIpVO>> selectBlackIpAll() {
        return ResponseResult.success(blackIpService.selectAll());
    }


}
