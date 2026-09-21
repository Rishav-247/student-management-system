package com.example.sms.controller;

import com.example.sms.dto.UserDto;
import com.example.sms.entity.Role;
import com.example.sms.entity.User;
import com.example.sms.security.CustomUserDetails;
import com.example.sms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", Role.values());
        model.addAttribute("activeNav", "11");
        return "users/list";
    }

    @GetMapping("/users/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("roles", Role.values());
        model.addAttribute("isEdit", false);
        model.addAttribute("activeNav", "11");
        return "users/form";
    }

    @PostMapping("/users/save")
    public String saveUser(@Valid @ModelAttribute("userDto") UserDto userDto,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", Role.values());
            model.addAttribute("isEdit", userDto.getId() != null);
            model.addAttribute("activeNav", "11");
            return "users/form";
        }

        try {
            if (userDto.getId() == null) {
                userService.registerUser(userDto);
                redirectAttributes.addFlashAttribute("successMessage", "USER ACCOUNT CREATED: " + userDto.getUsername());
            } else {
                userService.updateUser(userDto.getId(), userDto);
                redirectAttributes.addFlashAttribute("successMessage", "USER ACCOUNT UPDATED: " + userDto.getUsername());
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/users/new";
        }

        return "redirect:/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "USER ACCOUNT DELETED");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/settings")
    public String settings(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails != null) {
            User user = userService.getUserById(userDetails.getId());
            model.addAttribute("currentUser", user);
        }
        model.addAttribute("activeNav", "11");
        return "settings/index";
    }

    @PostMapping("/settings/change-password")
    public String changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "NEW PASSWORDS DO NOT MATCH");
            return "redirect:/settings";
        }

        try {
            userService.changePassword(userDetails.getId(), oldPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "PASSWORD CHANGED SUCCESSFULLY");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/settings";
    }
}
