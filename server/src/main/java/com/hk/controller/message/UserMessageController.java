package com.hk.controller.message;

import com.hk.common.ResponseResult;
import com.hk.service.message.UserMessageService;
import com.hk.vo.message.UserMessageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author huangkun
 * @date 2025/8/6 17:52
 */
@Tag(name = "用户消息管理")
@RestController
@RequestMapping("/user/message")
public class UserMessageController {

    @Autowired
    private UserMessageService userMessageService;

    /**
     * 删除用户消息
     */
    @PostMapping("/delete")
    @Operation(summary = "删除用户消息")
//    @PreAuthorize("@ss.hasPermission('/user/message/delete')")
    public ResponseResult deleteMessage(@RequestBody List<Long> messageIds) {
        return userMessageService.deleteMessage(messageIds) ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }

    /**
     * 查询详情
     */
    @GetMapping("/get")
    @Operation(summary = "查询用户消息详情")
//    @PreAuthorize("@ss.hasPermission('/user/message/get')")
    public ResponseResult<UserMessageVO> getMessageById(@RequestParam Long id) {
        return ResponseResult.success(userMessageService.getMessageById(id));
    }

    /**
     * 查询列表
     */
    @PostMapping("/page")
    @Operation(summary = "查询用户消息列表")
//    @PreAuthorize("@ss.hasPermission('/user/message/page')")
    public ResponseResult<List<UserMessageVO>> selectMessageList(Long userId) {
        return ResponseResult.success(userMessageService.getMessageList(userId));
    }

    /**
     * 设置已读
     */
    @PostMapping("/read")
    @Operation(summary = "设置用户消息已读")
//    @PreAuthorize("@ss.hasPermission('/user/message/read')")
    public ResponseResult readMessage(@RequestBody List<Long> messageIds) {
        return userMessageService.readMessage(messageIds) ? ResponseResult.success("设置成功") : ResponseResult.fail("设置失败");
    }
}
