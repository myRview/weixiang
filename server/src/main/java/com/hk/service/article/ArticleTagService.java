package com.hk.service.article;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.article.ArticleTagEntity;
import com.hk.vo.article.ArticleTagVO;

import java.util.List;

/**
 * <p>
 * 文章标签表 服务类
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
public interface ArticleTagService extends IService<ArticleTagEntity> {

    boolean deleteByArticleId(Long articleId);

    List<ArticleTagVO> selectList(List<Long> tagIds);

    List<ArticleTagVO> selectListByArticleId(Long articleId);
}
