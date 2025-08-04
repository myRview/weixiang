package com.hk.controller.follow;

import com.hk.common.ResponseResult;
import com.hk.service.follow.UserFollowService;
import com.hk.vo.user.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author huangkun
 * @date 2025/8/4 14:46
 */
@Tag(name = "关注管理")
@RestController
@RequestMapping("/follow")
public class UserFollowController {

    @Autowired
    private UserFollowService userFollowService;

    @PostMapping("/chang/status")
    @Operation(summary = "关注/取消关注")
    public ResponseResult<Boolean> changStatus(Long userId, Integer status) {
        return ResponseResult.success(userFollowService.changStatus(userId, status));
    }

    //获取关注列表
    @PostMapping("/list")
    @Operation(summary = "获取关注列表")
    public ResponseResult<List<UserVO>> list(Long userId) {
        return ResponseResult.success(userFollowService.list(userId));
    }

    //获取粉丝列表
    @PostMapping("/fans/list")
    @Operation(summary = "获取粉丝列表")
    public ResponseResult<List<UserVO>> fansList(Long userId) {
        return ResponseResult.success(userFollowService.fansList(userId));
    }

    //获取关注状态
    @PostMapping("/status")
    @Operation(summary = "获取关注状态")
    public ResponseResult<Boolean> status(Long userId) {
        return ResponseResult.success(userFollowService.status(userId));
    }
}
