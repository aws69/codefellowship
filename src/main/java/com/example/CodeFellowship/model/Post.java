package com.example.CodeFellowship.model;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String body;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser userId;

    public Post() {
    }

    public Post(String body, ApplicationUser userId,  LocalDate createdAt) {
        this.body = body;
        this.userId = userId;
        this.createdAt = createdAt;

    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }


    public ApplicationUser getUserId() {
        return userId;
    }

    public void setUserId(ApplicationUser userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                ", userId=" + userId +
                '}';
    }
}
