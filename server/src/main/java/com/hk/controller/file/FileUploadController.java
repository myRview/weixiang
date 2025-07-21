package com.hk.controller.file;

import com.hk.common.ResponseResult;
import com.hk.service.file.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author huangkun
 * @date 2025/7/20 19:43
 */
@Tag(name = "文件上传")
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    @Operation(summary = "文件上传")
    public ResponseResult<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("category") String category) {
        String fileUrl = fileUploadService.upload(file, category);
        return ResponseResult.success(fileUrl, "上传成功");
    }
}
