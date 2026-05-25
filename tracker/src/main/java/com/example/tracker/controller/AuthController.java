package com.example.tracker.controller;

import com.example.tracker.dto.*;
import com.example.tracker.security.JwtUtils;
import com.example.tracker.security.UserDetailsServiceImpl;
import com.example.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {

        try {

            userService.register(request);

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "User registered"
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthRequest request
    ) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

        } catch (BadCredentialsException e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            Map.of(
                                    "error",
                                    "Invalid credentials"
                            )
                    );
        }

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        request.getEmail()
                );

        String token =
                jwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(
                new TokenResponse(token)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(
            Principal principal
    ) {

        return ResponseEntity.ok(
                Map.of(
                        "email",
                        principal.getName()
                )
        );
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMe(
            Principal principal
    ) {

        userService.deleteUser(
                principal.getName()
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "User deleted"
                )
        );
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal principal
    ) {

        try {

            userService.changePassword(
                    principal.getName(),
                    request
            );

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Password changed"
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }

    @GetMapping("/user/id/{id}")
    public ResponseEntity<UserResponse> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                userService.getResponseById(id)
        );
    }

    @GetMapping("/find")
    public ResponseEntity<UserResponse> getByEmail(
            @RequestParam String email
    ) {
        System.out.println("Got an email: " + email);
        return ResponseEntity.ok(
                userService.getResponseByEmail(email)
        );
    }
}