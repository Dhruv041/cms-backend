package com.cms.cms_backend.services;

import com.cms.cms_backend.model.Article;
import com.cms.cms_backend.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Article create(Article article) {
        return articleRepository.save(article);
    }

    public Optional<Article> getById(Long id) {
        return articleRepository.findById(id);
    }

    public Page<Article> getAll(int page, int size) {
        return articleRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    public Article update(Long id, Article updated) {
        return articleRepository.findById(id)
            .map(existing -> {
                existing.setTitle(updated.getTitle());
                existing.setContent(updated.getContent());
                return articleRepository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("Article not found"));
    }

    public void delete(Long id) {
        articleRepository.deleteById(id);
    }
}
