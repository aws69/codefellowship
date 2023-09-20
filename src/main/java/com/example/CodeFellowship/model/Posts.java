package com.example.CodeFellowship.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String body;
    private LocalDate createdAt;
    @ManyToOne
    private ApplicationUser user;

    public Posts() {
    }

    public Posts(String body, LocalDate createdAt, ApplicationUser user) {
        this.body = body;
        this.createdAt = createdAt;
        this.user = user;
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
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    public ApplicationUser getUser() {
        return user;
    }
    public void setUser(ApplicationUser user) {
        this.user = user;
    }
}