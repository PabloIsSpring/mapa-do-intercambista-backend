package com.mechasystem.mapaintercambista.controller;

import com.mechasystem.mapaintercambista.dto.request.LoginRequest;
import com.mechasystem.mapaintercambista.dto.request.RegisterUserRequest;
import com.mechasystem.mapaintercambista.dto.response.LoginResponse;
import com.mechasystem.mapaintercambista.dto.response.RegisterUserResponse;
import com.mechasystem.mapaintercambista.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController (UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public String test () {
        return "Testando";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser (@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/registeruser")
    public ResponseEntity<RegisterUserResponse> registerUser (@Valid @RequestBody RegisterUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(request));
    }
}
