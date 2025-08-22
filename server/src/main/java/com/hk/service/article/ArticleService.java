package com.hk.service.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.article.ArticleEntity;
import com.hk.param.ArticleSearchParam;
import com.hk.vo.article.ArticleAuditVO;
import com.hk.vo.article.ArticleEditVO;
import com.hk.vo.article.ArticleVO;

import java.util.List;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author hk
 * @since 2025-07-29
 */
public interface ArticleService extends IService<ArticleEntity> {

    Boolean saveArticle(ArticleEditVO articleEditVO);

    Boolean deleteArticle(Long id);

    Boolean publishArticle(Long id);

    IPage<ArticleVO> selectArticlePageByAuthor(ArticleSearchParam param);

    IPage<ArticleVO> selectPassArticlePage(ArticleSearchParam param);

    ArticleVO selectArticleDetail(Long id);

    Boolean likeArticle(Long articleId, Integer isLike);

    Boolean articleLikeStatus(Long articleId);

    Boolean auditArticle(ArticleAuditVO auditVO);

    Integer articleLikeCount(Long articleId);

    Integer articleViewCount(Long articleId);

    Boolean addArticleViewCount(Long articleId);

    IPage<ArticleVO> selectArticlePage(ArticleSearchParam param);

    /**
     * 查询所有文章（最近多少分钟内的）
     * @param offsetMinute 偏移分钟
     * @return
     */
    List<ArticleVO> selectAll(Integer offsetMinute);

    IPage<ArticleVO> selectArticlePageFromEs(ArticleSearchParam param);
}
