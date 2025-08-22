package com.hk.task;

import cn.hutool.core.collection.CollectionUtil;
import com.hk.elasticsearch.vo.ArticleEsVO;
import com.hk.service.article.ArticleService;
import com.hk.vo.article.ArticleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 增量文章同步任务
 * @author huangkun
 * @date 2025/8/22 15:13
 */
@Component
@Slf4j
public class ArticleSyncTask {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    /**
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void run() {
        //获取5分钟内更新的文章数据
        final int  FIVE_MINUTES = 5 ;
        List<ArticleVO> articleVOS = articleService.selectAll(FIVE_MINUTES);
        if (CollectionUtil.isEmpty(articleVOS)){
            log.info("五分钟内没有更新的文章数据");
            return;
        }
        List<ArticleEsVO> articleEsVOS = articleVOS.stream().map(ArticleEsVO::convertToEsVO).collect(Collectors.toList());
        log.info("五分钟内更新的文章数据为:{}",articleEsVOS);
        elasticsearchOperations.save(articleEsVOS);
    }
}
