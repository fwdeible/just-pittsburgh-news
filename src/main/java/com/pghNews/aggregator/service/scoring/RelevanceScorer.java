package com.pghNews.aggregator.service.scoring;

import com.pghNews.aggregator.entity.Article;
import com.pghNews.aggregator.service.keywords.RelevanceKeywordConfig;
import com.pghNews.aggregator.service.keywords.RelevanceKeywordConfigService;
import com.pghNews.aggregator.service.keywords.KeywordRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class RelevanceScorer extends AbstractScorer {

    @Autowired
    RelevanceKeywordConfigService relevanceKeywordConfigService;

    void rateLocalRelevance(List<Article> unprocessedArticles) {
        log.info("found unprocessedArticles for local relevance scoring: " + unprocessedArticles.size());

        RelevanceKeywordConfig config = relevanceKeywordConfigService.getConfig();


        List<KeywordRule> allRules =
                Stream.of(config.getCounties(),
                                config.getCities_and_towns(),
                                config.getNeighborhoods(),
                                config.getRoads(),
                                config.getInstitutions(),
                                config.getSports(),
                                config.getNegative_keywords()
                        )
                        .flatMap(List::stream)
                        .toList();

        super.runScoring(unprocessedArticles,  allRules);

    }

    public void  setScore(Article article, Integer score) {
        article.setRelevanceScore(score);
    }

}
