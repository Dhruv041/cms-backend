package com.cms.cms_backend.repository;

import com.cms.cms_backend.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    // No extra methods needed for now
}
