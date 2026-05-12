package com.pghNews.aggregator.service.readers;

import com.pghNews.aggregator.dto.ArticleDTO;
import com.pghNews.aggregator.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NewsReaderScheduler {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    FeedReader feedReader;


    @Scheduled(fixedRate = 60000)
    public List<ArticleDTO> readAll() {
        log.info("Starting News Reader readAll()");

        List<ArticleDTO> allArticles = feedReader.readAndStoreAllFeeds();

        return allArticles;
    }


}
