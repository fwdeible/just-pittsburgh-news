package com.pghNews.aggregator.repository;

import com.pghNews.aggregator.dto.ArticleDTO;
import com.pghNews.aggregator.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findArticlesByProcessedIs(Boolean processed);

    List<Article> findArticlesByRelevanceScoreGreaterThan(Integer score);

    @NativeQuery(value = "select * from articles where relevance_score > 5 order by published_at  DESC LIMIT 100")
    List<Article> findRelevantArticlesSortedByPublishedAt();


    boolean existsArticlesByArticleHash(String articleHash);
}
