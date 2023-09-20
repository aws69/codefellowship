package com.example.CodeFellowship.repository;


import com.example.CodeFellowship.model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepo extends JpaRepository<Posts,Long> {

}