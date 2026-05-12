package com.pghNews.aggregator.service.keywords;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;

@Service
public class SourceKeywordConfigService {

    private final ObjectMapper objectMapper;

    @Getter
    private SourceKeywordConfig config;

    public SourceKeywordConfigService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadConfig()  {

        InputStream inputStream =
                getClass()
                        .getResourceAsStream("/sources.json");

        config = objectMapper.readValue(
                inputStream,
                SourceKeywordConfig.class
        );

    }

}
