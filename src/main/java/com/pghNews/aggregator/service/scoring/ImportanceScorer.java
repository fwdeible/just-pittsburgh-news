package com.pghNews.aggregator.service.scoring;

import com.pghNews.aggregator.entity.Article;
import com.pghNews.aggregator.service.keywords.ImportanceKeywordConfig;
import com.pghNews.aggregator.service.keywords.ImportanceKeywordConfigService;
import com.pghNews.aggregator.service.keywords.KeywordRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class ImportanceScorer extends AbstractScorer {

    @Autowired
    ImportanceKeywordConfigService importanceKeywordConfigService;

    void rateImportance(List<Article> unprocessedArticles) {
        log.info("found unprocessedArticles for Importance scoring: " + unprocessedArticles.size());


        ImportanceKeywordConfig config = importanceKeywordConfigService.getConfig();
        List<KeywordRule> allRules =
                Stream.of(config.getImportance_boosts(),
                                config.getImportance_penalties()
                        )
                        .flatMap(List::stream)
                        .toList();

        super.runScoring(unprocessedArticles,  allRules);

        log.info("finished importance scoring");
    }

    public void setScore(Article article, Integer score) {
        article.setImportanceScore(score);
    }
}
