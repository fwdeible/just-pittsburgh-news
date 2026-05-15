package com.pghNews.aggregator.social;

import com.pghNews.aggregator.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostQueueRepo extends JpaRepository<PostQueueElement, Long> {

    @Query("""
        SELECT pq
        FROM PostQueueElement pq
        JOIN FETCH pq.article
        WHERE pq.isPosted = false
        ORDER BY pq.scheduledAt
        LIMIT 1
    """)
    PostQueueElement findNextUnpostedArticle();
}
