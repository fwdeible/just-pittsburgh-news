package com.pghNews.aggregator.service;

import com.pghNews.aggregator.dto.ArticleDTO;
import com.pghNews.aggregator.entity.Article;
import com.pghNews.aggregator.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
public class ArticleProcessingService {

    @Autowired
    private ArticleRepository articleRepository;

    public void processArticleDto(ArticleDTO dto) throws NoSuchAlgorithmException {
        Article article = Article.createArticleFromDTO(dto);


        if (!articleRepository.existsArticlesByArticleHash(article.getArticleHash()) ) {

            log.info("Saving article: " + article.getTitle());
            articleRepository.save(article);
        }

    }
}
