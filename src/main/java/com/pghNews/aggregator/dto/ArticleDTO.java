package com.pghNews.aggregator.dto;

import com.pghNews.aggregator.entity.Article;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ArticleDTO {

        String source;
        String sourceUrl;
        String title;
        String description;
        String content;
        String articleUrl;
        String author;
        Instant publishedAt;
        Integer relevanceScore;
        Integer importanceScore;
        String matchedKeywords;


        public static List<ArticleDTO> convertArticleEntitiesToDtos(List<Article> entities) {
                List<ArticleDTO> dtos = new ArrayList<>();
                for(Article article : entities) {
                        ArticleDTO dto = new ArticleDTO();
                        dto.setSource(article.getSource());
                        dto.setTitle(article.getTitle());
                        dto.setDescription(article.getDescription());
                        dto.setContent(article.getContent());
                        dto.setArticleUrl(article.getArticleUrl());
                        dto.setAuthor(article.getAuthor());
                        dto.setRelevanceScore(article.getRelevanceScore());
                        dto.setImportanceScore(article.getImportanceScore());
                        dto.setPublishedAt(article.getPublishedAt());
                        dto.setMatchedKeywords(article.getMatchedKeywords());

                        dtos.add(dto);

                }
                return dtos;
        }


}
