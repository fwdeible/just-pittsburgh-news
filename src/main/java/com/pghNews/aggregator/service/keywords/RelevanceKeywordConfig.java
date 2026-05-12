package com.pghNews.aggregator.service.keywords;

import lombok.Data;

import java.util.List;

@Data
public class RelevanceKeywordConfig {

    private List<KeywordRule> counties;
    private List<KeywordRule> cities_and_towns;
    private List<KeywordRule> neighborhoods;
    private List<KeywordRule> roads;
    private List<KeywordRule> institutions;
    private List<KeywordRule> sports;
    private List<KeywordRule> negative_keywords;

}
