package com.hk.service.article;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.article.ArticleCommentEntity;
import com.hk.vo.article.ArticleCommentVO;

import java.util.List;

/**
 * <p>
 * 文章评论表 服务类
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
public interface ArticleCommentService extends IService<ArticleCommentEntity> {

    List<ArticleCommentVO> getArticleCommentList(Long articleId);

    boolean addArticleComment(ArticleCommentVO articleCommentVO);

    boolean deleteArticleComment(Long id);
}
