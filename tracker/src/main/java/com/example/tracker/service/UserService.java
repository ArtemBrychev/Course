package com.example.tracker.service;

import com.example.tracker.dto.ChangePasswordRequest;
import com.example.tracker.dto.RegisterRequest;
import com.example.tracker.dto.UserResponse;
import com.example.tracker.model.User;
import com.example.tracker.model.UserRole;
import com.example.tracker.repository.UserRepository;
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

    //TODO: Возможно все таки сделать кеширование через репозитории а не вот это вот говно со слоями
    @CacheEvict(value = {"usersByEmail", "usersById"}, allEntries = true)
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

    @Cacheable(value = "usersByEmail", key = "#email")
    public User getByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("Не нашел пользователя");
                            return new RuntimeException("User not found");
                        }
                );
    }

    @CacheEvict(value = {"usersByEmail", "usersById"}, allEntries = true)
    public void deleteUser(String email) {

        User user = getByEmail(email);

        userRepository.delete(user);
    }

    @CacheEvict(value = {"usersByEmail", "usersById"}, allEntries = true)
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
    }

    @Cacheable(value = "usersById", key = "#id")
    public User getById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );
    }

    public UserResponse getResponseById(Long id) {

        User user = getById(id);

        return UserResponse.from(user);
    }

    @Cacheable(value = "usersByEmail", key = "#email")
    public UserResponse getResponseByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                            System.out.println("Не нашел пользователя");
                            return new RuntimeException("User not found");
                        }
                );;

        return UserResponse.from(user);
    }


}