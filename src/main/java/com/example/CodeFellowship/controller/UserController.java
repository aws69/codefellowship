package com.example.CodeFellowship.controller;

import com.example.CodeFellowship.model.ApplicationUser;
import com.example.CodeFellowship.model.Post;
import com.example.CodeFellowship.repository.ApplicationUserRepository;
import com.example.CodeFellowship.repository.PostRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    private ApplicationUserRepository appRepo;
    private PostRepository postRepo;

    public UserController(ApplicationUserRepository appRepo, PostRepository postRepo){
        this.appRepo = appRepo;
        this.postRepo = postRepo;
    }

    @GetMapping("/profile")
    public String getUserInfo(Model model, Principal p) {
        if (p != null) {
            String username = p.getName();
            ApplicationUser appUser = appRepo.findByUsername(username);

            model.addAttribute("createdDate",appUser.getLocalDate() );
            model.addAttribute("username", username);
            model.addAttribute("firstName", appUser.getFirstName());
            model.addAttribute("lastName", appUser.getLastName());
            model.addAttribute("dateOfBirth", appUser.getDateOfBirth());
            model.addAttribute("bio", appUser.getBio());
            model.addAttribute("codeFellowUserId", appUser.getId());

            List<Post> userPosts = postRepo.findByUserId(appUser);
            model.addAttribute("userPosts", userPosts);

        }

        return "profile.html";

    }

    @GetMapping("/users/{id}")
    public String getUserInfo(Model model, Principal p, @PathVariable Long id) {
        {
            if (p != null)
            {
                String username = p.getName();
                ApplicationUser appBrowsingUser = appRepo.findByUsername(username);


                model.addAttribute("username", username);
                model.addAttribute("createdDate", appBrowsingUser.getLocalDate());
                model.addAttribute("firstName", appBrowsingUser.getFirstName());
                model.addAttribute("lastName", appBrowsingUser.getLastName());
                model.addAttribute("dateOfBirth", appBrowsingUser.getDateOfBirth());
                model.addAttribute("bio", appBrowsingUser.getBio());
                model.addAttribute("profilePic", appBrowsingUser.getProfilePic());
            }

            ApplicationUser codeFellowUser = appRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found with id " + id));

            model.addAttribute("codeFellowCreatedDate", LocalDateTime.now());
            model.addAttribute("codeFellowUsername", codeFellowUser.getUsername());
            model.addAttribute("codeFellowFirstName", codeFellowUser.getFirstName());
            model.addAttribute("codeFellowLastName", codeFellowUser.getLastName());
            model.addAttribute("codeFellowDateOfBirth", codeFellowUser.getDateOfBirth());
            model.addAttribute("codeFellowBio", codeFellowUser.getBio());
            model.addAttribute("codeFellowProfilePic", codeFellowUser.getProfilePic());
            model.addAttribute("codeFellowUserId", codeFellowUser.getId());

        }

        return "userInformation.html";
    }

    @PutMapping("/users/{id}")
    public RedirectView editUserInfo(Principal p, @PathVariable Long id, String username, @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")  LocalDate localDate, RedirectAttributes redirect, String firstName, String lastName, String dateOfBirth, String bio)
    {
        if ((p != null) && (p.getName().equals(username)))
        {
            ApplicationUser appUser = appRepo.findById(id).orElseThrow();

            appUser.setBio(bio);
            appUser.setFirstName(firstName);
            appUser.setLastName(lastName);
            appUser.setDateOfBirth(dateOfBirth);
            appUser.setUsername(username);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = localDate.format(formatter);
            appUser.setLocalDate(LocalDate.parse(formattedDate, formatter));

            appRepo.save(appUser);
        }
        else
        {
            redirect.addFlashAttribute("errorMessage", "Cannot edit another user's Info!");
        }

        return new RedirectView("/users/" + id);
    }

    @GetMapping("/allusers")
    public String getAllUsers(Principal p, Model m) {
        if (p != null) {
            List<ApplicationUser> applicationUser=appRepo.findAll();
            m.addAttribute("users", applicationUser);

        }
        return "allusers.html";
    }

    @PostMapping("/follow/{id}")
    public RedirectView followUser(@PathVariable Long id, Principal principal){
        if(principal != null){
            ApplicationUser applicationUser = appRepo.findByUsername(principal.getName());
            ApplicationUser wantFollowApplicationUser = appRepo.findById(id).orElseThrow();
            if(applicationUser != null && wantFollowApplicationUser != null){
                wantFollowApplicationUser.getFollowers().add(applicationUser);
                appRepo.save(wantFollowApplicationUser);
            }
        }

        return new RedirectView("/users/" + id);
    }


    @DeleteMapping("/unfollow/{id}")
    public RedirectView unfollowUser(@PathVariable Long id) {
        ApplicationUser applicationUser = appRepo.findById(id).orElseThrow();
        ApplicationUser userToUnfollow = appRepo.findById(id).orElseThrow();
        userToUnfollow.getFollowers().remove(applicationUser);
        appRepo.save(applicationUser);
        return new RedirectView("/users/" + id);
    }


    @GetMapping("/feed")
    public String getAllFeed(Principal p , Model m){
        if (p != null)
        {
            ApplicationUser applicationUser = appRepo.findByUsername(p.getName());
            Set<ApplicationUser> followed = applicationUser.getFollowing();
            followed.remove(applicationUser);
            List<Post> posts = postRepo.findAllByApplicationUserIn(followed);
            m.addAttribute("posts", posts);
        }

        return "feed";
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
        ResourceNotFoundException(String message) {
            super(message);
        }
    }

}