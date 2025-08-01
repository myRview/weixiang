package com.hk.service.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.article.ArticleEntity;
import com.hk.param.ArticleSearchParam;
import com.hk.vo.article.ArticleAuditVO;
import com.hk.vo.article.ArticleEditVO;
import com.hk.vo.article.ArticleVO;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
public interface ArticleService extends IService<ArticleEntity> {

    boolean saveArticle(ArticleEditVO articleEditVO);

    boolean deleteArticle(Long id);

    boolean publishArticle(Long id);

    IPage<ArticleVO> selectArticlePageByAuthor(ArticleSearchParam param);


    IPage<ArticleVO> selectPassArticlePage(ArticleSearchParam param);

    ArticleVO selectArticleDetail(Long id);

    boolean likeArticle(Long articleId, Integer isLike);

    boolean auditArticle(ArticleAuditVO auditVO);


    IPage<ArticleVO> selectArticlePage(ArticleSearchParam param);

    boolean articleLikeStatus(Long articleId);
}
