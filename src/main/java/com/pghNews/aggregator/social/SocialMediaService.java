package com.pghNews.aggregator.social;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.pghNews.aggregator.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class SocialMediaService {



    @Autowired
    private TwitterProperties twitterProperties;


    private boolean postMessageToTwitter = true;


    public String postArticleToTwitter(Article article) {
        return this.postToTwitter(article.getTitle() + " " +  article.getArticleUrl());
    }

    public String postToTwitter(String message) {
        log.info("posting to twitter: " + message + " postMessageToTwitter is on: " + postMessageToTwitter);

        String responseMessage = "FAIL";

        if(!postMessageToTwitter) {
            log.info("Posting to Twitter is disabled.");
            responseMessage = "Posting to Twitter is disabled.";
        }



        else  {
            System.out.println("twitter key isNull check: " + (Objects.isNull(twitterProperties.getApiKey()) || Objects.isNull(twitterProperties.getApiSecret()) || Objects.isNull(twitterProperties.getAccessToken()) || Objects.isNull(twitterProperties.getAccessSecret())));
            OAuth10aService service =
                    new ServiceBuilder(twitterProperties.getApiKey())
                            .apiSecret(twitterProperties.getApiSecret())
                            .build(TwitterApi.instance());


        OAuth1AccessToken accessToken =
                new OAuth1AccessToken(
                        twitterProperties.getAccessToken(),
                        twitterProperties.getAccessSecret()
                );

        OAuthRequest request =
                new OAuthRequest(
                        Verb.POST,
                        "https://api.twitter.com/2/tweets"
                );

        request.addHeader("Content-Type", "application/json");

        String payload = """
                {
                  "text": "%s"
                }
                """.formatted(message);

        request.setPayload(payload);

        service.signRequest(accessToken, request);

        Response response = null;
        try {
            response = service.execute(request);
            System.out.println(response.getCode());
            System.out.println(response.getBody());

            responseMessage = response.getBody();
        } catch (InterruptedException e) {
            log.error("Interrupted while posting to Twitter", e);
        } catch (ExecutionException e) {
            log.error("Error while posting to Twitter", e);
        } catch (IOException e) {
            log.error("Error while posting to Twitter", e);
        }

    }

        return responseMessage;
    }

    public void setPostMessageToTwitter(Boolean postMessageToTwitter) {
        this.postMessageToTwitter = postMessageToTwitter;
    }
    public boolean getPostMessageToTwitter() {
        return this.postMessageToTwitter;
    }
}
