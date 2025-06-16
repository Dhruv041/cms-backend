package com.cms.cms_backend.services;

import com.cms.cms_backend.model.User;
import com.cms.cms_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User register(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void addToRecentlyViewed(User user, Long articleId) {
        List<Long> viewed = user.getRecentlyViewedArticles();

        viewed.remove(articleId); // remove if already exists
        viewed.add(0, articleId); // add to the beginning

        // keep only latest 5
        if (viewed.size() > 5) {
            viewed = viewed.subList(0, 5);
        }

        user.setRecentlyViewedArticles(viewed);
        userRepository.save(user);
    }

    public List<Long> getRecentlyViewed(User user) {
        return user.getRecentlyViewedArticles();
    }
}
