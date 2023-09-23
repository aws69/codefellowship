package com.example.CodeFellowship.controller;

import com.example.CodeFellowship.model.ApplicationUser;
import com.example.CodeFellowship.model.Posts;
import com.example.CodeFellowship.repository.ApplicationUserRepository;
import com.example.CodeFellowship.repository.PostsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ui.Model;
import java.security.Principal;
import java.time.LocalDate;

@Controller
public class PostsController {
    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    PostsRepo postsRepo;

    @GetMapping("/CreatedPost")
    public String CreatedPost(Principal p, Model m){
        if (p != null) {
            String username = p.getName();
            ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
            m.addAttribute("user", applicationUser);
        }
        return "profile";
    }

    @PostMapping("/createPost")
    public RedirectView createPost(Principal p, @RequestParam String body){
        if (p != null) {
            String username = p.getName();
            ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);

            Posts posts = new Posts();
            posts.setBody(body);
            posts.setUser(applicationUser);
            posts.setCreatedAt(LocalDate.now());
            postsRepo.save(posts);
        }
        return new RedirectView("/profile");
    }
}
