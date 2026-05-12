package com.pghNews.aggregator.service.scoring;

import com.pghNews.aggregator.entity.Article;
import com.pghNews.aggregator.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ScoringScheduler {

    @Autowired
    ImportanceScorer importanceScorer;

    @Autowired
    RelevanceScorer relevanceScorer;

    @Autowired
    ArticleRepository articleRepository;


    @Scheduled(fixedRate = 30000)
    public void runScoring() {
        log.info("running scoring");
        List<Article> articles = articleRepository.findArticlesByProcessedIs(false);

        this.runScoring(articles);

    }

    public void reprocessAllScoring() {
        List<Article> articles = articleRepository.findAll();

        this.runScoring(articles);
    }

    private void runScoring(List<Article> articles) {
        importanceScorer.rateImportance(articles);
        relevanceScorer.rateLocalRelevance(articles);

        articleRepository.saveAll(articles);
    }

}
