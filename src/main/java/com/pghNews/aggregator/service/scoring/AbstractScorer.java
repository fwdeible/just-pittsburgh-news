package com.pghNews.aggregator.service.scoring;


import com.pghNews.aggregator.entity.Article;
import com.pghNews.aggregator.service.keywords.KeywordRule;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractScorer {


    public abstract void setScore(Article article, Integer score);

    public void runScoring(List<Article> unprocessedArticles, List<KeywordRule> allRules) {
        for (Article article : unprocessedArticles) {
            String searchableText =
                    (article.getTitle() + " " +
                            article.getDescription())
                            .toLowerCase();

            searchableText =
                    searchableText.replaceAll("[^a-z0-9 ]", " ");


            int score = 0;
            List<String> matched = new ArrayList<>();

            for (KeywordRule rule : allRules) {

                if (searchableText.contains(rule.getKeyword())) {

                    score += rule.getScore();

                    matched.add(rule.getKeyword());
                }
            }

            article.setProcessed(true);

            article.setMatchedKeywords(matched.toString());
            setScore(article, score);
        }
    }
}
