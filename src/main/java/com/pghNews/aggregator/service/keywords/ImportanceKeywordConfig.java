package com.pghNews.aggregator.service.keywords;

import lombok.Data;

import java.util.List;

@Data
public class ImportanceKeywordConfig {

    private List<KeywordRule> importance_boosts;
    private List<KeywordRule> importance_penalties;


}
