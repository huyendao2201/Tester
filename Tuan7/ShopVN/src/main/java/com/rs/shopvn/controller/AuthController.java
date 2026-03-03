package com.rs.shopvn.controller;

import com.rs.shopvn.dto.RegisterRequest;
import com.rs.shopvn.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/register"})
    public String showRegister(Model model) {
        model.addAttribute("form", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid @ModelAttribute("form") RegisterRequest form,
                             BindingResult bindingResult,
                             Model model) {

        // unique username
        if (!bindingResult.hasFieldErrors("username")
                && form.getUsername() != null
                && userService.usernameExists(form.getUsername())) {
            bindingResult.rejectValue("username", "username.exists", "Tên đăng nhập đã tồn tại.");
        }

        // unique email
        if (!bindingResult.hasFieldErrors("email")
                && form.getEmail() != null
                && userService.emailExists(form.getEmail().trim().toLowerCase())) {
            bindingResult.rejectValue("email", "email.exists", "Email đã được đăng ký.");
        }

        // referral exists (optional)
        if (!bindingResult.hasFieldErrors("referralCode")
                && form.getReferralCode() != null
                && !form.getReferralCode().isBlank()
                && !userService.referralExists(form.getReferralCode())) {
            bindingResult.rejectValue("referralCode", "ref.notfound", "Mã giới thiệu không tồn tại trong hệ thống.");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.register(form);
        return "redirect:/success";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}