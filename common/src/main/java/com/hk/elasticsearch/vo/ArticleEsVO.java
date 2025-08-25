package com.hk.elasticsearch.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.hk.vo.article.ArticleVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 文章ES存储对象
 *
 * @author 20231
 */
@Data
@Document(indexName = "article")
public class ArticleEsVO implements Serializable {
    private static final long serialVersionUID = -4372481825355206039L;

    @Schema(description = "主键")
    @Id
    private String id;

    @Schema(description = "标题")
    @Field(name = "title", type = FieldType.Text)
    private String title;

    @Schema(description = "内容")
    @Field(name = "content", type = FieldType.Text)
    private String content;

    @Schema(description = "用户id")
    @Field(name = "userId", type = FieldType.Keyword)
    private String userId;

    @Schema(description = "状态，0-草稿，1-已发布")
    @Field(name = "publishStatus", type = FieldType.Integer)
    private Integer publishStatus;

    @Schema(description = "审核状态，0-待审核，1-审核通过，2-审核不通过")
    @Field(name = "auditStatus", type = FieldType.Integer)
    private Integer auditStatus;

    @Schema(description = "审核原因")
    private String auditReason;

    @Schema(description = "浏览量")
    private Integer viewCount;

    @Schema(description = "点赞量")
    private Integer likeCount;

    @Schema(description = "分类id")
    @Field(name = "categoryId", type = FieldType.Keyword)
    private String categoryId;

    @Schema(description = "创建时间")
    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description = "标签id集合")
    @Field(name = "tagIds", type = FieldType.Keyword)
    private List<String> tagIds;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "用户头像")
    private String userAvatar;

    /**
     * 将文章VO转换为ES存储对象
     *
     * @param articleVO
     * @return
     */
    public static ArticleEsVO convertToEsVO(ArticleVO articleVO) {
        if (articleVO == null) return null;
        ArticleEsVO articleEsVO = new ArticleEsVO();
        articleEsVO.setId(String.valueOf(articleVO.getId()));
        articleEsVO.setTitle(articleVO.getTitle());
        articleEsVO.setContent(articleVO.getContent());
        articleEsVO.setUserId(String.valueOf(articleVO.getUserId()));
        articleEsVO.setPublishStatus(articleVO.getPublishStatus());
        articleEsVO.setAuditStatus(articleVO.getAuditStatus());
        articleEsVO.setAuditReason(articleVO.getAuditReason());
        articleEsVO.setViewCount(articleVO.getViewCount());
        articleEsVO.setLikeCount(articleVO.getLikeCount());
        articleEsVO.setCategoryId(String.valueOf(articleVO.getCategoryId()));
        articleEsVO.setCreateTime(articleVO.getCreateTime());
        if (CollectionUtil.isNotEmpty(articleVO.getTagIds())) {
            List<String> tagIds = articleVO.getTagIds().stream().map(String::valueOf).toList();
            articleEsVO.setTagIds(tagIds);
        }
        articleEsVO.setUserName(articleVO.getUserName());
        articleEsVO.setUserAvatar(articleVO.getUserAvatar());
        return articleEsVO;
    }

    /**
     * 将ES存储对象转换为实体对象
     *
     * @param articleEsVO
     * @return
     */
    public static ArticleVO convertToVO(ArticleEsVO articleEsVO) {
        if (articleEsVO == null) return null;
        ArticleVO articleVO = new ArticleVO();
        articleVO.setId(Long.valueOf(articleEsVO.getId()));
        articleVO.setTitle(articleEsVO.getTitle());
        articleVO.setContent(articleEsVO.getContent());
        if (StringUtils.isNotBlank(articleEsVO.getUserId())) {
            articleVO.setUserId(Long.valueOf(articleEsVO.getUserId()));
        }
        articleVO.setPublishStatus(articleEsVO.getPublishStatus());
        articleVO.setAuditStatus(articleEsVO.getAuditStatus());
        articleVO.setAuditReason(articleEsVO.getAuditReason());
        articleVO.setViewCount(articleEsVO.getViewCount());
        articleVO.setLikeCount(articleEsVO.getLikeCount());
        if (StringUtils.isNotBlank(articleEsVO.getCategoryId())) {
            articleVO.setCategoryId(Long.valueOf(articleEsVO.getCategoryId()));
        }
        articleVO.setCreateTime(articleEsVO.getCreateTime());
        if (CollectionUtil.isNotEmpty(articleEsVO.getTagIds())) {
            List<Long> tagIds = articleEsVO.getTagIds().stream().map(Long::valueOf).toList();
            articleVO.setTagIds(tagIds);
        }
        articleVO.setUserName(articleEsVO.getUserName());
        articleVO.setUserAvatar(articleEsVO.getUserAvatar());

        return articleVO;
    }

}