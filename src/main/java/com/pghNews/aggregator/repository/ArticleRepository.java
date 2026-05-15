package com.pghNews.aggregator.repository;

import com.pghNews.aggregator.AggregatorApplication;
import com.pghNews.aggregator.dto.ArticleDTO;
import com.pghNews.aggregator.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findArticlesByProcessedIs(Boolean processed);

    List<Article> findArticlesByRelevanceScoreGreaterThan(Integer score);

    @NativeQuery(value = "select * from insurance_app.articles where relevance_score > :threshold order by published_at  DESC LIMIT 100")
    List<Article> findRelevantArticlesSortedByPublishedAt(@Param("threshold") Integer threshold);


    boolean existsArticlesByArticleHash(String articleHash);


    @NativeQuery(value = "select * from insurance_app.articles where relevance_score > :relevanceThreshold and importance_score > :importanceThreshold and is_queued = false LIMIT 10")
    List<Article> findUnQueuedArticles(@Param("relevanceThreshold") Integer relevanceThreshold, @Param("importanceThreshold") Integer importanceThreshold);
}
