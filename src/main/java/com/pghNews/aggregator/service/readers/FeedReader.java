package com.pghNews.aggregator.service.readers;

import com.pghNews.aggregator.dto.ArticleDTO;
import com.pghNews.aggregator.entity.Article;
import com.pghNews.aggregator.repository.ArticleRepository;
import com.pghNews.aggregator.service.ArticleProcessingService;
import com.pghNews.aggregator.service.keywords.SourceKeywordConfigService;
import com.pghNews.aggregator.service.keywords.SourceRule;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FeedReader {

    @Autowired
    SourceKeywordConfigService sourceKeywordConfigService;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    private ArticleProcessingService articleProcessingService;

    public List<ArticleDTO> readAndStoreAllFeeds() {
        List<ArticleDTO> articles = new ArrayList<>();
        for(SourceRule source : sourceKeywordConfigService.getConfig().getSources()) {
            articles.addAll(readAndStoreFeed(source));
        }

        return articles;
    }


    public List<ArticleDTO> readAndStoreFeed(SourceRule source) {
        log.info("Checking feed: " + source.getName());

        List<ArticleDTO> articles = new ArrayList<>();

        try {

            URL feedSource = new URL(source.getUrl());

            SyndFeedInput input = new SyndFeedInput();

            SyndFeed feed = input.build(new XmlReader(feedSource));

            log.info("Feed: " + source.getName() + " has " + feed.getEntries().size() + " entries");

            for (SyndEntry entry : feed.getEntries()) {

                ArticleDTO dto = new ArticleDTO();


                dto.setTitle(entry.getTitle());

                dto.setArticleUrl(entry.getLink());

                dto.setSource(source.getName());

                if (entry.getDescription() != null) {

                    String clean =
                            Jsoup.parse(
                                    entry.getDescription().getValue()
                            ).text();

                    dto.setDescription(clean);

                }

                if (entry.getPublishedDate() != null) {
                    dto.setPublishedAt(
                            entry.getPublishedDate()
                                    .toInstant()
                    );
                }

                articleProcessingService.processArticleDto(dto);

                articles.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return articles;
    }

}
