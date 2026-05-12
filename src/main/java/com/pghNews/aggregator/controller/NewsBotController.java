package com.pghNews.aggregator.controller;

import com.pghNews.aggregator.dto.ArticleDTO;
import com.pghNews.aggregator.entity.Article;
import com.pghNews.aggregator.repository.ArticleRepository;
import com.pghNews.aggregator.service.readers.NewsReaderScheduler;
import com.pghNews.aggregator.service.scoring.ScoringScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Slf4j
public class NewsBotController {


    @Autowired
    private NewsReaderScheduler newsReaderScheduler;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ScoringScheduler scoringScheduler;

    @PostMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println("TEST TEST");

        return new ResponseEntity<String>("TEST SUCCESSFUL", HttpStatus.OK);
    }

    @PostMapping("/read")
    public ResponseEntity<List<ArticleDTO>> read() {

        List<ArticleDTO> articles = newsReaderScheduler.readAll();

        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/getLocalDbArticles")
    public ResponseEntity<List<Article>> getLocalDbArticles() {
        log.info("getLocalDbArticles");

        List<Article> dbArticles =  articleRepository.findAll();
        log.info("found dbArticles: " + dbArticles.size());
        return new ResponseEntity<>(dbArticles, HttpStatus.OK);
    }

    @PostMapping("/scoreArticles")
    public ResponseEntity<String> scoreUnprocessedArticles() {
        log.info("scoreUnprocessedArticles");
        scoringScheduler.reprocessAllScoring();
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @GetMapping("/getLocalArticles")
    public ResponseEntity<List<ArticleDTO>> getLocalArticles() {
        log.info("getLocalArticles");
        return new ResponseEntity<>(
                ArticleDTO.convertArticleEntitiesToDtos(
                        articleRepository.findRelevantArticlesSortedByPublishedAt()
                )
        , HttpStatus.OK);
    }
}
