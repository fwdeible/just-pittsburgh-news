package com.pghNews.aggregator.service.scoring;

import com.pghNews.aggregator.AggregatorApplication;
import com.pghNews.aggregator.entity.Article;
import com.pghNews.aggregator.repository.ArticleRepository;
import com.pghNews.aggregator.social.PostQueueScheduler;
import com.pghNews.aggregator.social.SocialMediaService;
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

    @Autowired
    SocialMediaService socialMediaService;


    @Scheduled(fixedRate = 30000)
    public void runScoring() {
        log.info("running scoring");
        List<Article> articles = articleRepository.findArticlesByProcessedIs(false);

        this.runScoring(articles);

        for(Article article : articles) {
            if(article.getRelevanceScore() > PostQueueScheduler.LOCAL_RELEVANCE_THRESHOLD
                    && article.getImportanceScore() > PostQueueScheduler.SOCIAL_MEDIA_HIGH_IMPORTANCE_THRESHOLD) {
                // send to socials right now
                socialMediaService.postArticleToTwitter(article);
                article.setIsQueued(true);
                articleRepository.save(article);
            }
        }

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
