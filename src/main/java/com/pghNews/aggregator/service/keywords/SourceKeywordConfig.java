package com.pghNews.aggregator.service.keywords;

import lombok.Data;

import java.util.List;

@Data
public class SourceKeywordConfig {

    private List<SourceRule> sources;

}
