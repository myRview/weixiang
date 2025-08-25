package com.hk.service.article;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.article.TagEntity;
import com.hk.param.TagSearchParam;
import com.hk.vo.article.TagVO;

import java.util.List;

/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
public interface TagService extends IService<TagEntity> {

    boolean saveTag(TagVO tagVO);

    boolean updateTag(TagVO tagVO);

    List<TagVO> getTagList(String tagName);

    IPage<TagVO> selectTagPage(TagSearchParam param);

    List<TagVO> buildCache(List<TagEntity> tagList);

    boolean deleteTagById(Long id);
}
