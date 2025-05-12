package com.example.demo.controller;

import com.example.demo.dto.AddEmailRequest;
import com.example.demo.dto.AddPhoneRequest;
import com.example.demo.dto.UserDto;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/email")
    public ResponseEntity<Void> addEmail(@AuthenticationPrincipal UserDetailsImpl user,
                                         @RequestBody @Valid AddEmailRequest request) {
        userService.addEmail(user.getUser().getId(), request.getEmail());
        log.info("POST /email/ adding email {} for user {} called", request.getEmail(), user.getUser().getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/email")
    public ResponseEntity<Void> deleteEmail(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody @Valid AddEmailRequest request) {
        log.info("DELETE /email/ deleting email {} for user {} called", request.getEmail(), user.getUser().getName());
        userService.removeEmail(user.getUser().getId(), request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/phone")
    public ResponseEntity<Void> addPhone(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody @Valid AddPhoneRequest request) {
        log.info("POST /phone/ adding phone {} for user {} called", request.getPhone(), user.getUser().getName());
        userService.addPhone(user.getUser().getId(), request.getPhone());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/phone")
    public ResponseEntity<Void> deletePhone(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody @Valid AddPhoneRequest request) {
        log.info("DELETE /phone/ deleting phone {} for user {} called", request.getPhone(), user.getUser().getName());
        userService.removePhone(user.getUser().getId(), request.getPhone());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("GET /search searching users");
        List<UserDto> result = userService.searchUsers(name, email, phone, dateOfBirth, page, size);
        return ResponseEntity.ok(result);
    }
}
