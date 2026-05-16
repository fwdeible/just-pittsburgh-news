package com.pghNews.aggregator.social;

import com.pghNews.aggregator.AggregatorApplication;
import com.pghNews.aggregator.entity.Article;
import com.pghNews.aggregator.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PostQueueScheduler {

    public static final Integer SOCIAL_MEDIA_IMPORTANCE_THRESHOLD = 0;
    public static final Integer SOCIAL_MEDIA_HIGH_IMPORTANCE_THRESHOLD = 20;
    public static final Integer LOCAL_RELEVANCE_THRESHOLD = 5;


    @Autowired
    private ArticleRepository articleRepo;

    @Autowired
    private PostQueueRepo postQueueRepo;

    @Autowired
    private SocialMediaService socialMediaService;

    @Scheduled(fixedRate = 30000)
    public void addArticleToQueue() {
        log.info("running addArticleToQueue");

        List<Article> articles = articleRepo.findUnQueuedArticles(LOCAL_RELEVANCE_THRESHOLD, SOCIAL_MEDIA_IMPORTANCE_THRESHOLD);

        for(Article article : articles) {

            log.info("queueing article: " + article.getTitle() + " id: " + article.getId() );
            article.setIsQueued(true);
            articleRepo.save(article);

            PostQueueElement postQueueElement = new PostQueueElement();
            postQueueElement.setArticle(article);
            postQueueElement.setStatus("PENDING");
            postQueueElement.setPosted(false);
            postQueueElement.setScheduledAt(Timestamp.from(Instant.now()));
//            postQueueElement.setCreatedAt(Instant.now());


            postQueueRepo.save(postQueueElement);



        }
    }

    @Scheduled(fixedRate = 600000)
    public void postToTwitter() {
        log.info("attempting posting to twitter");

        PostQueueElement queue = postQueueRepo.findNextUnpostedArticle();

        if(!Objects.isNull(queue)) {
            String response = "NO RESPONSE";
            try {
                response = socialMediaService.postArticleToTwitter(queue.getArticle());
            } catch (Exception e) {
                log.error("Error posting to twitter", e);
            }

            queue.setStatus("POSTED");
            queue.setPosted(true);
            queue.setExternalId(response);

            postQueueRepo.save(queue);
        } else {
            log.info("no articles to post");
        }


    }
}
