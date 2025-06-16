package com.cms.cms_backend.controller;

import com.cms.cms_backend.model.Article;
import com.cms.cms_backend.model.User;
import com.cms.cms_backend.security.JwtUtil;
import com.cms.cms_backend.services.ArticleService;
import com.cms.cms_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // Create
    @PostMapping
    public ResponseEntity<Article> create(@RequestBody Article article) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articleService.create(article));
    }

    // Get all with pagination
    @GetMapping
    public ResponseEntity<Page<Article>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(articleService.getAll(page, size));
    }

    // View by ID + track recently viewed
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String tokenHeader) {

        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtUtil.validateToken(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        Optional<Article> articleOpt = articleService.getById(id);
        Optional<User> userOpt = userService.findByUsername(username);

        if (articleOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article or user not found");
        }

        userService.addToRecentlyViewed(userOpt.get(), id);
        return ResponseEntity.ok(articleOpt.get());
    }

    // Get recently viewed
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentlyViewed(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtUtil.validateToken(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        List<Long> ids = userService.getRecentlyViewed(userOpt.get());
        List<Article> articles = new ArrayList<>();
        for (Long id : ids) {
            articleService.getById(id).ifPresent(articles::add);
        }

        return ResponseEntity.ok(articles);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id, @RequestBody Article updated) {
        return ResponseEntity.ok(articleService.update(id, updated));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.ok("Deleted");
    }
}
