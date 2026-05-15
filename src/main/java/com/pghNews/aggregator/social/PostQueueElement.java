package com.pghNews.aggregator.social;

import com.pghNews.aggregator.entity.Article;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;

@Table(name = "post_queue", schema="public")
@NoArgsConstructor
@Data
@Entity
public class PostQueueElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "article_id")
    private Article article;
    private String platform;
    private String status;
    private Timestamp postedAt;
    private Timestamp scheduledAt;
    private boolean isPosted;
    private String postingNotes;
    private String externalId;

//    @Column(name = "created_at", insertable = false, updatable = false)
//    private Timestamp createdAt;

}
