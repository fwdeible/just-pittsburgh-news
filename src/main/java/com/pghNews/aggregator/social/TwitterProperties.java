package com.pghNews.aggregator.social;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "twitter")
@Getter @Setter
public class TwitterProperties {

    private String accessToken;
    private String accessSecret;
    private String apiKey;
    private String apiSecret;

}
