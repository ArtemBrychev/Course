package com.example.tracker.service;

import com.example.tracker.dto.ChangePasswordRequest;
import com.example.tracker.dto.RegisterRequest;
import com.example.tracker.dto.UserResponse;
import com.example.tracker.model.User;
import com.example.tracker.model.UserRole;
import com.example.tracker.repository.UserRepository;
import com.example.tracker.repository.cache.UserCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserCacheRepository userCacheRepository;

    public User register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        if (request.getPassword().length() < 8) {
            throw new RuntimeException("Password too short");
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role(UserRole.USER)
                .build();

        return userRepository.save(user);
    }

    public User getByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("Не нашел пользователя");
                            return new RuntimeException("User not found");
                        }
                );
    }

    public void deleteUser(String email) {

        User user = getByEmail(email);

        userCacheRepository.delete(
                UserResponse.from(user)
        );

        userRepository.delete(user);
    }

    public void changePassword(
            String email,
            ChangePasswordRequest request
    ) {

        User user = getByEmail(email);

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPasswordHash()
        )) {

            throw new RuntimeException(
                    "Wrong old password"
            );
        }

        if (request.getNewPassword().length() < 8) {
            throw new RuntimeException(
                    "Password too short"
            );
        }

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

        userCacheRepository.delete(
                UserResponse.from(user)
        );
    }

    public User getById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );
    }

    public UserResponse getResponseById(Long id) {

        UserResponse cached =
                userCacheRepository.findById(id);

        if (cached != null) {

            System.out.println(
                    "CACHE HIT user:id:" + id
            );

            return cached;
        }

        System.out.println(
                "CACHE MISS user:id:" + id
        );

        User user = getById(id);

        UserResponse response =
                UserResponse.from(user);

        userCacheRepository.save(response);

        return response;
    }

    public UserResponse getResponseByEmail(
            String email
    ) {

        UserResponse cached =
                userCacheRepository.findByEmail(email);

        if (cached != null) {

            System.out.println(
                    "CACHE HIT user:email:" + email
            );

            return cached;
        }

        System.out.println(
                "CACHE MISS user:email:" + email
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        UserResponse response =
                UserResponse.from(user);

        userCacheRepository.save(response);

        return response;
    }
}