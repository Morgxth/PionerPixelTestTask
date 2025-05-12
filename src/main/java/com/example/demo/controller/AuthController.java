package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenUtil;
import com.example.demo.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenUtil jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid AuthRequest request) {
        try {
            String username = request.getEmail() != null ? request.getEmail() : request.getPhone();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword())
            );
            log.info("authenticated user: {}", username);
            log.info("authentication object {}", authentication.toString());
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            log.info("user details {}", userDetails.toString());
            String jwt = jwtUtils.generateToken(userDetails.getUser().getId());
            return ResponseEntity.ok(jwt);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication failed");
        }
    }
//    @PostMapping("/signup")
//    public String registerUser(@RequestBody User user) {
//        if (userRepository.existsByUsername(user.getName())) {
//            return "Error: Username is already taken!";
//        }
//        // Create new user's account
//        User newUser = new User(
//                null,
//                user.getName(),
//                encoder.encode(user.getPassword())
//        );
//        userRepository.save(newUser);
//        return "User registered successfully!";
//    }
}
