package com.example.CodeFellowship.controller;

import com.example.CodeFellowship.model.ApplicationUser;
import com.example.CodeFellowship.repository.ApplicationUserRepository;
import com.example.CodeFellowship.repository.PostRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

@Controller
public class GeneralController {
    private ApplicationUserRepository appRepo;
    private PostRepository postRepo;

    public GeneralController(ApplicationUserRepository appRepo, PostRepository postRepo){
        this.appRepo = appRepo;
        this.postRepo = postRepo;
    }

    @GetMapping("/")
    public String getHomePage() {
        return "indexAfterLogin.html";
    }

    @GetMapping("/indexAfterLogin")
    public String getHomePageAfterLogging() {
        return "indexAfterLogin.html";
    }


    @GetMapping("/signup")
    public String getSignupPage(){
        return "/signup.html";
    }

    @PostMapping("/signup")
    public String signupUser(String username, String password, String firstName, String lastName, String dateOfBirth, String bio, String profilePic, LocalDate localDate) {
        String hashedPassword= BCrypt.hashpw(password,BCrypt.gensalt(12));
        ApplicationUser appUser = new ApplicationUser(username, hashedPassword, firstName, lastName, dateOfBirth, bio, profilePic, localDate);
        appUser.setLocalDate(LocalDate.now());
        appRepo.save(appUser);
        return "/login.html";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "/login.html";
    }

    @PostMapping("/login")
    public RedirectView loggedInUser(HttpServletRequest request, String username, String password){
        ApplicationUser appUser = appRepo.findByUsername(username);

        if((appUser == null)
                || !(BCrypt.checkpw(password, appUser.getPassword())))
        {
            return new RedirectView("/login");
        }
        HttpSession httpSession= request.getSession();
        httpSession.setAttribute("username", username);
        return new RedirectView("/securedHome");
    }

    @PostMapping("/logout")
    public RedirectView logoutUser(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        return new RedirectView("/index");
    }

    @GetMapping("/logout")
    public String getLogout(){
        return "/indexAfterLogin.html";
    }
}
