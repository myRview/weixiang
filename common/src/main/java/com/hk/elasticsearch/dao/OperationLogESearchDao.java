package com.hk.elasticsearch.dao;

import com.hk.elasticsearch.vo.OperationLogEsVO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author huangkun
 * @date 2025/8/24 16:01
 */
public interface OperationLogESearchDao extends ElasticsearchRepository<OperationLogEsVO, Long> {
}
