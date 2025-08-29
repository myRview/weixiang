package com.hk.controller.system;

import com.hk.common.ResponseResult;
import com.hk.service.system.CityService;
import com.hk.vo.system.CityVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 城市表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2025-08-28
 */
@Tag(name = "城市管理")
@RestController
@RequestMapping("/city")
public class CityController {
    
    @Autowired
    private CityService cityService;

    @Operation(summary = "添加区县数据")
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermission('/system/setting')")
    public ResponseResult<?> addCity(@RequestBody CityVO CityVO) {
        Boolean result = cityService.addCity(CityVO);
        return result ? ResponseResult.success("添加成功") : ResponseResult.fail("添加失败");
    }

    @Operation(summary = "删除区县数据")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermission('/system/setting')")
    public ResponseResult<?> deleteCity(@RequestBody Long id) {
        Boolean result = cityService.deleteCity(id);
        return result ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }

    @Operation(summary = "更新区县数据")
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermission('/system/setting')")
    public ResponseResult<?> updateCity(@RequestBody CityVO CityVO) {
        Boolean result = cityService.updateCity(CityVO);
        return result ? ResponseResult.success("更新成功") : ResponseResult.fail("更新失败");
    }

    @Operation(summary = "查询区县数据")
    @GetMapping("/select")
    public ResponseResult<?> selectCity(@RequestParam Long id) {
        CityVO CityVO = cityService.selectOne(id);
        return ResponseResult.success(CityVO);
    }

}
