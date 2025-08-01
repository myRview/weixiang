package com.hk.vo.article;

import com.hk.entity.article.ArticleTagEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 文章标签表
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Data
public class ArticleTagVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "文章id")
    private Long articleId;

    @Schema(description = "标签id")
    private Long tagId;

    public static ArticleTagVO convert(ArticleTagEntity articleTagEntity) {
        if (articleTagEntity==null) return null;
        ArticleTagVO articleTagVO = new ArticleTagVO();
        articleTagVO.setId(articleTagEntity.getId());
        articleTagVO.setArticleId(articleTagEntity.getArticleId());
        articleTagVO.setTagId(articleTagEntity.getTagId());
        return articleTagVO;
    }
}
