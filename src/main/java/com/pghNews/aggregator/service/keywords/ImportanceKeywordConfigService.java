package com.pghNews.aggregator.service.keywords;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;

@Service
public class ImportanceKeywordConfigService {

    private final ObjectMapper objectMapper;

    @Getter
    private ImportanceKeywordConfig config;

    public ImportanceKeywordConfigService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadConfig()  {

        InputStream inputStream =
                getClass()
                        .getResourceAsStream("/importance_keywords.json");

        config = objectMapper.readValue(
                inputStream,
                ImportanceKeywordConfig.class
        );
    }

}
