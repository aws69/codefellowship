package com.example.CodeFellowship.repository;

import com.example.CodeFellowship.model.ApplicationUser;
import com.example.CodeFellowship.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(ApplicationUser user);

    List<Post> findAllByApplicationUserIn(Set<ApplicationUser> followed);
}
