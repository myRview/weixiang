package com.hk.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hk.common.ResponseResult;
import com.hk.entity.user.PermissionEntity;
import com.hk.param.PermissionSearchParam;
import com.hk.service.user.PermissionService;
import com.hk.service.user.RoleService;
import com.hk.vo.user.PermissionVO;
import com.hk.vo.user.RolePermissionVO;
import com.hk.vo.user.RoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色权限表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2025-06-30
 */
@Tag(name = "角色权限管理")
@RestController
@RequestMapping
public class RolePermissionController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @PostMapping("/role/save")
    @Operation(summary = "添加角色")
    public ResponseResult saveRole(@RequestBody RoleVO roleVO) {
        return roleService.saveRole(roleVO) ? ResponseResult.success("添加成功") : ResponseResult.fail("添加失败");
    }

    @DeleteMapping("/role/{id}")
    @Operation(summary = "删除角色")
    public ResponseResult deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id) ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }

    @GetMapping("/role/{id}")
    @Operation(summary = "查询角色权限详情")
    public ResponseResult<List<PermissionVO>> getRolePermission(@PathVariable Long id) {
        return ResponseResult.success(roleService.getRolePermission(id));
    }

    @GetMapping("/role/list")
    @Operation(summary = "查询角色列表")
    public ResponseResult<List<RoleVO>> selectAllRole() {
        return ResponseResult.success(roleService.getAll());
    }


    @PostMapping("/permission/save")
    @Operation(summary = "添加权限")
    public ResponseResult savePermission(@RequestBody PermissionVO permissionVO) {
        PermissionEntity permission = new PermissionEntity();
        permission.setPermissionName(permissionVO.getPermissionName());
        permission.setPermissionCode(permissionVO.getPermissionCode());
        return permissionService.save(permission) ? ResponseResult.success("添加成功") : ResponseResult.fail("添加失败");
    }

    @PostMapping("/permission/delete")
    @Operation(summary = "删除权限")
    public ResponseResult deletePermission(@RequestBody List<Long> permissionIds) {
        return permissionService.removeBatchByIds(permissionIds) ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }

    @PostMapping("/permission/list")
    @Operation(summary = "权限列表")
    public ResponseResult<IPage<PermissionVO>> getPermissionList(@RequestBody PermissionSearchParam searchParam) {
        return ResponseResult.success(permissionService.getPermissionList(searchParam));
    }

    @PostMapping("/role/permission")
    @Operation(summary = "角色添加权限")
    public ResponseResult saveRolePermission(@RequestBody RolePermissionVO rolePermissionVO) {
        boolean result = roleService.saveRolePermission(rolePermissionVO);
        return result ? ResponseResult.success("添加成功") : ResponseResult.fail("添加失败");
    }
}