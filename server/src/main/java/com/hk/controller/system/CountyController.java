package com.hk.controller.system;

import com.hk.common.ResponseResult;
import com.hk.service.system.CountyService;
import com.hk.vo.system.CountyVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 区县表 前端控制器
 * </p>
 *
 * @author hk
 * @since 2025-08-28
 */
@Tag(name = "区县管理")
@RestController
@RequestMapping("/county")
public class CountyController {
    
    @Autowired
    private CountyService countyService;

    @Operation(summary = "添加区县数据")
    @PostMapping("/add")
    @PreAuthorize("@ss.hasPermission('/county/add')")
    public ResponseResult<?> addCounty(@RequestBody CountyVO CountyVO) {
        Boolean result = countyService.addCounty(CountyVO);
        return result ? ResponseResult.success("添加成功") : ResponseResult.fail("添加失败");
    }

    @Operation(summary = "删除区县数据")
    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermission('/county/delete')")
    public ResponseResult<?> deleteCounty(@RequestBody Long id) {
        Boolean result = countyService.deleteCounty(id);
        return result ? ResponseResult.success("删除成功") : ResponseResult.fail("删除失败");
    }

    @Operation(summary = "更新区县数据")
    @PostMapping("/update")
    @PreAuthorize("@ss.hasPermission('/county/update')")
    public ResponseResult<?> updateCounty(@RequestBody CountyVO CountyVO) {
        Boolean result = countyService.updateCounty(CountyVO);
        return result ? ResponseResult.success("更新成功") : ResponseResult.fail("更新失败");
    }

    @Operation(summary = "查询区县数据")
    @GetMapping("/select")
    @PreAuthorize("@ss.hasPermission('/county/select')")
    public ResponseResult<?> selectCounty(@RequestParam Long id) {
        CountyVO CountyVO = countyService.selectCounty(id);
        return ResponseResult.success(CountyVO);
    }
    

}
