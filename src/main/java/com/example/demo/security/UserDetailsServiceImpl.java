package com.example.demo.security;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Optional<User> userOpt;

        if (identifier.contains("@")) {
            userOpt = userRepo.findByEmails_Email(identifier);
        } else if (identifier.matches("\\d{10,}")) { // phone — числа длиной от 10 символов
            userOpt = userRepo.findByPhones_Phone(identifier);
        } else {
            Long id = Long.parseLong(identifier);
            userOpt = userRepo.findById(id);
        }

        return userOpt
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
