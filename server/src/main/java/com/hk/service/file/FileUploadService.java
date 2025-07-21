package com.hk.service.file;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import com.hk.common.ErrorCode;
import com.hk.enums.FileCategoryEnum;
import com.hk.exception.BusinessException;
import com.hk.storage.file.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @author huangkun
 * @date 2025/7/20 20:36
 */
@Component
public class FileUploadService {

    private final Map<String, FileStorageService> fileStorageServiceMap;

    @Value("${weixiang.upload.type:tencent}")
    private String uploadType;

    public FileUploadService(Map<String, FileStorageService> fileStorageServiceMap) {
        this.fileStorageServiceMap = fileStorageServiceMap;
    }

    public String upload(MultipartFile file, String category) {

        FileCategoryEnum categoryEnum = FileCategoryEnum.getByCode(category);
        if (categoryEnum == null) throw new BusinessException(ErrorCode.BAD_REQUEST, "不支持的文件类型");

        String originalFilename = file.getOriginalFilename();
        //获取文件后缀名
        String fileSuffix = FileUtil.getSuffix(originalFilename);
        //上传文件
        String uuid = RandomUtil.randomString(16);
        String fileName = String.format("%s_%s.%s", DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN), uuid, fileSuffix);
        String filePath = String.format("%s/%s", category, fileName);
        try {
            File tempFile = File.createTempFile(filePath, null);
            file.transferTo(tempFile);
            FileStorageService storageService = fileStorageServiceMap.get(uploadType);
            switch (categoryEnum) {
                case IMAGE:
                    return storageService.uploadImage(filePath, tempFile);
                default:
                    return storageService.upload(filePath, tempFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.ERROR_SYSTEM, "文件上传失败");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.ERROR_SYSTEM, "文件上传失败");
        }
    }
}
