package com.pghNews.aggregator.service.keywords;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;

@Service
public class RelevanceKeywordConfigService {

    private final ObjectMapper objectMapper;

    @Getter
    private RelevanceKeywordConfig config;

    public RelevanceKeywordConfigService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadConfig()  {

        InputStream inputStream =
                getClass()
                        .getResourceAsStream("/relevance_keywords.json");

        config = objectMapper.readValue(
                inputStream,
                RelevanceKeywordConfig.class
        );
    }

}
