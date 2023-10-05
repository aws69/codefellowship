package com.example.CodeFellowship.controller;

import com.example.CodeFellowship.model.ApplicationUser;
import com.example.CodeFellowship.model.Post;
import com.example.CodeFellowship.repository.ApplicationUserRepository;
import com.example.CodeFellowship.repository.PostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class PostController {
    private ApplicationUserRepository appRepo;
    private PostRepository postRepo;

    public PostController(ApplicationUserRepository appRepo, PostRepository postRepo) {
        this.appRepo = appRepo;
        this.postRepo = postRepo;
    }

    @GetMapping("/myposts")
    public String fellowPostsPage(Model model, Principal p) {
        if (p != null) {
            String username = p.getName();
            ApplicationUser appUser = appRepo.findByUsername(username);

            if (appUser != null) {
                List<Post> userPosts = appUser.getPosts();
                model.addAttribute("username", username);
                model.addAttribute("userPosts", userPosts);
                model.addAttribute("createdAt", LocalDate.now());
            }
        }
        return "createposts.html";

    }

    @PostMapping("/createPost")
    public RedirectView addPost(Principal p , String body) {

        if (p != null && p.getName() != null) {
            String username = p.getName();
            ApplicationUser appUser = appRepo.findByUsername(username);

            if (appUser != null) {
                Post post = new Post(body, appUser, LocalDate.now());
                post.setUserId(appUser);
                post.setCreatedAt(LocalDate.now());

                postRepo.save(post);
            }
        }

        return new RedirectView("/posts");
    }
}
