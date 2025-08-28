package com.hk.controller.system;

import com.hk.common.ResponseResult;
import com.hk.service.system.ProvinceService;
import com.hk.vo.system.BlackIpVO;
import com.hk.vo.system.ProvinceVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 省份表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2025-08-28
 */
@Tag(name = "省份管理")
@RestController
@RequestMapping("/province")
public class ProvinceController {

    @Autowired
    private ProvinceService provinceService;

    @Operation(summary = "获取所有省份数据（包含下级数据）")
    @PostMapping("/all")
    @PreAuthorize("@ss.hasPermission('/province/all')")
    public ResponseResult<List<ProvinceVO>> getAllProvince() {
        List<ProvinceVO> provinceVOS = provinceService.getAllProvince();
        return ResponseResult.success(provinceVOS);
    }

    @Operation(summary = "添加省份数据")
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermission('/province/add')")
    public ResponseResult<?> addProvince(@RequestBody ProvinceVO provinceVO) {
        Boolean result = provinceService.addProvince(provinceVO);
        return result ? ResponseResult.success("添加成功") : ResponseResult.fail("添加失败");
    }

    @Operation(summary = "删除省份数据")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermission('/province/delete')")
    public ResponseResult<?> deleteProvince(@RequestBody Long id) {
        Boolean result = provinceService.deleteProvince(id);
        return result ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }

    @Operation(summary = "更新省份数据")
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermission('/province/update')")
    public ResponseResult<?> updateProvince(@RequestBody ProvinceVO provinceVO) {
        Boolean result = provinceService.updateProvince(provinceVO);
        return result ? ResponseResult.success("更新成功") : ResponseResult.fail("更新失败");
    }

    @Operation(summary = "查询省份数据")
    @GetMapping("/select")
    @PreAuthorize("@ss.hasPermission('/province/select')")
    public ResponseResult<?> selectProvince(@RequestParam Long id) {
        ProvinceVO provinceVO = provinceService.selectProvince(id);
        return ResponseResult.success(provinceVO);
    }


}
