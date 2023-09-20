package com.example.CodeFellowship.config;

import com.example.CodeFellowship.model.ApplicationUser;
import com.example.CodeFellowship.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        if (applicationUser == null) {
            System.out.println("User" + username + "was not found");
            throw new UsernameNotFoundException("User" + username + "was not found");
        }
        System.out.println("User Found:"+ applicationUser.getUsername());
        return applicationUser;
    }
}