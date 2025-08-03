package com.hk.vo.article;

import com.hk.entity.article.ArticleCommentEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文章评论表
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
@Data
public class ArticleCommentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "文章id")
    private Long articleId;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "用户头像")
    private String userAvatar;
    public static ArticleCommentVO convert(ArticleCommentEntity articleCommentEntity) {
        if (articleCommentEntity == null) return null;
        ArticleCommentVO articleCommentVO = new ArticleCommentVO();
        articleCommentVO.setId(articleCommentEntity.getId());
        articleCommentVO.setArticleId(articleCommentEntity.getArticleId());
        articleCommentVO.setUserId(articleCommentEntity.getUserId());
        articleCommentVO.setContent(articleCommentEntity.getContent());
        articleCommentVO.setCreateTime(articleCommentEntity.getCreateTime());
        return articleCommentVO;
    }
}
