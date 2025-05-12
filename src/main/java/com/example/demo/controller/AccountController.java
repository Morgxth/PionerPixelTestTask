package com.example.demo.controller;

import com.example.demo.dto.TransferRequest;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferRequest request) {
        Long fromUserId = ((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId();
        log.info("commiting transfer from {} account to {} account", fromUserId, request.getToUserId());
        accountService.transfer(fromUserId, request.getToUserId(), request.getAmount());
        return ResponseEntity.ok().build();
    }
}

