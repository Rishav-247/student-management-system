package com.example.sms.service;

import com.example.sms.dto.UserDto;
import com.example.sms.entity.Role;
import com.example.sms.entity.User;

import java.util.List;

public interface UserService {

    User registerUser(UserDto dto);

    User updateUser(Long id, UserDto dto);

    User getUserById(Long id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    void deleteUser(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    long countByRole(Role role);

    void changePassword(Long userId, String oldPassword, String newPassword);
}
