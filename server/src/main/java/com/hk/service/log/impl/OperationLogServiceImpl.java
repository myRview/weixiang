package com.hk.service.log.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.elasticsearch.vo.ArticleEsVO;
import com.hk.elasticsearch.vo.OperationLogEsVO;
import com.hk.entity.log.OperationLogEntity;
import com.hk.mapper.log.OperationLogMapper;
import com.hk.param.LogSearchParam;
import com.hk.service.log.OperationLogService;
import com.hk.vo.article.ArticleVO;
import com.hk.vo.log.OperationLogVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLogEntity> implements OperationLogService {
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Override
    public OperationLogVO getOperationLogById(Long id) {
        OperationLogEntity operationLog = this.getById(id);
        if (operationLog != null) {
            return OperationLogVO.converter(operationLog);
        }
        return null;
    }

    @Override
    public IPage<OperationLogVO> selectOperaLogPage(LogSearchParam searchParam) {
        String username = searchParam.getUsername();
        String ipAddress = searchParam.getIpAddress();
        Integer pageNum = searchParam.getPageNum();
        Integer pageSize = searchParam.getPageSize();
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(username)) {
            criteria.and(new Criteria("username").contains(username));
        }
        if (StringUtils.isNotBlank(ipAddress)) {
            criteria.and(new Criteria("ipAddress").contains(ipAddress));
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));
        CriteriaQuery query = new CriteriaQuery(criteria, pageable);
        SearchHits<OperationLogEsVO> searchHits = elasticsearchOperations.search(query, OperationLogEsVO.class);
        Page<OperationLogVO> pageResult = new Page<>(pageNum, pageSize);
        List<OperationLogVO> operationLogVOS = new ArrayList<>();
        if (searchHits.hasSearchHits()) {
            searchHits.getSearchHits().forEach(searchHit -> {
                OperationLogEsVO operationLogEsVO = searchHit.getContent();
                operationLogVOS.add(OperationLogEsVO.convertToVO(operationLogEsVO));
            });
        }
        pageResult.setTotal(searchHits.getTotalHits());
        pageResult.setRecords(operationLogVOS);
        return pageResult;
    }

    @Override
    public boolean addLog(OperationLogVO operationLog) {
        OperationLogEntity entity = new OperationLogEntity();
        entity.setIpAddress(operationLog.getIpAddress());
        entity.setOperationAddress(operationLog.getOperationAddress());
        entity.setOperationContent(operationLog.getOperationContent());
        entity.setOperationModule(operationLog.getOperationModule());
        entity.setOperationTime(new Date());
        entity.setStatus(operationLog.getStatus());
        entity.setUserId(operationLog.getUserId());
        entity.setUsername(operationLog.getUsername());
        return this.save(entity);
    }

    @Override
    public List<OperationLogVO> selectAll(Date recentDate) {
        LambdaQueryWrapper<OperationLogEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (recentDate!=null){
            queryWrapper.ge(OperationLogEntity::getOperationTime,recentDate);
        }
        queryWrapper.select(OperationLogEntity::getId,
                OperationLogEntity::getUsername,
                OperationLogEntity::getIpAddress,
                OperationLogEntity::getOperationAddress,
                OperationLogEntity::getOperationModule,
                OperationLogEntity::getOperationContent,
                OperationLogEntity::getCreateTime,
                OperationLogEntity::getUserId,
                OperationLogEntity::getStatus
        );
        List<OperationLogEntity> list = this.list(queryWrapper);
        return list.stream().map(OperationLogVO::converter).collect(Collectors.toList());
    }
}
