package com.hk;

import cn.hutool.core.collection.CollectionUtil;
import com.hk.elasticsearch.dao.ArticleESearchDao;
import com.hk.elasticsearch.vo.ArticleEsVO;
import com.hk.service.article.ArticleService;
import com.hk.vo.article.ArticleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文章同步(只需要第一次启动时会执行一次)
 * @author huangkun
 * @date 2025/8/21 15:41
 */
//@Component
@Slf4j
public class ArticleSyncRunner implements CommandLineRunner {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleESearchDao articleESearchDao;

    @Override
    public void run(String... args) throws Exception {
        //获取所有文章  TODO:数据量大的话可以进行优化
        List<ArticleVO> articleVOS = articleService.selectAll(null);
        if (CollectionUtil.isEmpty(articleVOS)) {
            log.error("没有需要同步的文章数据");
            return;
        }
        //转换为es对象
        List<ArticleEsVO> articleEsVOS = articleVOS.parallelStream().map(ArticleEsVO::convertToEsVO).toList();
        //批量插入
        int batchSize = 200;
        for (int i = 0; i < articleEsVOS.size(); i += batchSize) {
            List<ArticleEsVO> batch = articleEsVOS.subList(i, Math.min(i + batchSize, articleEsVOS.size()));
            articleESearchDao.saveAll(batch);
        }
        log.info("文章同步完成,共{}条", articleEsVOS.size());
    }
}
