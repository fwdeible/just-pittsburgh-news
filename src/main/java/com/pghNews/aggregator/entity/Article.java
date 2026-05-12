package com.pghNews.aggregator.entity;

import com.pghNews.aggregator.dto.ArticleDTO;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Table(name = "articles", schema="public")
@NoArgsConstructor
@Data
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Timestamp created_at;
    String source;
    String source_url;
    String title;
    String description;
    String content;
    String articleUrl;
    String author;
    Instant publishedAt;
    Instant ingested_at;
    Boolean processed;
    Boolean is_local;
    Integer relevanceScore;
    Integer importanceScore;
    String category;
    String matchedKeywords;
    String processing_notes;
    Integer processing_attempts;
    Timestamp last_processed_at;
    String articleHash;

    public static Article createArticleFromDTO(ArticleDTO dto) throws NoSuchAlgorithmException {
        Article article = new Article();
        article.setCreated_at(Timestamp.from(Instant.now()));
        article.setSource(dto.getSource());
        article.setSource_url(StringUtils.isNotEmpty(dto.getSourceUrl()) ? dto.getSourceUrl() : "NO SOURCE URL");
        String normalizedTitle = StringUtils.isNotEmpty(dto.getTitle()) ? dto.getTitle().trim().replaceAll("\\s", " ") : "NO TITLE";
        article.setTitle(normalizedTitle);
        article.setDescription(Optional.ofNullable(dto.getDescription())
                .map(s -> s.substring(0, Math.min(s.length(), 255)))
                .orElse("NO DESCRIPTION"));
        article.setContent(Optional.ofNullable(dto.getContent())
                .map(s -> s.substring(0, Math.min(s.length(), 255)))
                .orElse("NO CONTENT"));
        article.setProcessed(false);
        article.setArticleHash(article.generateSha256(normalizedTitle));
        article.setArticleUrl(dto.getArticleUrl());
        article.setImportanceScore(dto.getImportanceScore());
        article.setPublishedAt(dto.getPublishedAt());
        return article;
    }

    public String generateSha256(String originalString) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8));

        return bytesToHex(encodedHash);
    }


    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
