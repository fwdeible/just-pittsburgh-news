package com.pghNews.aggregator.controller;

import com.pghNews.aggregator.AggregatorApplication;
import com.pghNews.aggregator.dto.ArticleDTO;
import com.pghNews.aggregator.entity.Article;
import com.pghNews.aggregator.repository.ArticleRepository;
import com.pghNews.aggregator.service.readers.NewsReaderScheduler;
import com.pghNews.aggregator.service.scoring.ScoringScheduler;
import com.pghNews.aggregator.social.PostQueueScheduler;
import com.pghNews.aggregator.social.SocialMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    private SocialMediaService socialMediaService;


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
                        articleRepository.findRelevantArticlesSortedByPublishedAt(PostQueueScheduler.LOCAL_RELEVANCE_THRESHOLD)
                )
        , HttpStatus.OK);
    }

    @PostMapping("/postToTwitter")
    public ResponseEntity<String> postToTwittter(@Param("message") String message) {
        log.info("postToTwitter message: " + message );
        String response = "Didn't attempt to post";
        if (socialMediaService.getPostMessageToTwitter()) {
            response = socialMediaService.postToTwitter(message);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/togglePosting")
    public ResponseEntity<String> toggleSocialPosting(@RequestParam("enabled") Boolean enabled) {
        System.out.println("toggleSocialPosting enabled: " + enabled);
        socialMediaService.setPostMessageToTwitter(enabled);
        return new ResponseEntity<>("Toggled posting to SocialMedia to " + socialMediaService.getPostMessageToTwitter(), HttpStatus.OK);
    }

}
