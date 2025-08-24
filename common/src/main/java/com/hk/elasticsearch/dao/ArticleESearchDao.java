package com.hk.elasticsearch.dao;

import com.hk.elasticsearch.vo.ArticleEsVO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author huangkun
 * @date 2025/8/21 15:38
 */
public interface ArticleESearchDao extends ElasticsearchRepository<ArticleEsVO, Long> {
}
