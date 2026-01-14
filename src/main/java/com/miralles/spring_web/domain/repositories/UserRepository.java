package com.miralles.spring_web.domain.repositories;

import com.miralles.spring_web.domain.models.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    void deleteById(Long id);
    Optional<User> findByEmail(String email);
}