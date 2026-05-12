package com.pghNews.aggregator.service.keywords;

import lombok.Data;

@Data
public class SourceRule {

    private String name;
    private String url;
    private int relevanceScore;
    private int importanceScore;

}
